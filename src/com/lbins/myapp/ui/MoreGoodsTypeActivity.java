package com.lbins.myapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.IndexTypeAdapter;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.GoodsTypeData;
import com.lbins.myapp.entity.GoodsType;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.ClassifyGridview;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 更多分类
 */
public class MoreGoodsTypeActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    //分类
    private IndexTypeAdapter adaptertype;
    ClassifyGridview gridv_one;//商品分类

    //商品分类
    public static List<GoodsType> listGoodsType = new ArrayList<GoodsType>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_goods_type_activity);
        initView();
        //查询商品分类
        getGoodsType();
    }

    private void getGoodsType() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_GOODS_TYPE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                int code1 = jo.getInt("code");
                                if (code1 == 200) {
                                    GoodsTypeData data = getGson().fromJson(s, GoodsTypeData.class);
                                    listGoodsType.clear();
                                    listGoodsType.addAll(data.getData());
                                    adaptertype.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(MoreGoodsTypeActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(MoreGoodsTypeActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MoreGoodsTypeActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("商品分类");

        gridv_one = (ClassifyGridview) this.findViewById(R.id.gridv_one);
        adaptertype = new IndexTypeAdapter(listGoodsType, MoreGoodsTypeActivity.this);
        gridv_one.setAdapter(adaptertype);
        gridv_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listGoodsType.size()>(position)){
                    GoodsType goodsType = listGoodsType.get(position);
                    if(goodsType != null){
                        if("0".equals(goodsType.getTypeId())){
                            //更多
                            Intent intent = new Intent(MoreGoodsTypeActivity.this, MoreGoodsTypeActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(MoreGoodsTypeActivity.this, SearchGoodsByTypeActivity.class);
                            intent.putExtra("typeId", goodsType.getTypeId());
                            intent.putExtra("typeName", goodsType.getTypeName());
                            startActivity(intent);
                        }
                    }
                }
            }
        });
        gridv_one.setSelector(new ColorDrawable(Color.TRANSPARENT));
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
