package com.lbins.myapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
public class PublishGoodCommentActivity extends BaseActivity implements View.OnClickListener {
    private String record_uuid;
    private String emp_id;
    private String order_no;

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
        emp_id = getIntent().getExtras().getString("emp_id");
        record_uuid = getIntent().getExtras().getString("goods_id");
        order_no = getIntent().getExtras().getString("order_no");
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.VISIBLE);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        right_btn.setText("确定");
        right_btn.setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("添加评论");
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
                if (path.contains("camera_default") && position == dataList.size() - 1 && dataList.size() - 1 != 9) {
                    showSelectImageDialog();
                } else {
                    Intent intent = new Intent(PublishGoodCommentActivity.this, ImageDelActivity.class);
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
                progressDialog = new CustomProgressDialog(PublishGoodCommentActivity.this, "正在加载中",R.anim.custom_dialog_frame);
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
                                PublishGoodCommentActivity.this,
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
                InternetURL.PUBLISH_GOODS_COMMNENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                Toast.makeText(PublishGoodCommentActivity.this, "添加评论成功！", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent("add_goods_comment_success");
                                intent1.putExtra("order_no", order_no);
                                sendBroadcast(intent1);
                                finish();
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
                        Toast.makeText(PublishGoodCommentActivity.this, "添加评论失败！", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("goodsId", record_uuid);
                params.put("empId", getGson().fromJson(getSp().getString("empId", ""), String.class));
                params.put("fplempid", "");//父评论人
                params.put("fplid", "");
                params.put("content", cont);
                if(!StringUtil.isNullOrEmpty(emp_id)){
                    params.put("goodsEmpId", emp_id);//商品所有者
                }else {
                    params.put("goodsEmpId", "");//商品所有者
                }
                if(!StringUtil.isNullOrEmpty(String.valueOf(filePath))){
                    params.put("comment_pic", String.valueOf(filePath));
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
        deleteWindow = new SelectPhoPopWindow(PublishGoodCommentActivity.this, itemsOnClick);
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
                    Intent intent = new Intent(PublishGoodCommentActivity.this, AlbumActivity.class);
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


}
