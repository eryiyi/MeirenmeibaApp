package com.lbins.myapp.ui;

import android.content.Intent;
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

    private TextView btn_all;
    private TextView btn_nearby;
    private TextView btn_paixu;
    private TextView btn_val;

    private String typeName = "";

    private TextView btn_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_activity);
        lx_class_id = getIntent().getExtras().getString("lx_class_id");
        typeName = getIntent().getExtras().getString("typeName");
        initView();

        progressDialog = new CustomProgressDialog(NearbyActivity.this, "",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        //获得周围店铺
        initData();
    }

    private void initView() {
        btn_scan = (TextView) this.findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(this);
        btn_all = (TextView) this.findViewById(R.id.btn_all);
        btn_nearby = (TextView) this.findViewById(R.id.btn_nearby);
        btn_paixu = (TextView) this.findViewById(R.id.btn_paixu);
        btn_val = (TextView) this.findViewById(R.id.btn_val);
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

        search_null = (ImageView) this.findViewById(R.id.search_null);
        this.findViewById(R.id.back).setOnClickListener(this);
        keywords = (EditText) this.findViewById(R.id.keywords);

        keywords.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER) {
                    //修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    NearbyActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    initData();

                }
                return false;
            }
        });

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
//                if(!StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.latStr)){
//                    params.put("lat_company", MeirenmeibaAppApplication.latStr);
//                }
//                if(!StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.lngStr)){
//                    params.put("lng_company", MeirenmeibaAppApplication.lngStr);
//                }
                params.put("page", String.valueOf(pageIndex));
                if(!StringUtil.isNullOrEmpty(keywords.getText().toString())){
                    params.put("cont", keywords.getText().toString());
                }
                if(!StringUtil.isNullOrEmpty(lx_class_id)){
                    params.put("lx_class_id", lx_class_id);
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
            case R.id.btn_all:
            {
                //全部点击
                btn_all.setTextColor(getResources().getColor(R.color.red));
                btn_paixu.setTextColor(getResources().getColor(R.color.text_color));
                btn_nearby.setTextColor(getResources().getColor(R.color.text_color));
                btn_val.setTextColor(getResources().getColor(R.color.text_color));
                tmpNearby = 0;
                Intent intent = new Intent(NearbyActivity.this, SearchMoreClassActivitySelect.class);
                startActivityForResult(intent, 1000);
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
                progressDialog = new CustomProgressDialog(NearbyActivity.this, "",R.anim.custom_dialog_frame);
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
                progressDialog = new CustomProgressDialog(NearbyActivity.this, "",R.anim.custom_dialog_frame);
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
                progressDialog = new CustomProgressDialog(NearbyActivity.this, "",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                initData();
            }
            break;
            case R.id.back:
                finish();
                break;
            case R.id.btn_scan:
            {
                keywords.setText("");
                tmpNearby = 0;
                lx_class_id = "";
                pageIndex = 1;
                IS_REFRESH = true;
                btn_all.setText("全部");
                progressDialog = new CustomProgressDialog(NearbyActivity.this, "",R.anim.custom_dialog_frame);
                progressDialog.setCancelable(true);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                initData();
            }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            switch (resultCode) {
                case 1001:
                {
                    lx_class_id = data.getStringExtra("cloud_caoping_guige_id");
                    String cloud_caoping_guige_cont = data.getStringExtra("cloud_caoping_guige_cont");
                    if(!StringUtil.isNullOrEmpty(cloud_caoping_guige_cont)){
                        btn_all.setText(cloud_caoping_guige_cont);
                    }
                    progressDialog = new CustomProgressDialog(NearbyActivity.this, "",R.anim.custom_dialog_frame);
                    progressDialog.setCancelable(true);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();
                    initData();
                }
                break;
            }
        }
    }

}
