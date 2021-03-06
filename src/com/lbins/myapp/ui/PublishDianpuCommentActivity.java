package com.lbins.myapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.Publish_mood_GridView_Adapter;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.SuccessData;
import com.lbins.myapp.upload.CommonUtil;
import com.lbins.myapp.util.CommonDefine;
import com.lbins.myapp.util.FileUtils;
import com.lbins.myapp.util.ImageUtils;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.CustomProgressDialog;
import com.lbins.myapp.widget.NoScrollGridView;
import com.lbins.myapp.widget.SelectPhoPopWindow;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/9
 * Time: 14:51
 * 类的功能、说明写在此处.
 */
public class PublishDianpuCommentActivity extends BaseActivity implements View.OnClickListener {
    private String order_no;
    private String emp_id_seller;

    private String cont;
    /**
     * 输入框
     */
    public EditText et_sendmessage;
    private TextView title;
    private TextView right_btn;

    private SelectPhoPopWindow deleteWindow;
    private ArrayList<String> dataList = new ArrayList<String>();
    private ArrayList<String> tDataList = new ArrayList<String>();
    private List<String> uploadPaths = new ArrayList<String>();

    private static int REQUEST_CODE = 1;
    private final static int SELECT_LOCAL_PHOTO = 110;
    private Uri uri;
    private NoScrollGridView publish_moopd_gridview_image;//图片
    private Publish_mood_GridView_Adapter adapter;
    private RatingBar startNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_comment_xml);
        order_no = getIntent().getExtras().getString("order_no");
        emp_id_seller = getIntent().getExtras().getString("emp_id_seller");
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.VISIBLE);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        right_btn.setText("确定");
        right_btn.setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("添加店铺评论");
        et_sendmessage = (EditText) this.findViewById(R.id.face_content);
        dataList.add("camera_default");
        publish_moopd_gridview_image = (NoScrollGridView) this.findViewById(R.id.publish_moopd_gridview_image);
        adapter = new Publish_mood_GridView_Adapter(this, dataList);
        publish_moopd_gridview_image.setAdapter(adapter);
        publish_moopd_gridview_image.setOnItemClickListener(new GridView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String path = dataList.get(position);
                if (path.equals("camera_default")) {
                    showSelectImageDialog();
                } else {
                    Intent intent = new Intent(PublishDianpuCommentActivity.this, ImageDelActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("path", dataList.get(position));
                    startActivityForResult(intent, CommonDefine.DELETE_IMAGE);
                }
            }
        });
        startNumber = (RatingBar) this.findViewById(R.id.startNumber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
                //发布按钮
                cont = et_sendmessage.getText().toString();//评论内容
                if (StringUtil.isNullOrEmpty(cont)) {
                    Toast.makeText(this, "请输入评论内容！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cont.length() > 200){
                    Toast.makeText(this, "评论超出字段限制！最多200字", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(startNumber.getRating()==0.0){
                    Toast.makeText(this, "请选择评价星级！", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = new CustomProgressDialog(PublishDianpuCommentActivity.this, "",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                //检查有没有选择图片
                if (dataList.size() <=1 ) {
                    publish_comment();
                    return;
                } else {
                    for (int i = 1; i < dataList.size(); i++) {
                        Bitmap bm = FileUtils.getSmallBitmap(dataList.get(i));
                        final String pic = FileUtils.saveBitToSD(bm, System.currentTimeMillis() + ".jpg");
                        File f = new File(pic);
                        final Map<String, File> files = new HashMap<String, File>();
                        files.put("file", f);
                        Map<String, String> params = new HashMap<String, String>();
                        CommonUtil.addPutUploadFileRequest(
                                PublishDianpuCommentActivity.this,
                                InternetURL.UPLOAD_FILE,
                                files,
                                params,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        if (StringUtil.isJson(s)) {
                                            try {
                                                JSONObject jo = new JSONObject(s);
                                                String code = jo.getString("code");
                                                if (Integer.parseInt(code) == 200) {
                                                    String coverStr = jo.getString("data");
                                                    uploadPaths.add(coverStr);
                                                    if (uploadPaths.size() == (dataList.size()-1)) {
                                                        publish_comment();
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        if (progressDialog != null) {
                                            progressDialog.dismiss();
                                        }
                                    }
                                },
                                null);
                    }
                }

                break;

        }
    }

    //开始发布
    private void publish_comment() {
        final StringBuffer filePath = new StringBuffer();
        if(uploadPaths != null && uploadPaths.size()>0){
            for (int i = 0; i < uploadPaths.size(); i++) {
                filePath.append(uploadPaths.get(i));
                if (i != uploadPaths.size() - 1) {
                    filePath.append(",");
                }
            }
        }
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.saveDianpuComment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                Toast.makeText(PublishDianpuCommentActivity.this, "添加评论成功！", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent("add_goods_comment_success");
                                intent1.putExtra("order_no", order_no);
                                sendBroadcast(intent1);
                                finish();
                            }
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(PublishDianpuCommentActivity.this, "添加评论失败！", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", getGson().fromJson(getSp().getString("empId", ""), String.class));
                params.put("emp_id_seller", emp_id_seller);
                params.put("dianpu_comment_cont", cont);
                if(!StringUtil.isNullOrEmpty(String.valueOf(filePath))){
                    params.put("dianpu_comment_pic", String.valueOf(filePath));
                }
                params.put("starNumber", String.valueOf((int)startNumber.getRating()));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    // 选择相册，相机
    private void showSelectImageDialog() {
        deleteWindow = new SelectPhoPopWindow(PublishDianpuCommentActivity.this, itemsOnClick);
        //显示窗口
        deleteWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            deleteWindow.dismiss();
            switch (v.getId()) {
                case R.id.camera: {
                    Intent cameraIntent = new Intent();
                    cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    // 根据文件地址创建文件
                    File file = new File(CommonDefine.FILE_PATH);
                    if (file.exists()) {
                        file.delete();
                    }
                    uri = Uri.fromFile(file);
                    // 设置系统相机拍摄照片完成后图片文件的存放地址
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                    // 开启系统拍照的Activity
                    startActivityForResult(cameraIntent, CommonDefine.TAKE_PICTURE_FROM_CAMERA);
                }
                break;
                case R.id.mapstorage: {
                    Intent intent = new Intent(PublishDianpuCommentActivity.this, AlbumActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("dataList", getIntentArrayList(dataList));
                    bundle.putString("editContent", et_sendmessage.getText().toString());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, CommonDefine.TAKE_PICTURE_FROM_GALLERY);
                }
                break;
                default:
                    break;
            }
        }

    };


    private void openCamera() {
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
        // 根据文件地址创建文件
        File file = new File(CommonDefine.FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        uri = Uri.fromFile(file);
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        // 开启系统拍照的Activity
        startActivityForResult(cameraIntent, CommonDefine.TAKE_PICTURE_FROM_CAMERA);
    }

    private ArrayList<String> getIntentArrayList(ArrayList<String> dataList) {

        ArrayList<String> tDataList = new ArrayList<String>();

        for (String s : dataList) {
//            if (!s.contains("camera_default")) {
            tDataList.add(s);
//            }
        }
        return tDataList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_LOCAL_PHOTO:
                    tDataList = data.getStringArrayListExtra("datalist");
                    if (tDataList != null) {
                        for (int i = 0; i < tDataList.size(); i++) {
                            String string = tDataList.get(i);
                            dataList.add(string);
                        }
//                        if (dataList.size() < 9) {
//                            dataList.add("camera_default");
//                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        finish();
                    }
                    break;
                case CommonDefine.TAKE_PICTURE_FROM_CAMERA:
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        return;
                    }
                    Bitmap bitmap = ImageUtils.getUriBitmap(this, uri, 400, 400);
                    String cameraImagePath = FileUtils.saveBitToSD(bitmap, System.currentTimeMillis() + ".jpg");

                    dataList.add(cameraImagePath);
                    adapter.notifyDataSetChanged();
                    break;
                case CommonDefine.TAKE_PICTURE_FROM_GALLERY:
                    tDataList = data.getStringArrayListExtra("datalist");
                    if (tDataList != null) {
                        dataList.clear();
                        for (int i = 0; i < tDataList.size(); i++) {
                            String string = tDataList.get(i);
                            dataList.add(string);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case CommonDefine.DELETE_IMAGE:
                    int position = data.getIntExtra("position", -1);
                    dataList.remove(position);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
