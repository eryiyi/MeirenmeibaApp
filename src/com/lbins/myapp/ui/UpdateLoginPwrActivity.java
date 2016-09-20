package com.lbins.myapp.ui;

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
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 修改登录密码
 */
public class UpdateLoginPwrActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText pwr_one;
    private EditText pwr_two;
    private EditText pwr_three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_login_pwr_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("设置");
        pwr_one = (EditText) this.findViewById(R.id.pwr_one);
        pwr_two = (EditText) this.findViewById(R.id.pwr_two);
        pwr_three = (EditText) this.findViewById(R.id.pwr_three);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
    public void updateLoginPwrAction(View view){
        if(StringUtil.isNullOrEmpty(pwr_one.getText().toString())){
            showMsg(UpdateLoginPwrActivity.this, "请输入原始密码！");
            return;
        }
        if(StringUtil.isNullOrEmpty(pwr_two.getText().toString())){
            showMsg(UpdateLoginPwrActivity.this, "请输入新密码！");
            return;
        }
        if(pwr_two.getText().toString().length() >18 || pwr_two.getText().toString().length() < 6){
            showMsg(UpdateLoginPwrActivity.this, "请输入6到18位密码！");
            return;
        }
        if(StringUtil.isNullOrEmpty(pwr_three.getText().toString())){
            showMsg(UpdateLoginPwrActivity.this, "请输入确认密码！");
            return;
        }
        if(!pwr_three.getText().toString().equals(pwr_two.getText().toString())){
            showMsg(UpdateLoginPwrActivity.this, "两次输入密码不一致！！");
            return;
        }

        if(!getGson().fromJson(getSp().getString("empPass", ""), String.class).equals(pwr_one.getText().toString())){
            showMsg(UpdateLoginPwrActivity.this, "您输入的密码有误！请输入正确的原始密码！");
            return;
        }
        progressDialog = new CustomProgressDialog(UpdateLoginPwrActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        updatePwr();
    }

    private void updatePwr() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_PWR__URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    showMsg(UpdateLoginPwrActivity.this, "修改密码成功！");
                                    save("empPass", pwr_two.getText().toString());
                                    finish();
                                }else {
                                    showMsg(UpdateLoginPwrActivity.this, jo.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(UpdateLoginPwrActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UpdateLoginPwrActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString("empId", ""), String.class));
                params.put("rePass",pwr_two.getText().toString());
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
