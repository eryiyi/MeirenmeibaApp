package com.lbins.myapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
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

    private TextView lx_class;

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
        }
    }

    public void sureAction(View view){
        if(StringUtil.isNullOrEmpty(shanghuming.getText().toString())){
            showMsg(ApplyActivity.this, "请输入你的店铺名称！");
            return;
        }
        if(StringUtil.isNullOrEmpty(lx_class_id)){
            showMsg(ApplyActivity.this, "请选择店铺分类！");
            return;
        }
        progressDialog = new CustomProgressDialog(ApplyActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        upDianpu();
    }

    private String lx_class_id;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 1000){
//            // 根据上面发送过去的请求吗来区别
//            switch (resultCode) {
//                case 1001:
//                {
//                    lx_class_id = data.getStringExtra("cloud_caoping_guige_id");
//                    String cloud_caoping_guige_cont = data.getStringExtra("cloud_caoping_guige_cont");
//                    if(!StringUtil.isNullOrEmpty(cloud_caoping_guige_cont)){
//                        lx_class.setText(cloud_caoping_guige_cont);
//                    }
//                }
//                break;
//                default:
//                    break;
//            }
//        }
        if(requestCode == 1000){
            // 根据上面发送过去的请求吗来区别
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
