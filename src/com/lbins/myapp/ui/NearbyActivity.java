package com.lbins.myapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemTuijianDianpusAdapter;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.ManagerInfoData;
import com.lbins.myapp.entity.ManagerInfo;
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
 * 附近商家
 */
public class NearbyActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView lstv;
    private ItemTuijianDianpusAdapter adapter;
    List<ManagerInfo> listsDianpu = new ArrayList<ManagerInfo>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;
    private EditText keywords;
    private ImageView search_null;
    private String lx_class_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_activity);
        lx_class_id = getIntent().getExtras().getString("lx_class_id");
        initView();

        progressDialog = new CustomProgressDialog(NearbyActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        //获得周围店铺
        initData();
    }

    private void initView() {
        search_null = (ImageView) this.findViewById(R.id.search_null);
        this.findViewById(R.id.back).setOnClickListener(this);
        keywords = (EditText) this.findViewById(R.id.keywords);
        keywords.addTextChangedListener(watcher);
        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);

        adapter = new ItemTuijianDianpusAdapter(listsDianpu, NearbyActivity.this);

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
//                if ("1".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))) {
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
                if(listsDianpu != null && listsDianpu.size()>(position-1)){
                    ManagerInfo managerInfo = listsDianpu.get(position-1);
                    if(managerInfo != null){
                        Intent detailDianpuV = new Intent(NearbyActivity.this, DianpuDetailActivity.class);
                        detailDianpuV.putExtra("emp_id_dianpu", managerInfo.getEmp_id());//店铺用户id
                        startActivity(detailDianpuV);
                    }
                }
            }
        });

    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            initData();
        }
    };

    void initData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_DIANPU_LISTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    ManagerInfoData data = getGson().fromJson(s, ManagerInfoData.class);
                                    if (IS_REFRESH) {
                                        listsDianpu.clear();
                                    }
                                    listsDianpu.addAll(data.getData());
                                    lstv.onRefreshComplete();
                                    adapter.notifyDataSetChanged();
                                    if(listsDianpu.size() == 0){
                                        search_null.setVisibility(View.VISIBLE);
                                        lstv.setVisibility(View.GONE);
                                    }else {
                                        search_null.setVisibility(View.GONE);
                                        lstv.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Toast.makeText(NearbyActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(NearbyActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if(!StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.latStr)){
                    params.put("lat_company", MeirenmeibaAppApplication.latStr);
                }
                if(!StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.lngStr)){
                    params.put("lng_company", MeirenmeibaAppApplication.lngStr);
                }
                params.put("page", String.valueOf(pageIndex));
                if(!StringUtil.isNullOrEmpty(keywords.getText().toString())){
                    params.put("cont", keywords.getText().toString());
                }
                if(!StringUtil.isNullOrEmpty(lx_class_id)){
                    params.put("lx_class_id", lx_class_id);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    public void scanAction(View view){

        Intent intent = new Intent(NearbyActivity.this, SelectMoreClassTypeActivity.class);
        startActivityForResult(intent, 1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            // 根据上面发送过去的请求吗来区别
            switch (resultCode) {
                case 1001:
                {
                    lx_class_id = data.getStringExtra("cloud_caoping_guige_id");
                    String cloud_caoping_guige_cont = data.getStringExtra("cloud_caoping_guige_cont");
                    if(!StringUtil.isNullOrEmpty(cloud_caoping_guige_cont)){
//                        guige.setText(cloud_caoping_guige_cont);
                    }
                    initData();
                }
                break;
            }
        }
    }

}
