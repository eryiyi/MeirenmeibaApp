package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.lbins.myapp.MainActivity;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.MemberData;
import com.lbins.myapp.entity.Member;
import com.lbins.myapp.util.HttpUtils;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.util.Utils;
import com.lbins.myapp.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText mobile;
    private EditText pwr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("登录");
        mobile = (EditText) this.findViewById(R.id.mobile);
        pwr = (EditText) this.findViewById(R.id.pwr);

        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("empMobile", ""), String.class)) &&
                !StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("empPass", ""), String.class))){
            mobile.setText(getGson().fromJson(getSp().getString("empMobile", ""), String.class));
            pwr.setText(getGson().fromJson(getSp().getString("empPass", ""), String.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;

        }
    }
    public void regAction(View view){
        Intent intent = new Intent(LoginActivity.this, RegOneActivity.class);
        startActivity(intent);
    }

    boolean isMobileNet, isWifiNet;

    public void loginAction(View view){
        try {
            isMobileNet = HttpUtils.isMobileDataEnable(getApplicationContext());
            isWifiNet = HttpUtils.isWifiDataEnable(getApplicationContext());
            if (!isMobileNet && !isWifiNet) {
                Toast.makeText(this, R.string.net_work_error, Toast.LENGTH_SHORT).show();
                return;
            }else{
                //登录
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(LoginActivity.this ,"请输入手机号");
                    return;
                }
                if(StringUtil.isNullOrEmpty(pwr.getText().toString())){
                    showMsg(LoginActivity.this ,"请输入密码");
                    return;
                }

                progressDialog = new CustomProgressDialog(LoginActivity.this, "",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                login();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void login() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.LOGIN__URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    MemberData data = getGson().fromJson(s, MemberData.class);
                                    Member member = data.getData();
                                    saveMember(member);
                                }else {
                                    switch (Integer.parseInt(code)){
                                        case 1:
                                            showMsg(LoginActivity.this, "暂无此用户，请检查用户名！");
                                            break;
                                        case 2:
                                            showMsg(LoginActivity.this, "用户密码错误");
                                            break;
                                        case 3:
                                            showMsg(LoginActivity.this, "此用户暂不可用");
                                            break;
                                        case 4:
                                            showMsg(LoginActivity.this, "请输入用户名");
                                            break;
                                        case 5:
                                            showMsg(LoginActivity.this, "请输入密码");
                                            break;
                                        case 6:
                                            showMsg(LoginActivity.this, "注册时密码为空，您不能登录！请重新注册");
                                            break;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(LoginActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LoginActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", mobile.getText().toString());
                params.put("password", pwr.getText().toString());
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

    public void saveMember(Member member){
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue(LoginActivity.this, "api_key"));
        save("empId", member.getEmpId());
        save("emp_number", member.getEmp_number());
        save("empMobile", member.getEmpMobile());
        save("empPass", pwr.getText().toString());
        save("empName", member.getEmpName());
        save("empCover", member.getEmpCover());
        save("empSex", member.getEmpSex());
        save("isUse", member.getIsUse());
        save("dateline", member.getDateline());
        save("emp_birthday", member.getEmp_birthday());
        save("pushId", member.getPushId());
        save("hxUsername", member.getHxUsername());
        save("isInGroup", member.getIsInGroup());
        save("deviceType", member.getDeviceType());
        save("lat", member.getLat());
        save("lng", member.getLng());
        save("level_id", member.getLevel_id());
        save("emp_erweima", member.getEmp_erweima());
        save("emp_up", member.getEmp_up());
        save("emp_up_mobile", member.getEmp_up_mobile());
        save("levelName", member.getLevelName());
        save("jfcount", member.getJfcount());
        save("emp_pay_pass", member.getEmp_pay_pass());
        save("package_money", member.getPackage_money());
        save("empType", member.getEmpType());
        save("is_card_emp", member.getIs_card_emp());
        save("lx_attribute_id", member.getLx_attribute_id());
        save("level_zhe", member.getLevel_zhe());

        save("isLogin", "1");//1已经登录了  0未登录
        save("is_card", member.getIs_card());

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void findAction(View view){
        //找回账号密码
        Intent intent = new Intent(LoginActivity.this, FindPwrMobileActivity.class);
        startActivity(intent);
    }

}
