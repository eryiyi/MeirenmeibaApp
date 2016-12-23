package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
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
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemCommentAdapter;
import com.lbins.myapp.adapter.ItemDianpuCommentAdapter;
import com.lbins.myapp.adapter.ItemFavourDianpuAdapter;
import com.lbins.myapp.adapter.OnClickContentItemListener;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.DianpuCommentData;
import com.lbins.myapp.data.GoodsCommentData;
import com.lbins.myapp.entity.DianPuFavour;
import com.lbins.myapp.entity.DianpuComment;
import com.lbins.myapp.entity.GoodsComment;
import com.lbins.myapp.library.PullToRefreshBase;
import com.lbins.myapp.library.PullToRefreshListView;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.CustomProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class MineCommentActivity extends BaseActivity implements View.OnClickListener ,OnClickContentItemListener{
    private PullToRefreshListView lstv;
    private ItemCommentAdapter adapter;
    List<GoodsComment> lists = new ArrayList<GoodsComment>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;
    private ImageView search_null;

    //viewPager
    private ViewPager vPager;
    private List<View> views;
    private View view1, view2;
    private int currentSelect = 0;//当前选中的viewpage

    private TextView btn_one;
    private TextView btn_two;


    private PullToRefreshListView classtype_lstv2;//列表
    private ItemDianpuCommentAdapter adapter2;
    private List<DianpuComment> goods2= new ArrayList<DianpuComment>();
    private int pageIndex2 = 1;
    private static boolean IS_REFRESH2 = true;
    private ImageView search_null2;

    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_comment_activity);
        initView();
        progressDialog = new CustomProgressDialog(MineCommentActivity.this, "",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();
        initData2();
    }

    private void initView() {
        title = (TextView) this.findViewById(R.id.title);
        title.setText("我的点评");
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

        search_null = (ImageView) view1.findViewById(R.id.search_null);
        this.findViewById(R.id.back).setOnClickListener(this);
        lstv = (PullToRefreshListView) view1.findViewById(R.id.minegoods_lstv);
        adapter = new ItemCommentAdapter(lists, MineCommentActivity.this);
        adapter.setOnClickContentItemListener(this);
        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MineCommentActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MineCommentActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                getData();
            }
        });
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lists.size()> (position-1)){
                    GoodsComment comment = lists.get(position-1);
                    if(comment != null){
                        Intent intent = new Intent(MineCommentActivity.this, DetailPaopaoGoodsActivity.class);
                        intent.putExtra("emp_id_dianpu", (comment.getGoodsEmpId()==null?"":comment.getGoodsEmpId()));
                        intent.putExtra("goods_id", comment.getGoodsId());
                        startActivity(intent);
                    }
                }
            }
        });



        //第二部分
        search_null2 = (ImageView) view2.findViewById(R.id.search_null);
        classtype_lstv2 = (PullToRefreshListView) view2.findViewById(R.id.minegoods_lstv);
        adapter2 = new ItemDianpuCommentAdapter(goods2, MineCommentActivity.this);
        adapter2.setOnClickContentItemListener(this);
        classtype_lstv2.setMode(PullToRefreshBase.Mode.BOTH);
        classtype_lstv2.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MineCommentActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH2 = true;
                pageIndex2 = 1;
                initData2();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MineCommentActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH2 = false;
                pageIndex2++;
                initData2();
            }
        });
        classtype_lstv2.setAdapter(adapter2);
        classtype_lstv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                DianpuComment record = goods2.get(position - 1);
//                Intent deitalV = new Intent(MineFavoursActivity.this, DianpuDetailActivity.class);
//                deitalV.putExtra("emp_id_dianpu", record.getEmp_id());
//                startActivity(deitalV);
            }
        });


    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        String str = (String) object;
        if("000".equals(str)){
            switch (flag){
                case 1:
                {
                    if(lists.size() > position){
                        GoodsComment goodsComment = lists.get(position);
                        if(goodsComment != null){
                            Intent intent = new Intent(MineCommentActivity.this, DetailPaopaoGoodsActivity.class);
                            intent.putExtra("emp_id_dianpu", goodsComment.getGoodsEmpId());
                            intent.putExtra("goods_id", goodsComment.getGoodsId());
                            startActivity(intent);
                        }
                    }
                }
                break;
            }
        }
        if("111".equals(str)){
            switch (flag){
                case 1:
                {
                    if(goods2.size() > position){
                        DianpuComment dianpuComment = goods2.get(position);
                        if(dianpuComment != null){
                           Intent intent = new Intent(MineCommentActivity.this, DianpuDetailActivity.class);
                            intent.putExtra("emp_id_dianpu", dianpuComment.getEmp_id_seller());
                            startActivity(intent);
                        }
                    }
                }
                break;
            }
        }
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


    private void getData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_MINE_GOODS_COMMENT_LISTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            GoodsCommentData data = getGson().fromJson(s, GoodsCommentData.class);
                            if (data.getCode() == 200) {
                                if (IS_REFRESH) {
                                    lists.clear();
                                }
                                lists.addAll(data.getData());
                                lstv.onRefreshComplete();
                                adapter.notifyDataSetChanged();
                                if(lists.size() == 0){
                                    search_null.setVisibility(View.VISIBLE);
                                    lstv.setVisibility(View.GONE);
                                }else {
                                    search_null.setVisibility(View.GONE);
                                    lstv.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(MineCommentActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineCommentActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MineCommentActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("page", String.valueOf(pageIndex));
                params.put("emp_id", getGson().fromJson(getSp().getString("empId", ""), String.class));
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


    //获得店铺评论
    private void initData2() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appGetDianpuComment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            DianpuCommentData data = getGson().fromJson(s, DianpuCommentData.class);
                            if (data.getCode() == 200) {
                                if (IS_REFRESH2) {
                                    goods2.clear();
                                }
                                goods2.addAll(data.getData());
                                classtype_lstv2.onRefreshComplete();
                                adapter2.notifyDataSetChanged();
                                if(goods2.size() == 0){
                                    search_null2.setVisibility(View.VISIBLE);
                                    classtype_lstv2.setVisibility(View.GONE);
                                }else {
                                    search_null2.setVisibility(View.GONE);
                                    classtype_lstv2.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(MineCommentActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineCommentActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MineCommentActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("page", String.valueOf(pageIndex2));
                params.put("emp_id", getGson().fromJson(getSp().getString("empId", ""), String.class));
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

}
