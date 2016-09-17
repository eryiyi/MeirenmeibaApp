package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemFavourAdapter;
import com.lbins.myapp.adapter.ItemFavourDianpuAdapter;
import com.lbins.myapp.adapter.OnClickContentItemListener;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.DianPuFavourData;
import com.lbins.myapp.data.GoodsFavourVOData;
import com.lbins.myapp.data.SuccessData;
import com.lbins.myapp.entity.DianPuFavour;
import com.lbins.myapp.entity.GoodsFavourVO;
import com.lbins.myapp.library.PullToRefreshBase;
import com.lbins.myapp.library.PullToRefreshListView;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.DeletePopWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/11.
 * 我的收藏
 */
public class MineFavoursActivity extends BaseActivity implements View.OnClickListener, OnClickContentItemListener {
    private ImageView search_null1;

    private PullToRefreshListView classtype_lstv1;//列表
    private ItemFavourAdapter adapter1;
    private List<GoodsFavourVO> goods1;
    private int pageIndex1 = 1;
    private static boolean IS_REFRESH1 = true;

    private int tmpSelected1;//暂时存UUID  删除用

    private DeletePopWindow deleteWindow1;
    private TextView title;

    //viewPager
    private ViewPager vPager;
    private List<View> views;
    private View view1, view2;
    private int currentSelect = 0;//当前选中的viewpage

    private TextView btn_one;
    private TextView btn_two;


    private PullToRefreshListView classtype_lstv2;//列表
    private ItemFavourDianpuAdapter adapter2;
    private List<DianPuFavour> goods2= new ArrayList<DianPuFavour>();
    private int pageIndex2 = 1;
    private static boolean IS_REFRESH2 = true;
    private ImageView search_null2;
    private int tmpSelected2;//暂时存UUID  删除用

    private DeletePopWindow deleteWindow2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_favours_xml);
        initView();
        initData1();
        initData2();
    }

    private void initView() {
        title = (TextView) this.findViewById(R.id.title);
        title.setText("我的收藏");
        this.findViewById(R.id.back).setOnClickListener(this);
        btn_one= (TextView) this.findViewById(R.id.btn_one);
        btn_two= (TextView) this.findViewById(R.id.btn_two);

        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);

        //viewPage
        vPager = (ViewPager) this.findViewById(R.id.vPager);
        views = new ArrayList<View>();
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.mine_favour_one, null);
        view2 = inflater.inflate(R.layout.mine_favour_one, null);
        views.add(view1);
        views.add(view2);

        vPager.setAdapter(new MyViewPagerAdapter(views));
        currentSelect = 0;
        vPager.setCurrentItem(currentSelect);
        vPager.setOnPageChangeListener(new MyOnPageChangeListener());

        //第一部分
        goods1 = new ArrayList<GoodsFavourVO>();
        search_null1 = (ImageView) view1.findViewById(R.id.search_null);

        classtype_lstv1 = (PullToRefreshListView) view1.findViewById(R.id.minegoods_lstv);
        adapter1 = new ItemFavourAdapter(goods1, MineFavoursActivity.this);

        classtype_lstv1.setMode(PullToRefreshBase.Mode.BOTH);
        classtype_lstv1.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MineFavoursActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH1 = true;
                pageIndex1 = 1;
                initData1();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MineFavoursActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH1 = false;
                pageIndex1++;
                initData1();
            }
        });
        classtype_lstv1.setAdapter(adapter1);
        adapter1.setOnClickContentItemListener(this);
        classtype_lstv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsFavourVO record = goods1.get(position - 1);
                Intent intent  = new Intent(MineFavoursActivity.this, DetailPaopaoGoodsActivity.class);
                intent.putExtra("emp_id_dianpu", record.getEmp_id_goods());
                intent.putExtra("goods_id", record.getGoods_id());
                startActivity(intent);
            }
        });
        //第二部分
        search_null2 = (ImageView) view2.findViewById(R.id.search_null);
        classtype_lstv2 = (PullToRefreshListView) view2.findViewById(R.id.minegoods_lstv);
        adapter2 = new ItemFavourDianpuAdapter(goods2, MineFavoursActivity.this);

        classtype_lstv2.setMode(PullToRefreshBase.Mode.BOTH);
        classtype_lstv2.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MineFavoursActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH2 = true;
                pageIndex2 = 1;
                initData2();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MineFavoursActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH2 = false;
                pageIndex2++;
                initData2();
            }
        });
        classtype_lstv2.setAdapter(adapter2);
        adapter2.setOnClickContentItemListener(this);
        classtype_lstv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DianPuFavour record = goods2.get(position - 1);
                Intent deitalV = new Intent(MineFavoursActivity.this, DianpuDetailActivity.class);
                deitalV.putExtra("emp_id_dianpu", record.getEmp_id());
                startActivity(deitalV);
            }
        });
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public MyViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
//        int one = offset * 1 + bmpW;
//        int two = one * 1;

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int arg0) {
//            Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);
//            currIndex = arg0;
//            animation.setFillAfter(true);
//            animation.setDuration(300);
//            imageView.startAnimation(animation);
            if (arg0 == 0) {
                btn_one.setTextColor(getResources().getColor(R.color.red));
                btn_two.setTextColor(getResources().getColor(R.color.text_color));
                currentSelect = 0;
            }
            if (arg0 == 1) {
                btn_one.setTextColor(getResources().getColor(R.color.text_color));
                btn_two.setTextColor(getResources().getColor(R.color.red));
                currentSelect = 1;
            }
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_one:
            {
                //商品
                btn_one.setTextColor(getResources().getColor(R.color.red));
                btn_two.setTextColor(getResources().getColor(R.color.text_color));
                currentSelect = 0;
                vPager.setCurrentItem(currentSelect);
            }
            break;
            case R.id.btn_two:
            {
                //店铺
                btn_one.setTextColor(getResources().getColor(R.color.text_color));
                btn_two.setTextColor(getResources().getColor(R.color.red));
                currentSelect = 1;
                vPager.setCurrentItem(currentSelect);
            }
            break;
        }
    }

    //收藏商品列表
    private void initData1() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.MINE_FAVOUR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            GoodsFavourVOData data = getGson().fromJson(s, GoodsFavourVOData.class);
                            if (data.getCode() == 200) {
                                if (IS_REFRESH1) {
                                    goods1.clear();
                                }
                                goods1.addAll(data.getData());
                                classtype_lstv1.onRefreshComplete();
                                if (goods1.size() == 0) {
                                    search_null1.setVisibility(View.VISIBLE);
                                } else {
                                    search_null1.setVisibility(View.GONE);
                                }
                                adapter1.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MineFavoursActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineFavoursActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MineFavoursActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("page", String.valueOf(pageIndex1));
                params.put("empId", getGson().fromJson(getSp().getString("empId", ""), String.class));
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



    // 选择是否删除
    private void showSelectImageDialog() {
        deleteWindow1 = new DeletePopWindow(MineFavoursActivity.this, itemsOnClick);
        //显示窗口
        deleteWindow1.showAtLocation(MineFavoursActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //删除商品方法
    private void delete() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                 InternetURL.DELETE_FAVOUR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                Toast.makeText(MineFavoursActivity.this, R.string.delete_success, Toast.LENGTH_SHORT).show();
                                goods1.remove(tmpSelected1);
                                adapter1.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MineFavoursActivity.this, R.string.delete_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineFavoursActivity.this, R.string.delete_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MineFavoursActivity.this, R.string.delete_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("favourId", good.getFavour_id());
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

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            deleteWindow1.dismiss();
            switch (v.getId()) {
                case R.id.btn_sure:
                    delete();
                    break;
                default:
                    break;
            }
        }

    };

    //收藏商品列表
    private void initData2() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.APP_GET_FAVOUR_DIANPU_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            DianPuFavourData data = getGson().fromJson(s, DianPuFavourData.class);
                            if (data.getCode() == 200) {
                                if (IS_REFRESH2) {
                                    goods2.clear();
                                }
                                goods2.addAll(data.getData());
                                classtype_lstv2.onRefreshComplete();
                                if (goods2.size() == 0) {
                                    search_null2.setVisibility(View.VISIBLE);
                                } else {
                                    search_null2.setVisibility(View.GONE);
                                }
                                adapter2.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MineFavoursActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineFavoursActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MineFavoursActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id_favour", getGson().fromJson(getSp().getString("empId", ""), String.class));
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

    GoodsFavourVO good;
    DianPuFavour good2;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag) {
            case 1:
                if(currentSelect == 0){
                    good = goods1.get(position);
                    //删除
                    tmpSelected1 = position;
                    showSelectImageDialog();
                }else {
                    good2 = goods2.get(position);
                    //删除
                    tmpSelected2 = position;
                    showSelectImageDialog2();
                }
                break;
        }
    }

    // 选择是否删除
    private void showSelectImageDialog2() {
        deleteWindow2 = new DeletePopWindow(MineFavoursActivity.this, itemsOnClick2);
        //显示窗口
        deleteWindow2.showAtLocation(MineFavoursActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void deleteDp() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.DELETE_DIANPU_FAVOUR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                Toast.makeText(MineFavoursActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                                goods2.remove(tmpSelected2);
                                adapter2.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MineFavoursActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineFavoursActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MineFavoursActivity.this,"取消收藏失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("favour_no", good2.getFavour_no());
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

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick2 = new View.OnClickListener() {

        public void onClick(View v) {
            deleteWindow2.dismiss();
            switch (v.getId()) {
                case R.id.btn_sure:
                    deleteDp();
                    break;
                default:
                    break;
            }
        }

    };

}
