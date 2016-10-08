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
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.entity.BankObj;
import com.lbins.myapp.entity.Count;
import com.lbins.myapp.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 积分提现
 */
public class TixianCountActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView money_count;
    private TextView ban_card;
    private EditText money_cztx;

    private Count count;
    BankObj bankObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.count_tixian_activity);
        count = (Count) getIntent().getExtras().get("count");
        initView();

        if(count != null){
            money_count.setText(count.getCount());
        }

    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        money_count = (TextView) this.findViewById(R.id.money_count);
        ban_card = (TextView) this.findViewById(R.id.ban_card);
        money_cztx = (EditText) this.findViewById(R.id.money_cztx);
        title.setText("积分提现");
        this.findViewById(R.id.liner_card).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner_card:
            {
                //选择银行卡
                Intent intent = new Intent(TixianCountActivity.this, BankCardsActivity.class);
                startActivityForResult(intent, 1000);
            }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == 10001 ){
            bankObj = (BankObj) data.getExtras().get("bankObj");
            ban_card.setText(bankObj.getBank_card());
        }
    }

    public void txAction(View view){
        //提现
        if(StringUtil.isNullOrEmpty(money_count.getText().toString())){
            showMsg(TixianCountActivity.this, "您的积分不能提现，必须100积分以上才可以提现！");
            return;
        }
        if(Double.valueOf(money_count.getText().toString())<100.0){
            showMsg(TixianCountActivity.this, "您的积分不能提现，必须100积分以上才可以提现！");
            return;
        }
        if(StringUtil.isNullOrEmpty(money_cztx.getText().toString())){
            showMsg(TixianCountActivity.this, "请输入提现金额！");
            return;
        }
        if(Double.valueOf(money_cztx.getText().toString())<100.0){
            showMsg(TixianCountActivity.this, "请输入提现金额！必须是100的整数倍！");
            return;
        }
        if((Double.valueOf(money_cztx.getText().toString()) > Double.valueOf(money_count.getText().toString()))){
            showMsg(TixianCountActivity.this, "您的积分不够，请重新输入！");
            return;
        }
        //计算整数
        if(Integer.parseInt(money_cztx.getText().toString())%100 != 0){
            showMsg(TixianCountActivity.this, "提现必须是100的整数倍！");
            return;
        }
        if(bankObj == null){
            showMsg(TixianCountActivity.this, "请选择银行卡！");
            return;
        }
        save();
    }

    private void save() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SAVE_BANK_APPLY_RETURN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    showMsg(TixianCountActivity.this , "提现申请成功，请等待管理员审核！");
                                    Intent intent1 = new Intent("bank_apply_success");
                                    sendBroadcast(intent1);
                                    finish();
                                }else {
                                    Toast.makeText(TixianCountActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(TixianCountActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(TixianCountActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", getGson().fromJson(getSp().getString("empId", ""), String.class));
                params.put("bank_id", bankObj.getBank_id());
                params.put("lx_bank_apply_count", money_cztx.getText().toString());
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
