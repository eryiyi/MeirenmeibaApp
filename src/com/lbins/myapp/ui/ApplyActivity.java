package com.lbins.myapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.AnimateFirstDisplayListener;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.SuccessData;
import com.lbins.myapp.upload.CommonUtil;
import com.lbins.myapp.util.CompressPhotoUtil;
import com.lbins.myapp.util.FileUtils;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.CustomProgressDialog;
import com.lbins.myapp.widget.SelectPhoPopWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 申请入驻
 */
public class ApplyActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText shanghuming;//商户名
    private CheckBox checkbox;

    private TextView lx_class;

    private ImageView pic_one;
    private ImageView pic_two;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private static final File PHOTO_CACHE_DIR = new File(Environment.getExternalStorageDirectory() + "/liangxun/PhotoCache");
    private String txpic = "";
    private String pics = "";

    private String piconeStr = "";
    private String pictwoStr = "";

    private EditText company_person;
    private EditText company_tel;
    private EditText idcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("申请入驻");
        shanghuming = (EditText) this.findViewById(R.id.shanghuming);
        lx_class = (TextView) this.findViewById(R.id.lx_class);
        lx_class.setOnClickListener(this);
        checkbox =  (CheckBox) this.findViewById(R.id.checkbox);
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("ruzhu_apply", ""), String.class))){
            shanghuming.setText(getGson().fromJson(getSp().getString("ruzhu_apply", ""), String.class));
        }

        pic_one = (ImageView) this.findViewById(R.id.pic_one);
        pic_two = (ImageView) this.findViewById(R.id.pic_two);
        pic_one.setOnClickListener(this);
        pic_two.setOnClickListener(this);
        company_person = (EditText) this.findViewById(R.id.company_person);
        company_tel = (EditText) this.findViewById(R.id.company_tel);
        idcard = (EditText) this.findViewById(R.id.idcard);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.lx_class:
            {
                //店铺分类
//                Intent intent = new Intent(ApplyActivity.this, SelectLxClassActivity.class);
//                startActivityForResult(intent, 1000);
                Intent intent = new Intent(ApplyActivity.this, SearchMoreClassActivitySelect.class);
                startActivityForResult(intent, 1000);
            }
                break;
            case R.id.pic_one:
                //
                tmpSelect = "1";
                showSelectImageDialog();
                break;
            case R.id.pic_two:
                //
                tmpSelect = "2";
                showSelectImageDialog();
                break;
        }
    }

    private Uri uri;
    private SelectPhoPopWindow selectPhoPop;
    String tmpSelect = "";


    public void sureAction(View view){
        if(StringUtil.isNullOrEmpty(shanghuming.getText().toString())){
            showMsg(ApplyActivity.this, "请输入你的店铺名称！");
            return;
        }
        if(StringUtil.isNullOrEmpty(lx_class_id)){
            showMsg(ApplyActivity.this, "请选择店铺分类！");
            return;
        }
        if(StringUtil.isNullOrEmpty(company_person.getText().toString())){
            showMsg(ApplyActivity.this, "请输入店铺联系人！");
            return;
        }
        if(StringUtil.isNullOrEmpty(company_tel.getText().toString())){
            showMsg(ApplyActivity.this, "请输入联系电话！");
            return;
        }
        if(StringUtil.isNullOrEmpty(idcard.getText().toString())){
            showMsg(ApplyActivity.this, "请输入身份证号！");
            return;
        }
        if(StringUtil.isNullOrEmpty(txpic)){
            showMsg(ApplyActivity.this, "请上传身份证！");
            return;
        }
        if(StringUtil.isNullOrEmpty(pics)){
            showMsg(ApplyActivity.this, "请上传营业执照！");
            return;
        }
        progressDialog = new CustomProgressDialog(ApplyActivity.this, "",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        uploadPicMine(txpic, "1");
        uploadPicMine(pics, "2");
        upDianpu();
    }

    void uploadPicMine(String txpic, final String tmpPic) {
        File f = new File(txpic);
        final Map<String, File> files = new HashMap<String, File>();
        files.put("file", f);
        Map<String, String> params = new HashMap<String, String>();
        CommonUtil.addPutUploadFileRequest(
                ApplyActivity.this,
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
                                    if ("1".equals(tmpPic)) {
                                        piconeStr = jo.getString("data");
                                    }
                                    if ("2".equals(tmpPic)) {
                                        pictwoStr = jo.getString("data");
                                    }
                                    if (!StringUtil.isNullOrEmpty(piconeStr) && !StringUtil.isNullOrEmpty(pictwoStr)) {
                                        //已经上传完成
                                        upDianpu();
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

    // 选择相册，相机
    private void showSelectImageDialog() {
        selectPhoPop = new SelectPhoPopWindow(ApplyActivity.this, itemsOnClick);
        //显示窗口
        selectPhoPop.showAtLocation(ApplyActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            selectPhoPop.dismiss();
            switch (v.getId()) {
                case R.id.camera: {
                    Intent camera = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                            .fromFile(new File(Environment
                                    .getExternalStorageDirectory(),
                                    "ppCover.jpg")));
                    startActivityForResult(camera, 2);
                }
                break;
                case R.id.mapstorage: {
                    Intent mapstorage = new Intent(Intent.ACTION_PICK, null);
                    mapstorage.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    startActivityForResult(mapstorage, 1);
                }
                break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
//                    setPicToView(data);
                }
                break;
            // 如果是调用相机拍照时
            case 2:
                File temp = new File(Environment.getExternalStorageDirectory()
                        + "/ppCover.jpg");
                startPhotoZoom(Uri.fromFile(temp));
                break;
            // 取得裁剪后的图片
            case 3:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            case 1000:
            {
                switch (resultCode) {
                    case 1001:
                    {
                        lx_class_id = data.getStringExtra("cloud_caoping_guige_id");
                        String cloud_caoping_guige_cont = data.getStringExtra("cloud_caoping_guige_cont");
                        if(!StringUtil.isNullOrEmpty(cloud_caoping_guige_cont)){
                            lx_class.setText(cloud_caoping_guige_cont);
                        }
                    }
                    break;
                }
            }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            if (photo != null) {
                if ("1".equals(tmpSelect)) {
                    txpic = CompressPhotoUtil.saveBitmap2file(photo, System.currentTimeMillis() + ".jpg", PHOTO_CACHE_DIR);
                    pic_one.setImageBitmap(photo);
                } else {
                    pics = CompressPhotoUtil.saveBitmap2file(photo, System.currentTimeMillis() + ".jpg", PHOTO_CACHE_DIR);
                    pic_two.setImageBitmap(photo);
                }
            }
        }
    }

    private String lx_class_id;

    public void upDianpu(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.EMP_APPLY_DIANPU_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                save("ruzhu_apply", shanghuming.getText().toString());
                                showMsg(ApplyActivity.this, "申请入驻成功，请等待管理员审核！");
                                finish();
                            }else if(data.getCode() == 1){
                                showMsg(ApplyActivity.this, "申请失败，您已经申请入驻，不能重复申请！");
                            }
                            else {
                                Toast.makeText(ApplyActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ApplyActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ApplyActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("company_name", shanghuming.getText().toString());
                params.put("lx_class_id", lx_class_id);
                params.put("emp_id", getGson().fromJson(getSp().getString("empId", ""), String.class));
                if(checkbox.isChecked() && !StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.latStr) && !StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.lngStr)){
                    params.put("lat_company", MeirenmeibaAppApplication.latStr);
                    params.put("lng_company", MeirenmeibaAppApplication.lngStr);
                }
                params.put("company_person", company_person.getText().toString());
                params.put("company_tel", company_tel.getText().toString());
                params.put("idcard", idcard.getText().toString());
                params.put("idcardUrl", piconeStr);
                params.put("company_yyzz", pictwoStr);
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
}
