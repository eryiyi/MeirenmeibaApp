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
import com.lbins.myapp.entity.GoodsType;
import com.lbins.myapp.ui.LocationCityActivity;
import com.lbins.myapp.ui.RegOneActivity;
import com.lbins.myapp.ui.SearchTuijianActivity;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.ClassifyGridview;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/7/1.
 * 商城
 */
public class ShangchengFragment extends BaseFragment implements View.OnClickListener ,OnClickContentItemListener {
    private View view;
    private Resources res;

    private TextView location;
    private ImageView btn_scan;
    private TextView keywords;

    private ClassifyGridview lstv;
    private ItemIndexGoodsGridviewAdapter adapter;
    List<String> listsgoods = new ArrayList<String>();
//    private int pageIndex = 1;
//    private static boolean IS_REFRESH = true;

    //轮播广告
    private ViewPager viewpager;
    private ShangchengAdViewPagerAdapter adapterAd;
    private LinearLayout viewGroup;
    private ImageView dot, dots[];
    private Runnable runnable;
    private int autoChangeTime = 5000;
    private List<String> listsAd = new ArrayList<String>();

    //分类
    private IndexTypeAdapter adaptertype;
    ClassifyGridview gridv_one;//商品分类

    //商品分类
    public static List<GoodsType> listGoodsType = new ArrayList<GoodsType>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.shangcheng_fragment, null);
        res = getActivity().getResources();
        initView();
        //商城分类
        initViewType();

        //轮播广告
        initViewPager();

        //查询商品分类
        getGoodsType();

        //定位城市
        initLocation();
        return view;
    }

    //商城分类
    private void initViewType() {

        gridv_one = (ClassifyGridview) view.findViewById(R.id.gridv_one);
        adaptertype = new IndexTypeAdapter(listGoodsType, getActivity());
        gridv_one.setAdapter(adaptertype);
        gridv_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        gridv_one.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initView() {
        location = (TextView) view.findViewById(R.id.location);
        location.setOnClickListener(this);
        btn_scan = (ImageView) view.findViewById(R.id.btn_scan);
        keywords = (TextView) view.findViewById(R.id.keywords);
        lstv = (ClassifyGridview) view.findViewById(R.id.lstv);
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        listsAd.add("");
        listsAd.add("");
        listsAd.add("");
        listsAd.add("");
        listsAd.add("");
        listsAd.add("");
        listsAd.add("");
        adapter = new ItemIndexGoodsGridviewAdapter(listsgoods, getActivity());
        lstv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lstv.setAdapter(adapter);

        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (lists.size() > position -2) {
//                    lists.get(position - 2).setIs_read("1");
//                    adapter.notifyDataSetChanged();
//                    recordVO = lists.get(position - 2);
//                    DBHelper.getInstance(getActivity()).updateRecord(recordVO);
//                }
            }
        });

        view.findViewById(R.id.btn_first_more).setOnClickListener(this);
        view.findViewById(R.id.btn_tehui_more).setOnClickListener(this);
    }


    void initData() {
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
            case R.id.btn_first_more:
            {
                //首发更多
                Intent intent = new Intent(getActivity(), SearchTuijianActivity.class);
                intent.putExtra("is_type", "1");
                startActivity(intent);
            }
                break;
            case R.id.btn_tehui_more:
            {
                //特惠更多
                Intent intent = new Intent(getActivity(), SearchTuijianActivity.class);
                intent.putExtra("is_type", "2");
                startActivity(intent);
            }
            break;
        }
    }


    private void initViewPager() {
        adapterAd = new ShangchengAdViewPagerAdapter(getActivity());
        adapterAd.change(listsAd);
        adapterAd.setOnClickContentItemListener(this);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
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
        viewGroup = (LinearLayout) view.findViewById(R.id.viewGroup);

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


}
