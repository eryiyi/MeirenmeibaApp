package com.lbins.myapp.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
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
import com.lbins.myapp.adapter.*;
import com.lbins.myapp.base.BaseFragment;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.GoodsTypeData;
import com.lbins.myapp.data.PaihangObjData;
import com.lbins.myapp.entity.GoodsType;
import com.lbins.myapp.entity.PaihangObj;
import com.lbins.myapp.library.PullToRefreshBase;
import com.lbins.myapp.library.PullToRefreshListView;
import com.lbins.myapp.ui.DetailPaopaoGoodsActivity;
import com.lbins.myapp.ui.LocationCityActivity;
import com.lbins.myapp.ui.RegOneActivity;
import com.lbins.myapp.ui.SearchGoodsByTypeActivity;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.ClassifyGridview;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/7/1.
 * 推荐
 */
public class TuijianFragment extends BaseFragment implements View.OnClickListener ,OnClickContentItemListener {
    private View view;
    private Resources res;

    private TextView location;
    private ImageView btn_scan;
    private TextView keywords;

    private PullToRefreshListView lstv;
    private ItemIndexGoodsAdapter adapter;
    List<PaihangObj> listsgoods = new ArrayList<PaihangObj>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    private LinearLayout headLiner;
    //轮播广告
    private ViewPager viewpager;
    private TuijianAdViewPagerAdapter adapterAd;
    private LinearLayout viewGroup;
    private ImageView dot, dots[];
    private Runnable runnable;
    private int autoChangeTime = 5000;
    private List<String> listsAd = new ArrayList<String>();

    //分类
    private IndexTypeAdapter adaptertype;
    ClassifyGridview gridv_one;//商品分类

    //广告二
    private AdOneAdapter adapterAdTwo;
    ClassifyGridview gridv_two;
    private List<String> listsAdsTwo = new ArrayList<String>();
    //广告三
    private AdOneAdapter adapterAdThree;
    ClassifyGridview gridv_three;
    private List<String> listsAdsThree = new ArrayList<String>();

    //商品分类
    public static List<GoodsType> listGoodsType = new ArrayList<GoodsType>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tuijian_fragment, null);
        res = getActivity().getResources();
        initView();
        //商城分类
        initViewType();
        //广告一
        initViewAdOne();
        //广告二
        initViewAdTwo();
        //轮播广告
        initViewPager();

        //查询商品分类
        getGoodsType();

        //定位地址
        initLocation();
        //推荐首页商品查询
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

    //广告一
    private void initViewAdOne() {
        listsAdsTwo.add("");
        listsAdsTwo.add("");
        listsAdsTwo.add("");
        listsAdsTwo.add("");
        gridv_two = (ClassifyGridview) headLiner.findViewById(R.id.gridv_two);
        adapterAdTwo = new AdOneAdapter(listsAdsTwo,getActivity());
        gridv_two.setAdapter(adapterAdTwo);
        gridv_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        gridv_two.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }
    //广告二
    private void initViewAdTwo() {
        listsAdsThree.add("");
        listsAdsThree.add("");
        listsAdsThree.add("");
        listsAdsThree.add("");
        listsAdsThree.add("");
        listsAdsThree.add("");
        gridv_three = (ClassifyGridview) headLiner.findViewById(R.id.gridv_three);
        adapterAdThree = new AdOneAdapter(listsAdsThree,getActivity());
        gridv_three.setAdapter(adapterAdThree);
        gridv_three.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        gridv_three.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }
    //商城分类
    private void initViewType() {
        gridv_one = (ClassifyGridview) headLiner.findViewById(R.id.gridv_one);
        adaptertype = new IndexTypeAdapter(listGoodsType,getActivity());
        gridv_one.setAdapter(adaptertype);
        gridv_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listGoodsType.size()>(position)){
                    GoodsType goodsType = listGoodsType.get(position);
                    if(goodsType != null){
                        Intent intent = new Intent(getActivity(), SearchGoodsByTypeActivity.class);
                        intent.putExtra("typeId", goodsType.getTypeId());
                        intent.putExtra("typeName", goodsType.getTypeName());
                        startActivity(intent);
                    }
                }
            }
        });
        gridv_one.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initView() {
        location = (TextView) view.findViewById(R.id.location);
        location.setOnClickListener(this);
        btn_scan = (ImageView) view.findViewById(R.id.btn_scan);
        keywords = (TextView) view.findViewById(R.id.keywords);
        lstv = (PullToRefreshListView) view.findViewById(R.id.lstv);
        headLiner = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.tuijian_header, null);

        listsAd.add("");
        listsAd.add("");
        listsAd.add("");
        listsAd.add("");
        listsAd.add("");
        adapter = new ItemIndexGoodsAdapter(listsgoods, getActivity());

        final ListView listView = lstv.getRefreshableView();
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
                if (listsgoods.size() > position -2) {
                    PaihangObj paihangObj = listsgoods.get(position - 2);
                    if(paihangObj != null){
                        Intent intent  = new Intent(getActivity(), DetailPaopaoGoodsActivity.class);
                        intent.putExtra("emp_id_dianpu", paihangObj.getGoods_emp_id());
                        intent.putExtra("goods_id", paihangObj.getGoods_id());
                        startActivity(intent);
                    }
                }
            }
        });

    }


    //推荐首页商品查询
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
                                }else {
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

//                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("index", String.valueOf(pageIndex));
                params.put("size", "10");
                params.put("is_type", "0");
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.right_img:
                //登录
            {
                Intent intent = new Intent(getActivity(), RegOneActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.location:
            {
                //地址
                Intent intent = new Intent(getActivity(), LocationCityActivity.class);
                startActivity(intent);
            }
                break;
        }
    }

    private void initViewPager() {
        adapterAd = new TuijianAdViewPagerAdapter(getActivity());
        adapterAd.change(listsAd);
        adapterAd.setOnClickContentItemListener(this);
        viewpager = (ViewPager) headLiner.findViewById(R.id.viewpager);
        viewpager.setAdapter(adapterAd);
        viewpager.setOnPageChangeListener(myOnPageChangeListener);
        initDot();
        runnable = new Runnable() {
            @Override
            public void run() {
                int next = viewpager.getCurrentItem() + 1;
                if (next >= adapterAd.getCount()) {
                    next = 0;
                }
                viewHandler.sendEmptyMessage(next);
            }
        };
        viewHandler.postDelayed(runnable, autoChangeTime);
    }


    // 初始化dot视图
    private void initDot() {
        viewGroup = (LinearLayout) headLiner.findViewById(R.id.viewGroup);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                20, 20);
        layoutParams.setMargins(4, 3, 4, 3);

        dots = new ImageView[adapterAd.getCount()];
        for (int i = 0; i < adapterAd.getCount(); i++) {
            dot = new ImageView(getActivity());
            dot.setLayoutParams(layoutParams);
            dots[i] = dot;
            dots[i].setTag(i);
            dots[i].setOnClickListener(onClick);

            if (i == 0) {
                dots[i].setBackgroundResource(R.drawable.dotc);
            } else {
                dots[i].setBackgroundResource(R.drawable.dotn);
            }

            viewGroup.addView(dots[i]);
        }
    }

    ViewPager.OnPageChangeListener myOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            setCurDot(arg0);
            viewHandler.removeCallbacks(runnable);
            viewHandler.postDelayed(runnable, autoChangeTime);
        }

    };
    // 实现dot点击响应功能,通过点击事件更换页面
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            setCurView(position);
        }

    };

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position > adapterAd.getCount()) {
            return;
        }
        viewpager.setCurrentItem(position);
//        if (!StringUtil.isNullOrEmpty(lists.get(position).getNewsTitle())){
//            titleSlide = lists.get(position).getNewsTitle();
//            if(titleSlide.length() > 13){
//                titleSlide = titleSlide.substring(0,12);
//                article_title.setText(titleSlide);//当前新闻标题显示
//            }else{
//                article_title.setText(titleSlide);//当前新闻标题显示
//            }
//        }

    }

    /**
     * 选中当前引导小点
     */
    private void setCurDot(int position) {
        for (int i = 0; i < dots.length; i++) {
            if (position == i) {
                dots[i].setBackgroundResource(R.drawable.dotc);
            } else {
                dots[i].setBackgroundResource(R.drawable.dotn);
            }
        }
    }

    /**
     * 每隔固定时间切换广告栏图片
     */
    @SuppressLint("HandlerLeak")
    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setCurView(msg.what);
        }

    };

    @Override
    public void onClickContentItem(int position, int flag, Object object) {

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
                                    Toast.makeText(getActivity(), jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
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



}
