package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemIndexGoodsAdapter;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.PaihangObjData;
import com.lbins.myapp.entity.PaihangObj;
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
 * 首发新品 特惠专区
 */
public class SearchTuijianActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private String is_type;//0推荐首页 1首发新品 2特惠专区
    private PullToRefreshListView lstv;//列表
    private ItemIndexGoodsAdapter adapter;
    List<PaihangObj> listsgoods = new ArrayList<PaihangObj>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    private ImageView search_null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_tuijian_activity);
        is_type = getIntent().getExtras().getString("is_type");
        initView();

        progressDialog = new CustomProgressDialog(SearchTuijianActivity.this, "",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        initData();
    }

    private void initView() {
        search_null = (ImageView) this.findViewById(R.id.search_null);
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("更多");
        switch (Integer.parseInt(is_type)){
            case 0:
                title.setText("推荐");
                break;
            case 1:
                title.setText("首发新品");
                break;
            case 2:
                title.setText("特惠专区");
                break;
        }
        adapter = new ItemIndexGoodsAdapter(listsgoods, SearchTuijianActivity.this);
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
                    PaihangObj paihangObj = listsgoods.get((position-1));
                    if(paihangObj != null){
                        Intent intent  = new Intent(SearchTuijianActivity.this, DetailPaopaoGoodsActivity.class);
                        intent.putExtra("emp_id_dianpu", paihangObj.getGoods_emp_id());
                        intent.putExtra("goods_id", paihangObj.getGoods_id());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    void initData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_INDEX_TUIJIAN_LISTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    PaihangObjData data = getGson().fromJson(s, PaihangObjData.class);
                                    if (IS_REFRESH) {
                                        listsgoods.clear();
                                    }
                                    listsgoods.addAll(data.getData());
//                                    if (data != null && data.getData() != null) {
//                                        for (RecordMsg recordMsg : data.getData()) {
//                                            RecordMsg recordMsgLocal = DBHelper.getInstance(getActivity()).getRecord(recordMsg.getMm_msg_id());
//                                            if (recordMsgLocal != null) {
//                                                //已经存在了 不需要插入了
//                                            } else {
//                                                DBHelper.getInstance(getActivity()).saveRecord(recordMsg);
//                                            }
//
//                                        }
//                                    }
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
                                    Toast.makeText(SearchTuijianActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                params.put("is_type", is_type);
                params.put("is_del", "0");
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
