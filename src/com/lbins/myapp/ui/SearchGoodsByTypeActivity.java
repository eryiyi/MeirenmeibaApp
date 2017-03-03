package com.lbins.myapp.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.GoodsTypeAdapter;
import com.lbins.myapp.adapter.ItemGoodsAdapter;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.GoodsTypeData;
import com.lbins.myapp.data.PaopaoGoodsData;
import com.lbins.myapp.entity.GoodsType;
import com.lbins.myapp.entity.PaopaoGoods;
import com.lbins.myapp.fragment.TuijianFragment;
import com.lbins.myapp.library.PullToRefreshBase;
import com.lbins.myapp.library.PullToRefreshListView;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 按照商品类别查询商品列表
 */
public class SearchGoodsByTypeActivity extends BaseActivity implements View.OnClickListener {
    private String typeId;
    private String typeName;
    private PullToRefreshListView lstv;//列表
    private ItemGoodsAdapter adapter;
    List<PaopaoGoods> listsgoods = new ArrayList<PaopaoGoods>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;
    private ImageView search_null;
    private EditText keywords;

    private TextView btn_all;
    private TextView btn_nearby;
    private TextView btn_paixu;
    private TextView btn_val;

    private String keyContent;
    private TextView btn_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_goods_byid_activity);
        typeId = getIntent().getExtras().getString("typeId");
        typeName = getIntent().getExtras().getString("typeName");
        keyContent = getIntent().getExtras().getString("keyContent");
        initView();
        if(!StringUtil.isNullOrEmpty(keyContent)){
            keywords.setText(keyContent);
        }
        progressDialog = new CustomProgressDialog(SearchGoodsByTypeActivity.this, "",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        initData();
    }

    private void initView() {
        btn_all = (TextView) this.findViewById(R.id.btn_all);
        btn_nearby = (TextView) this.findViewById(R.id.btn_nearby);
        btn_paixu = (TextView) this.findViewById(R.id.btn_paixu);
        btn_val = (TextView) this.findViewById(R.id.btn_val);
        btn_scan = (TextView) this.findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(this);

        if(!StringUtil.isNullOrEmpty(typeName)){
            btn_all.setText(typeName);
        }
        btn_all.setTextColor(getResources().getColor(R.color.red));
        btn_paixu.setTextColor(getResources().getColor(R.color.text_color));
        btn_nearby.setTextColor(getResources().getColor(R.color.text_color));
        btn_val.setTextColor(getResources().getColor(R.color.text_color));


        btn_all.setOnClickListener(this);
        btn_nearby.setOnClickListener(this);
        btn_paixu.setOnClickListener(this);
        btn_val.setOnClickListener(this);

        keywords = (EditText) this.findViewById(R.id.keywords);
        keywords.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER) {
                    //修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    SearchGoodsByTypeActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    initData();

                }
                return false;
            }
        });

        search_null = (ImageView) this.findViewById(R.id.search_null);
        this.findViewById(R.id.back).setOnClickListener(this);

        adapter = new ItemGoodsAdapter(listsgoods, SearchGoodsByTypeActivity.this);
        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setAdapter(adapter);
        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                initData();
            }
        });

        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listsgoods.size() > (position-1)){
                    PaopaoGoods paihangObj = listsgoods.get((position-1));
                    if(paihangObj != null){
                        Intent intent  = new Intent(SearchGoodsByTypeActivity.this, DetailPaopaoGoodsActivity.class);
                        intent.putExtra("emp_id_dianpu", paihangObj.getEmpId());
                        intent.putExtra("goods_id", paihangObj.getId());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    void initData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_GOODS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    PaopaoGoodsData data = getGson().fromJson(s, PaopaoGoodsData.class);
                                    if (IS_REFRESH) {
                                        listsgoods.clear();
                                    }
                                    listsgoods.addAll(data.getData());
                                    lstv.onRefreshComplete();
                                    adapter.notifyDataSetChanged();
                                    if(listsgoods.size() == 0){
                                        search_null.setVisibility(View.VISIBLE);
                                        lstv.setVisibility(View.GONE);
                                    }else {
                                        search_null.setVisibility(View.GONE);
                                        lstv.setVisibility(View.VISIBLE);
                                    }
                                }else {
                                    Toast.makeText(SearchGoodsByTypeActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                params.put("index", String.valueOf(pageIndex));
                params.put("size", "10");
                if(!StringUtil.isNullOrEmpty(typeId)){
                    params.put("typeId", typeId);
                }
                if(!StringUtil.isNullOrEmpty(keywords.getText().toString())){
                    params.put("cont", keywords.getText().toString());
                }
                if(tmpNearby == 1){
                    if(!StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.latStr)){
                        params.put("lat_company", MeirenmeibaAppApplication.latStr);
                    }
                    if(!StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.lngStr)){
                        params.put("lng_company", MeirenmeibaAppApplication.lngStr);
                    }
                }

                if(tmpNearby == 2){
                    params.put("is_time", "1");
                }

                if(tmpNearby == 3){
                    params.put("is_count", "1");
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

    private int tmpNearby = 0;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_all:
            {
                //全部点击
                btn_all.setTextColor(getResources().getColor(R.color.red));
                btn_paixu.setTextColor(getResources().getColor(R.color.text_color));
                btn_nearby.setTextColor(getResources().getColor(R.color.text_color));
                btn_val.setTextColor(getResources().getColor(R.color.text_color));
                tmpNearby = 0;
                //查询该分类下的小分类
                if(!StringUtil.isNullOrEmpty(typeId)){
                    GetTypeList();
                }else {
                    showGoodsType();
                }

        }
                break;
            case R.id.btn_nearby:
            {
                //附近点击
                btn_all.setTextColor(getResources().getColor(R.color.text_color));
                btn_paixu.setTextColor(getResources().getColor(R.color.text_color));
                btn_nearby.setTextColor(getResources().getColor(R.color.red));
                btn_val.setTextColor(getResources().getColor(R.color.text_color));
                tmpNearby = 1;
                IS_REFRESH = true;
                pageIndex = 1;
                progressDialog = new CustomProgressDialog(SearchGoodsByTypeActivity.this, "",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                initData();
            }
            break;
            case R.id.btn_paixu:
            {
                //最新排序
                btn_all.setTextColor(getResources().getColor(R.color.text_color));
                btn_paixu.setTextColor(getResources().getColor(R.color.red));
                btn_nearby.setTextColor(getResources().getColor(R.color.text_color));
                btn_val.setTextColor(getResources().getColor(R.color.text_color));
                tmpNearby = 2;
                IS_REFRESH = true;
                pageIndex = 1;
                progressDialog = new CustomProgressDialog(SearchGoodsByTypeActivity.this, "",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                initData();
            }
            break;
            case R.id.btn_val:
            {
                //销量
                btn_all.setTextColor(getResources().getColor(R.color.text_color));
                btn_paixu.setTextColor(getResources().getColor(R.color.text_color));
                btn_nearby.setTextColor(getResources().getColor(R.color.text_color));
                btn_val.setTextColor(getResources().getColor(R.color.red));
                tmpNearby = 3;
                IS_REFRESH = true;
                pageIndex = 1;
                progressDialog = new CustomProgressDialog(SearchGoodsByTypeActivity.this, "",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                initData();
            }
            break;
            case R.id.btn_scan:
            {
                keywords.setText("");
                tmpNearby = 0;
                typeId = "";
                pageIndex = 1;
                IS_REFRESH = true;
                btn_all.setText("全部");
                progressDialog = new CustomProgressDialog(SearchGoodsByTypeActivity.this, "",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                initData();
            }
            break;
        }
    }
    private void showGoodsType() {
        final Dialog picAddDialog = new Dialog(SearchGoodsByTypeActivity.this, R.style.spinner_Dialog);
        View picAddInflate = View.inflate(this, R.layout.select_type_dialog, null);
        ListView listView = (ListView) picAddInflate.findViewById(R.id.lstv);
        TextView title_msg = (TextView) picAddInflate.findViewById(R.id.title_msg);
        title_msg.setText("请选择分类");
        GoodsTypeAdapter adapter = new GoodsTypeAdapter(TuijianFragment.listGoodsType, SearchGoodsByTypeActivity.this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GoodsType goodsType = TuijianFragment.listGoodsType.get(i);
                btn_all.setText(goodsType.getTypeName());
                typeId = goodsType.getTypeId();
                IS_REFRESH = true;
                pageIndex = 1;
                progressDialog = new CustomProgressDialog(SearchGoodsByTypeActivity.this, "",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                initData();
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    private List<GoodsType> listGoodsType = new ArrayList<GoodsType>();
    private void GetTypeList() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_GOODS_SMALL_TYPE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    Gson gson = getGson();
                                    listGoodsType.clear();
                                    if(gson != null){
                                        GoodsTypeData data = gson.fromJson(s, GoodsTypeData.class);
                                        listGoodsType.addAll(data.getData());
                                        if(listGoodsType != null && listGoodsType.size() > 0){
                                            showGoodsType2();
                                        }else {
                                            tmpNearby = 0;
                                            typeId = "";
                                            pageIndex = 1;
                                            IS_REFRESH = true;
                                            btn_all.setText("全部");
                                            progressDialog = new CustomProgressDialog(SearchGoodsByTypeActivity.this, "",R.anim.custom_dialog_frame);
                                            progressDialog.setCancelable(true);
                                            progressDialog.setIndeterminate(true);
                                            progressDialog.show();
                                            initData();
                                        }
                                    }
                                }else {
                                    Toast.makeText(SearchGoodsByTypeActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SearchGoodsByTypeActivity.this, getResources().getString(R.string.get_data_error), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("is_hot", "");
                params.put("type_isuse", "0");
                params.put("type_id", typeId);
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

    private void showGoodsType2() {
        final Dialog picAddDialog = new Dialog(SearchGoodsByTypeActivity.this, R.style.spinner_Dialog);
        View picAddInflate = View.inflate(this, R.layout.select_type_dialog, null);
        ListView listView = (ListView) picAddInflate.findViewById(R.id.lstv);
        TextView title_msg = (TextView) picAddInflate.findViewById(R.id.title_msg);
        title_msg.setText("请选择分类");
        GoodsTypeAdapter adapter = new GoodsTypeAdapter(listGoodsType, SearchGoodsByTypeActivity.this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GoodsType goodsType = listGoodsType.get(i);
                btn_all.setText(goodsType.getTypeName());
                typeId = goodsType.getTypeId();
                IS_REFRESH = true;
                pageIndex = 1;
                progressDialog = new CustomProgressDialog(SearchGoodsByTypeActivity.this, "",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                initData();
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }


    @Override
    public void onStop() {
        releaseImageViews();
        super.onStop();
    }

    private void releaseImageViews() {
        releaseImageView(search_null);
    }

    private void releaseImageView(ImageView imageView) {
        Drawable d = imageView.getDrawable();
        if (d != null)
            d.setCallback(null);
        imageView.setImageDrawable(null);
        imageView.setBackgroundDrawable(null);
    }
}
