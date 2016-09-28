package com.lbins.myapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/9
 * Time: 14:51
 * 类的功能、说明写在此处.
 */
public class PublishGoodCommentActivity extends BaseActivity implements View.OnClickListener {
    private String record_uuid;
    private String emp_id;

    private String cont;
    /**
     * 输入框
     */
    public EditText et_sendmessage;
    private TextView title;
    private TextView right_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_comment_xml);
        emp_id = getIntent().getExtras().getString("emp_id");
        record_uuid = getIntent().getExtras().getString("goods_id");
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.VISIBLE);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        right_btn.setText("确定");
        right_btn.setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("添加评论");
        et_sendmessage = (EditText) this.findViewById(R.id.face_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
                //发布按钮
                cont = et_sendmessage.getText().toString();//评论内容
                if (StringUtil.isNullOrEmpty(cont)) {
                    Toast.makeText(this, "请输入评论内容！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cont.length() > 200){
                    Toast.makeText(this, "评论超出字段限制！最多200字", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = new CustomProgressDialog(PublishGoodCommentActivity.this, "正在加载中",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                publish_comment();
                break;

        }
    }

    //开始发布
    private void publish_comment() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.PUBLISH_GOODS_COMMNENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                Toast.makeText(PublishGoodCommentActivity.this, "添加评论成功！", Toast.LENGTH_SHORT).show();
                                //调用广播，刷新详细页评论
                                Intent intent1 = new Intent("add_goods_comment_success");
                                sendBroadcast(intent1);
                                finish();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(PublishGoodCommentActivity.this, "添加评论失败！", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("goodsId", record_uuid);
                params.put("empId", getGson().fromJson(getSp().getString("empId", ""), String.class));
                params.put("fplempid", "");//父评论人
                params.put("fplid", "");
                params.put("content", cont);
                if(!StringUtil.isNullOrEmpty(emp_id)){
                    params.put("goodsEmpId", emp_id);//商品所有者
                }else {
                    params.put("goodsEmpId", "");//商品所有者
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
