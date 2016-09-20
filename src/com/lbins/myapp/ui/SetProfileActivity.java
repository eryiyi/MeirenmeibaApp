package com.lbins.myapp.ui;

import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
import com.lbins.myapp.upload.CommonUtil;
import com.lbins.myapp.util.CompressPhotoUtil;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.DoubleDatePickerDialog;
import com.lbins.myapp.widget.SelectPhoPopWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 设置个人资料
 */
public class SetProfileActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ImageView mine_cover;//头像
    private TextView name;//用户名
    private TextView nick;//
    private TextView sex;//
    private TextView birth;//
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private String txpic = "";
    private SelectPhoPopWindow deleteWindow;
    private String pics = "";
    private static final File PHOTO_CACHE_DIR = new File(Environment.getExternalStorageDirectory() + "/liangxun/PhotoCache");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_profile_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("设置");
        mine_cover = (ImageView) this.findViewById(R.id.mine_cover);
        name = (TextView) this.findViewById(R.id.name);
        nick = (TextView) this.findViewById(R.id.nick);
        sex = (TextView) this.findViewById(R.id.sex);
        birth = (TextView) this.findViewById(R.id.birth);

        mine_cover.setOnClickListener(this);
        this.findViewById(R.id.liner_set_profile_name).setOnClickListener(this);
        this.findViewById(R.id.liner_set_profile_nick).setOnClickListener(this);
        this.findViewById(R.id.liner_set_profile_sex).setOnClickListener(this);
        this.findViewById(R.id.liner_set_profile_birth).setOnClickListener(this);

        initData();
    }

    void initData(){
        imageLoader.displayImage(getGson().fromJson(getSp().getString("empCover", ""), String.class), mine_cover, MeirenmeibaAppApplication.txOptions, animateFirstListener);
        nick.setText(getGson().fromJson(getSp().getString("empName", ""), String.class));
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("emp_number", ""), String.class))){
            name.setText(getGson().fromJson(getSp().getString("emp_number", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("empSex", ""), String.class))){
            switch (Integer.parseInt(getGson().fromJson(getSp().getString("empSex", ""), String.class))){
                case 0:
                    sex.setText("女");
                    break;
                case 1:
                    sex.setText("男");
                    break;
            }
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("emp_birthday", ""), String.class))){
            birth.setText(getGson().fromJson(getSp().getString("emp_birthday", ""), String.class));
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.mine_cover:
            {
                //头像点击
                ShowPickDialog();
            }
                break;
            case R.id.liner_set_profile_name:
            {
                //用户名点击
            }
                break;
            case R.id.liner_set_profile_nick:
            {
                //昵称点击
                Intent intent = new Intent(SetProfileActivity.this, EditNameActivity.class);
                startActivityForResult(intent, 1000);
            }
            break;
            case R.id.liner_set_profile_sex:
            {
                //性别点击
                showSexWindows();
            }
            break;
            case R.id.liner_set_profile_birth:
            {
                //生日
                Calendar c = Calendar.getInstance();
                // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                new DoubleDatePickerDialog(SetProfileActivity.this, 0, new DoubleDatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth) {
                        String textString = String.format("%d-%d-%d", startYear,
                                startMonthOfYear + 1, startDayOfMonth);
                        //调用接口
                        updateBirthday(textString);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false).show();
            }
            break;
        }
    }


    private void showSexWindows() {
        final Dialog picAddDialog = new Dialog(SetProfileActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.sex_select_dialog, null);
        final TextView sex_man = (TextView) picAddInflate.findViewById(R.id.sex_man);
        final TextView sex_woman = (TextView) picAddInflate.findViewById(R.id.sex_woman);
        //提交
        sex_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSex("1");
                picAddDialog.dismiss();
            }
        });

        //取消
        sex_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSex("0");
                picAddDialog.dismiss();
            }
        });
        TextView btn_cancel = (TextView) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    // 选择相册，相机
    private void ShowPickDialog() {
        deleteWindow = new SelectPhoPopWindow(SetProfileActivity.this, itemsOnClick);
        //显示窗口
        deleteWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            deleteWindow.dismiss();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1000 && resultCode == 1000){
            //修改昵称
            String nameStr = data.getExtras().getString("name");
            sendNick(nameStr);
        }
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
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
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
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
                pics = CompressPhotoUtil.saveBitmap2file(photo, System.currentTimeMillis() + ".jpg", PHOTO_CACHE_DIR);
                mine_cover.setImageBitmap(photo);
                //保存头像
                sendFile(pics);
            }
        }
    }


    public void sendFile(final String pic) {
        File f = new File(pic);
        final Map<String, File> files = new HashMap<String, File>();
        files.put("file", f);
        Map<String, String> params = new HashMap<String, String>();
        CommonUtil.addPutUploadFileRequest(
                SetProfileActivity.this,
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
                                    sendCover(coverStr);
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


    //修改头像
    private void sendCover(final String coverStr) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_INFO_COVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    save("empCover", InternetURL.INTERNAL+coverStr);
                                    //调用广播
                                    Intent intent1 = new Intent("update_cover_success");
                                    sendBroadcast(intent1);
                                }else {
                                    Toast.makeText(SetProfileActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(SetProfileActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                        }
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(SetProfileActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString("empId", ""), String.class));
                params.put("empCover", coverStr);
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


    private void updateSex(final String sexStr) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_INFO_SEX_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    save("empSex", sexStr);
                                    switch (Integer.parseInt(sexStr)){
                                        case 0:
                                            sex.setText("女");
                                            break;
                                        case 1:
                                            sex.setText("男");
                                            break;
                                    }
                                    //调用广播
                                    Intent intent1 = new Intent("update_cover_success");
                                    sendBroadcast(intent1);
                                }else {
                                    Toast.makeText(SetProfileActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(SetProfileActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                        }
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(SetProfileActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString("empId", ""), String.class));
                params.put("empSex", sexStr);
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

    private void sendNick(final String nickStr) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_INFO_COVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    save("empName", nickStr);
                                    nick.setText(nickStr);
                                    //调用广播
                                    Intent intent1 = new Intent("update_cover_success");
                                    sendBroadcast(intent1);
                                }else {
                                    Toast.makeText(SetProfileActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(SetProfileActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                        }
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(SetProfileActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString("empId", ""), String.class));
                params.put("empName", nickStr);
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


    //更新生日
    private void updateBirthday(final String birthdayStr) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_INFO_BIRTH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    save("emp_birthday", birthdayStr);
                                    birth.setText(birthdayStr);
                                    //调用广播
                                    Intent intent1 = new Intent("update_cover_success");
                                    sendBroadcast(intent1);
                                }else {
                                    Toast.makeText(SetProfileActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(SetProfileActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                        }
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(SetProfileActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString("empId", ""), String.class));
                params.put("emp_birthday", birthdayStr);
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
