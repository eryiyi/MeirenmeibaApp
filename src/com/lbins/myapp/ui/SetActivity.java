package com.lbins.myapp.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.base.ActivityTack;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.VersionUpdateObjData;
import com.lbins.myapp.entity.VersionUpdateObj;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.CustomProgressDialog;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 设置
 */
public class SetActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("设置");
        this.findViewById(R.id.liner_set_profile).setOnClickListener(this);
        this.findViewById(R.id.liner_set_pwr).setOnClickListener(this);
        this.findViewById(R.id.liner_set_address).setOnClickListener(this);
        this.findViewById(R.id.liner_set_liulan).setOnClickListener(this);
        this.findViewById(R.id.liner_set_version).setOnClickListener(this);
        this.findViewById(R.id.liner_aboutus).setOnClickListener(this);
        this.findViewById(R.id.liner_kefu).setOnClickListener(this);
        this.findViewById(R.id.liner_set_notice).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner_set_profile:
            {
                //个人资料
                Intent intent = new Intent(SetActivity.this, SetProfileActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_set_pwr:
            {
                //密码
                Intent pwrV = new Intent(SetActivity.this, SetPwrSelectActivity.class);
                startActivity(pwrV);
            }
            break;
            case R.id.liner_set_address:
            {
                //地址
                Intent pwrV = new Intent(SetActivity.this, MineAddressActivity.class);
                startActivity(pwrV);
            }
            break;
            case R.id.liner_set_liulan:
            {
                //浏览
            }
            break;
            case R.id.liner_set_version:
            {
                //版本
                //版本更新
                Resources res = getBaseContext().getResources();
                String message = res.getString(R.string.check_new_version).toString();
                progressDialog = new CustomProgressDialog(SetActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                initData();
            }
            break;
            case R.id.liner_aboutus:
            {
                Intent intetn = new Intent(SetActivity.this, AboutUsActivity.class);
                startActivity(intetn);
            }
                break;
            case R.id.liner_kefu:
            {
                Intent intetn = new Intent(SetActivity.this, KefuTelActivity.class);
                startActivity(intetn);
            }
                break;
            case R.id.liner_set_notice:
            {
                Intent intetn = new Intent(SetActivity.this, NoticesActivity.class);
                startActivity(intetn);
            }
                break;
        }
    }


    public void initData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.CHECK_VERSION_CODE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    VersionUpdateObjData data = getGson().fromJson(s, VersionUpdateObjData.class);
                                    VersionUpdateObj versionUpdateObj = data.getData();
                                    if("true".equals(versionUpdateObj.getFlag())){
                                        //更新
                                        final Uri uri = Uri.parse(versionUpdateObj.getDurl());
                                        final Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(it);
                                    }else{
                                        showMsg(SetActivity.this, "已是最新版本");
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SetActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SetActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mm_version_code", getV());
                params.put("mm_version_package", "com.Lbins.Mlt");
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

//    public String getVersion() {
//        try {
//            PackageManager manager = this.getPackageManager();
//            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
//            String version = info.versionName;
//            return this.getString(R.string.version_name) + version;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return this.getString(R.string.can_not_find_version_name);
//        }
//    }

    String getV(){
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public void quiteAction(View view){
        //退出
        save("empPass", "");
        ActivityTack.getInstanse().exit(SetActivity.this);
    }

}
