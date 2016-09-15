package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.SuccessData;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.CustomProgressDialog;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("申请入驻");
        shanghuming = (EditText) this.findViewById(R.id.shanghuming);
        checkbox =  (CheckBox) this.findViewById(R.id.checkbox);
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("ruzhu_apply", ""), String.class))){
            shanghuming.setText(getGson().fromJson(getSp().getString("ruzhu_apply", ""), String.class));
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

    public void sureAction(View view){
        if(StringUtil.isNullOrEmpty(shanghuming.getText().toString())){
            showMsg(ApplyActivity.this, "请输入你的店铺名称！");
            return;
        }

        progressDialog = new CustomProgressDialog(ApplyActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        upDianpu();
    }

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
                            } else {
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
                params.put("emp_id", getGson().fromJson(getSp().getString("empId", ""), String.class));
                if(checkbox.isChecked() && !StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.latStr) && !StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.lngStr)){
                    params.put("lat_company", MeirenmeibaAppApplication.latStr);
                    params.put("lng_company", MeirenmeibaAppApplication.lngStr);
                }
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
