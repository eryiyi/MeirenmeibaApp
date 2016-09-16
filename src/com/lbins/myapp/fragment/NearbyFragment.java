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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemTuijianDianpusAdapter;
import com.lbins.myapp.base.BaseFragment;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.ManagerInfoData;
import com.lbins.myapp.entity.ManagerInfo;
import com.lbins.myapp.library.PullToRefreshBase;
import com.lbins.myapp.library.PullToRefreshListView;
import com.lbins.myapp.ui.DianpuDetailActivity;
import com.lbins.myapp.ui.LocationCityActivity;
import com.lbins.myapp.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/7/1.
 */
public class NearbyFragment extends BaseFragment implements View.OnClickListener{
    private View view;
    private Resources res;

    private TextView location;
    private ImageView btn_scan;
    private TextView keywords;

    private PullToRefreshListView lstv;
    private ItemTuijianDianpusAdapter adapter;
    List<ManagerInfo> listsDianpu = new ArrayList<ManagerInfo>();
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
        //获得周围店铺
        initData();
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

        adapter = new ItemTuijianDianpusAdapter(listsDianpu, getActivity());

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
                                    listsDianpu.addAll(data.getData());
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
                                } else {
                                    Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
