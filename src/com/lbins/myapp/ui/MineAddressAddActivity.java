package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.SuccessData;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.CustomProgressDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/9.
 * 新增我的收货地址
 */
public class MineAddressAddActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;

    private EditText add_nickname;
    private TextView add_address_one;
    private EditText add_tel;
    private EditText add_address_two;
    private Button button_add_address;

    private String cityid;
    private String cityName;
    private String provinceId;
    private String provinceName;
    private String areaId;
    private String areaName;
    private CheckBox checkbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_address_add_four_activity);
        cityid = getIntent().getExtras().getString("cityid");
        cityName = getIntent().getExtras().getString("cityName");
        provinceId = getIntent().getExtras().getString("provinceId");
        provinceName = getIntent().getExtras().getString("provinceName");
        areaId = getIntent().getExtras().getString("areaId");
        areaName = getIntent().getExtras().getString("areaName");
        initView();
    }
    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("新增收获地址");

        add_nickname =  (EditText) this.findViewById(R.id.add_nickname);
        add_tel =  (EditText) this.findViewById(R.id.add_tel);
        add_address_one =  (TextView) this.findViewById(R.id.add_address_one);
        add_address_two =  (EditText) this.findViewById(R.id.add_address_two);
        button_add_address =  (Button) this.findViewById(R.id.button_add_address);
        checkbox =  (CheckBox) this.findViewById(R.id.checkbox);

        button_add_address.setOnClickListener(this);
        add_address_one.setText(provinceName+ cityName+ areaName);
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("empName", ""), String.class))){
            add_nickname.setText(getGson().fromJson(getSp().getString("empName", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("empMobile", ""), String.class))){
            add_tel.setText(getGson().fromJson(getSp().getString("empMobile", ""), String.class));
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.button_add_address:
                //添加
                if (StringUtil.isNullOrEmpty(add_nickname.getText().toString())) {
                    Toast.makeText(MineAddressAddActivity.this, R.string.add_address_error_one, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtil.isNullOrEmpty(add_tel.getText().toString())) {
                    Toast.makeText(MineAddressAddActivity.this, R.string.add_address_error_two, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (StringUtil.isNullOrEmpty(add_address_two.getText().toString())) {
                    Toast.makeText(MineAddressAddActivity.this, R.string.add_address_error_three, Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = new CustomProgressDialog(MineAddressAddActivity.this, "",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                saveAddress();
                break;
        }
    }

    //保存收货地址
    public void saveAddress(){
        //获得收货地址列表
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.ADD_MINE_ADDRSS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                //成功
                                Intent intent = new Intent("add_address_success");
                                sendBroadcast(intent);
                                finish();
                            } else {
                                Toast.makeText(MineAddressAddActivity.this, R.string.address_error_one, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineAddressAddActivity.this, R.string.address_error_one, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MineAddressAddActivity.this, R.string.address_error_one, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accept_name", add_nickname.getText().toString());
                params.put("address", add_address_two.getText().toString());
                params.put("phone", add_tel.getText().toString());
                params.put("emp_id", getGson().fromJson(getSp().getString("empId", ""), String.class));
                params.put("province", provinceId);
                params.put("city", cityid);
                params.put("area", areaId);
                if(checkbox.isChecked()){
                    params.put("is_default", "1");
                }else {
                    params.put("is_default", "0");
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
