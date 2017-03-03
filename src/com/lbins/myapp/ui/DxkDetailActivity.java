package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.DxkViewPageAdapter;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.DxkAdData;
import com.lbins.myapp.entity.DxkAd;
import com.lbins.myapp.util.StringUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 定向卡详情
 */
public class DxkDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private JSONArray jsonArray;
    private ViewPager viewPager;
    private DxkViewPageAdapter adapter;
    ArrayList<String> pics = new ArrayList<String>();
    private Button btn_one;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_dianpu_activity);
        getData();
    }

    ArrayList<DxkAd> picData = new ArrayList<DxkAd>();
    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appGetDxkAds,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            DxkAdData data = getGson().fromJson(s, DxkAdData.class);
                            if (data.getCode() == 200) {
                                picData.clear();
                                picData.addAll(data.getData());
                                if(picData != null){
                                    for(DxkAd loadPic: picData){
                                        pics.add(loadPic.getDxk_ad_pic());
                                    }
                                    initLoadData();
                                    initView();
                                }
                            }
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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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

    private void initLoadData() {
        jsonArray = new JSONArray();
        for (int i = 0; i < pics.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("resourceId", pics.get(i));
                jsonObject.put("title", pics.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
    }

    private void initView() {
        viewPager = (ViewPager) this.findViewById(R.id.viewpage);
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("定向卡介绍");
        adapter = new DxkViewPageAdapter(this, jsonArray);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int j = 0; j < pics.size(); j++) {
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        btn_one = (Button) this.findViewById(R.id.btn_one);
        btn_one.setText("立即加入");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    public void ruzhuAction(View view){
        if (StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) ||
                "0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) ) {
            showMsg(DxkDetailActivity.this, "请先登录！");
        } else {
            Intent intent = new Intent(DxkDetailActivity.this, DxkOrderActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
