package com.lbins.myapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
 * 支付密码
 */
public class UpdatePayPwrActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText pwr_one;
    private EditText pwr_two;
    private EditText pwr_three;
    private LinearLayout liner_one;//原始密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pay_pwr_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("设置");
        liner_one = (LinearLayout) this.findViewById(R.id.liner_one);
        pwr_one = (EditText) this.findViewById(R.id.pwr_one);
        pwr_two = (EditText) this.findViewById(R.id.pwr_two);
        pwr_three = (EditText) this.findViewById(R.id.pwr_three);
        //判断是否第一次设置支付密码
        if(StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("emp_pay_pass", ""), String.class))){
            //第一次
            liner_one.setVisibility(View.GONE);
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
    public void updatePayPwrAction(View view){
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("emp_pay_pass", ""), String.class))){
            //说明不是第一次
            if(StringUtil.isNullOrEmpty(pwr_one.getText().toString())){
                showMsg(UpdatePayPwrActivity.this, "请输入原始支付密码");
                return;
            }
            if(!getGson().fromJson(getSp().getString("emp_pay_pass", ""), String.class).equals(pwr_one.getText().toString())){
                showMsg(UpdatePayPwrActivity.this, "请输入正确的原始支付密码");
            }
        }

        if(StringUtil.isNullOrEmpty(pwr_two.getText().toString())){
            showMsg(UpdatePayPwrActivity.this, "请输入支付密码");
            return;
        }
        if(StringUtil.isNullOrEmpty(pwr_three.getText().toString())){
            showMsg(UpdatePayPwrActivity.this, "请输入确认密码");
            return;
        }

        if(!pwr_three.getText().toString().equals(pwr_two.getText().toString())){
            showMsg(UpdatePayPwrActivity.this, "两次输入密码不一致！！");
            return;
        }
        if(pwr_one.getText().toString().equals(pwr_two.getText().toString())){
            showMsg(UpdatePayPwrActivity.this, "新密码不能和原来的密码一致！！");
            return;
        }

        progressDialog = new CustomProgressDialog(UpdatePayPwrActivity.this, "",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        updatePayPass();
    }


    private void updatePayPass() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_INFO_PAY_PASS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    save("emp_pay_pass", pwr_two.getText().toString());
                                    showMsg(UpdatePayPwrActivity.this, "支付密码更新成功！");
                                    finish();
                                }else {
                                    showMsg(UpdatePayPwrActivity.this, "修改失败！");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(UpdatePayPwrActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UpdatePayPwrActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_pay_pass", pwr_two.getText().toString());
                params.put("empId", getGson().fromJson(getSp().getString("empId", ""), String.class));
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
