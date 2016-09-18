package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemBankCardAdapter;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.BankObjData;
import com.lbins.myapp.entity.BankObj;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.CustomProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 充值提现 要选择银行卡
 */

public class BankCardsActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ListView lstv;
    private ItemBankCardAdapter adapter;
    List<BankObj> listsBank = new ArrayList<BankObj>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_card_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("选择银行卡");
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemBankCardAdapter(listsBank, BankCardsActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                setResult(10001, intent);
                finish();
            }
        });

        progressDialog = new CustomProgressDialog(BankCardsActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
    }

    //获得银行卡列表
    public void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.APP_GET_BANK_CARDS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            BankObjData data = getGson().fromJson(s, BankObjData.class);
                            if (data.getCode() == 200) {
                                listsBank.clear();
                                listsBank.addAll(data.getData());
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(BankCardsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(BankCardsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BankCardsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", getGson().fromJson(getSp().getString("empId", ""), String.class));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
