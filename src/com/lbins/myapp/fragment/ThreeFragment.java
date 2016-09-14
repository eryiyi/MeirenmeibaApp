package com.lbins.myapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemIndexGoodsAdapter;
import com.lbins.myapp.adapter.ItemTuijianDianpusAdapter;
import com.lbins.myapp.base.BaseFragment;
import com.lbins.myapp.library.PullToRefreshBase;
import com.lbins.myapp.library.PullToRefreshListView;
import com.lbins.myapp.ui.DianpuDetailActivity;
import com.lbins.myapp.ui.LocationCityActivity;
import com.lbins.myapp.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/7/1.
 */
public class ThreeFragment extends BaseFragment implements View.OnClickListener{
    private View view;
    private Resources res;

    private TextView location;
    private ImageView btn_scan;
    private TextView keywords;

    private PullToRefreshListView lstv;
    private ItemTuijianDianpusAdapter adapter;
    List<String> listsgoods = new ArrayList<String>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    private LinearLayout headLiner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.three_fragment, null);
        res = getActivity().getResources();
        initView();


        //定位地址
        initLocation();
        return view;
    }


    //定位地址
    void initLocation(){
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("location_city", ""), String.class))){
            //说明用户自己选择了城市
            location.setText(getGson().fromJson(getSp().getString("location_city", ""), String.class));
        }else {
            if(!StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.locationAreaName)){
                location.setText(MeirenmeibaAppApplication.locationAreaName);
            }else {
                location.setText("郑州");
            }
        }
    }

    private void initView() {
        location = (TextView) view.findViewById(R.id.location);
        location.setOnClickListener(this);
        btn_scan = (ImageView) view.findViewById(R.id.btn_scan);
        keywords = (TextView) view.findViewById(R.id.keywords);
        lstv = (PullToRefreshListView) view.findViewById(R.id.lstv);
        headLiner = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.three_header, null);
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        adapter = new ItemTuijianDianpusAdapter(listsgoods, getActivity());

        ListView listView = lstv.getRefreshableView();
        listView.addHeaderView(headLiner);
        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setAdapter(adapter);
        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
//                if ("1".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))) {
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
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
//                if (lists.size() > position -2) {
//                    lists.get(position - 2).setIs_read("1");
//                    adapter.notifyDataSetChanged();
//                    recordVO = lists.get(position - 2);
//                    DBHelper.getInstance(getActivity()).updateRecord(recordVO);
//                }
                Intent detailDianpuV = new Intent(getActivity(), DianpuDetailActivity.class);
                startActivity(detailDianpuV);
            }
        });

    }

    void initData() {
        lstv.onRefreshComplete();
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                InternetURL.GET_RECORD_LIST_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        if (StringUtil.isJson(s)) {
//                            try {
//                                JSONObject jo = new JSONObject(s);
//                                String code = jo.getString("code");
//                                if (Integer.parseInt(code) == 200) {
//                                    RecordData data = getGson().fromJson(s, RecordData.class);
//                                    if (IS_REFRESH) {
//                                        lists.clear();
//                                    }
//                                    lists.addAll(data.getData());
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
//                                    lstv.onRefreshComplete();
//                                    adapter.notifyDataSetChanged();
//                                } else if (Integer.parseInt(code) == 9) {
//                                    Toast.makeText(getActivity(), R.string.login_out, Toast.LENGTH_SHORT).show();
//                                    save("password", "");
//                                    Intent loginV = new Intent(getActivity(), LoginActivity.class);
//                                    startActivity(loginV);
//                                    getActivity().finish();
//                                } else {
//                                    Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            if (lists.size() == 0) {
//                                no_data.setVisibility(View.GONE);
//                                lstv.setVisibility(View.VISIBLE);
//                            } else {
//                                no_data.setVisibility(View.GONE);
//                                lstv.setVisibility(View.VISIBLE);
//                            }
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//
////                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("index", String.valueOf(pageIndex));
//                params.put("size", "10");
//                params.put("mm_msg_type", "0");
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
//        };
//        getRequestQueue().add(request);
    }

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("update_location_success")) {
                //定位地址
                initLocation();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("update_location_success");
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.location:
            {
                //地址
                Intent intent = new Intent(getActivity(), LocationCityActivity.class);
                startActivity(intent);
            }
            break;
        }
    }
}
