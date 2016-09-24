package com.lbins.myapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.lbins.myapp.base.BaseFragment;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.ui.*;
import com.lbins.myapp.upload.CommonUtil;
import com.lbins.myapp.util.CompressPhotoUtil;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.SelectPhoPopWindow;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/7/1.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener{
    private View view;
    private Resources res;
    private TextView title;

    private ImageView mine_cover;//我的头像
    private TextView mine_type;//我的类型
    private TextView mine_name;//我的名字
//    private TextView mine_money;//我的金钱
    private TextView mine_number;//我的账号
    private ImageView mine_erweima;//我的二维码

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private String txpic = "";
    private SelectPhoPopWindow deleteWindow;
    private String pics = "";
    private static final File PHOTO_CACHE_DIR = new File(Environment.getExternalStorageDirectory() + "/liangxun/PhotoCache");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.four_fragment, null);
        res = getActivity().getResources();
        initView();
        initData();
        return view;
    }

    void initView(){
        view.findViewById(R.id.back).setVisibility(View.GONE);
        view.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) view.findViewById(R.id.title);
        title.setText("个人中心");
        mine_cover = (ImageView) view.findViewById(R.id.mine_cover);
        mine_type = (TextView) view.findViewById(R.id.mine_type);
        mine_name = (TextView) view.findViewById(R.id.mine_name);
        mine_number = (TextView) view.findViewById(R.id.mine_number);
//        mine_money = (TextView) view.findViewById(R.id.mine_money);
        mine_erweima = (ImageView) view.findViewById(R.id.mine_erweima);


        mine_cover.setOnClickListener(this);
        mine_erweima.setOnClickListener(this);
        view.findViewById(R.id.liner_profile_unpay).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_ungetgoods).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_uncomment).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_unback).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_unpay).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_order).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_comment).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_favour).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_cart).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_packget).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_meet).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_fensi).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_count).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_ad).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_set).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_ruzhu).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_dianjia).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_liulan).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mine_cover:
            {
                //头像点击
                ShowPickDialog();
            }
                break;
            case R.id.liner_profile_unpay:
            {
                //未付款
                Intent intent = new Intent(getActivity(), MineOrdersActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.liner_profile_ungetgoods:
            {
                //待收货
                Intent intent = new Intent(getActivity(), MineOrdersActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_profile_uncomment:
            {
                //待评价
                Intent intent = new Intent(getActivity(), MineOrdersActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_profile_unback:
            {
                //退款退回
            }
                break;
            case R.id.liner_profile_order:
            {
                //我的订单
                Intent intent = new Intent(getActivity(), MineOrdersActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_profile_comment:
            {
                //我的点评
            }
                break;
            case R.id.liner_profile_favour:
            {
                //我的收藏
                Intent intent = new Intent(getActivity(), MineFavoursActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_profile_cart:
            {
                //我的购物车
                Intent intent = new Intent(getActivity(), MineCartActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_profile_packget:
            {
                //我的钱包
                Intent intent = new Intent(getActivity(),   MinePackageActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_profile_meet:
            {
                //我的预约
            }
                break;
            case R.id.liner_profile_fensi:
            {
                //粉丝
                Intent intent = new Intent(getActivity(),   MineFensiActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_profile_count:
            {
                //积分
            }
                break;
            case R.id.liner_profile_ad:
            {
                //推广
            }
                break;
            case R.id.liner_profile_set:
            {
                //我的设置
                Intent setV =  new Intent(getActivity(), SetActivity.class);
                startActivity(setV);
            }
                break;
            case R.id.liner_profile_ruzhu:
            {
                //我的入驻
                Intent intent = new Intent(getActivity(), ApplyDianpuActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.liner_profile_dianjia:
            {
                //我的店家
            }
                break;
            case R.id.liner_profile_liulan:
            {
                //我的浏览
                Intent intent = new Intent(getActivity(), MineBrowsingActivity.class);
                startActivity(intent);
            }
                break;

        }
    }

//    private TextView mine_type;//我的类型
//    private TextView mine_money;//我的金钱
//    private ImageView mine_erweima;//我的二维码

    void initData(){
        imageLoader.displayImage(getGson().fromJson(getSp().getString("empCover", ""), String.class), mine_cover, MeirenmeibaAppApplication.txOptions, animateFirstListener);
        mine_name.setText(getGson().fromJson(getSp().getString("empName", ""), String.class));
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("emp_number", ""), String.class))){
            mine_number.setText("账号："+getGson().fromJson(getSp().getString("emp_number", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("levelName", ""), String.class))){
            mine_type.setText(getGson().fromJson(getSp().getString("levelName", ""), String.class));
        }
//        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("jfcount", ""), String.class))){
//            mine_money.setText("积分"+getGson().fromJson(getSp().getString("jfcount", ""), String.class));
//        }
    }

    // 选择相册，相机
    private void ShowPickDialog() {
        deleteWindow = new SelectPhoPopWindow(getActivity(), itemsOnClick);
        //显示窗口
        deleteWindow.showAtLocation(getActivity().findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

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
                getActivity(),
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
//                                    Intent intent1 = new Intent("update_cover_success");
//                                    getActivity().sendBroadcast(intent1);
                                }else {
                                    Toast.makeText(getActivity(), jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getActivity(), R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), R.string.add_failed, Toast.LENGTH_SHORT).show();
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

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("update_cover_success")) {
                imageLoader.displayImage(getGson().fromJson(getSp().getString("empCover", ""), String.class), mine_cover, MeirenmeibaAppApplication.txOptions, animateFirstListener);
                mine_name.setText(getGson().fromJson(getSp().getString("empName", ""), String.class));
            }
            if(action.equals("update_location_success")){
                //更新当前城市
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("update_cover_success");
        myIntentFilter.addAction("update_location_success");//更新选择的城市
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }
}
