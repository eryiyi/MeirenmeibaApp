package com.lbins.myapp.ui;

import android.content.Intent;
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
import com.lbins.myapp.data.SuccessData;
import com.lbins.myapp.entity.Order;
import com.lbins.myapp.entity.OrderVo;
import com.lbins.myapp.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 退款退货
 */
public class TuihuoActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private OrderVo orderVoTmp;
    private EditText content;//退货原因
    private EditText danhao;//退货单号
    private LinearLayout liner_danhao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuihuo_activity);
        orderVoTmp = (OrderVo) getIntent().getExtras().get("orderVoTmp");
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("退款退货");
        content = (EditText) this.findViewById(R.id.content);
        danhao = (EditText) this.findViewById(R.id.danhao);
        liner_danhao = (LinearLayout) this.findViewById(R.id.liner_danhao);

        this.findViewById(R.id.button_delete_address).setOnClickListener(this);
        this.findViewById(R.id.button_add_address).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.button_add_address:
            {
                //
                if(StringUtil.isNullOrEmpty(content.getText().toString())){
                    showMsg(TuihuoActivity.this, "请输入退款原因！");
                    return;
                }
                tuiOrder();
            }
                break;
            case R.id.button_delete_address:
            {
                finish();
            }
            break;
        }
    }

    //退货
    public void tuiOrder() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_ORDER_RETURN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                Intent intent1 = new Intent("update_order_success");
                                intent1.putExtra("order_no", orderVoTmp.getOrder_no());
                                intent1.putExtra("status", "7");
                                sendBroadcast(intent1);
                                finish();
                                Toast.makeText(TuihuoActivity.this, "退货申请成功，请等待卖家处理！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TuihuoActivity.this, "退货申请失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(TuihuoActivity.this, "退货申请失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(TuihuoActivity.this, "退货申请失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_no", orderVoTmp.getOrder_no());
                params.put("status", "7");//退货
                if(!StringUtil.isNullOrEmpty(content.getText().toString())){
                    params.put("returnMsg", content.getText().toString());//退货原因
                }
               if(StringUtil.isNullOrEmpty(danhao.getText().toString())){
                   params.put("returnOrder", danhao.getText().toString());//退货单号
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
