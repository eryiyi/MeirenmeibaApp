package com.lbins.myapp.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.amap.api.maps.model.LatLng;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.AdViewPagerAdapter;
import com.lbins.myapp.adapter.ItemCommentAdapter;
import com.lbins.myapp.adapter.OnClickContentItemListener;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.GoodsCommentData;
import com.lbins.myapp.data.ManagerInfoSingleData;
import com.lbins.myapp.data.PaopaoGoodsSingleData;
import com.lbins.myapp.data.SuccessData;
import com.lbins.myapp.db.DBHelper;
import com.lbins.myapp.entity.*;
import com.lbins.myapp.library.PullToRefreshBase;
import com.lbins.myapp.library.PullToRefreshListView;
import com.lbins.myapp.util.DateUtil;
import com.lbins.myapp.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/9/16.
 * 商品详情
 */
public class DetailPaopaoGoodsActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {
    private PullToRefreshListView lstv;
    private ItemCommentAdapter adapterComment;
    private List<GoodsComment> listComments = new ArrayList<GoodsComment>();
    private boolean IS_REFRESH = true;
    private int pageIndex = 1;

    private TextView title;
    private ImageView btn_favour;
    private ImageView btn_share;
    //header
    LinearLayout headLiner;
    //轮播广告
    private ViewPager viewpager;
    private AdViewPagerAdapter adapterAd;
    private LinearLayout viewGroup;
    private ImageView dot, dots[];
    private Runnable runnable;
    private int autoChangeTime = 5000;
    private List<AdObj> listsAd = new ArrayList<AdObj>();

    private TextView btn_sst;
    private TextView btn_gqt;
    private TextView sale_num;
    private TextView rate_comment;
    private TextView comment_count;

    private TextView dp_title;
    private TextView dp_distance;
    private TextView dp_count;
    private ImageView dp_star;
    private ImageView dp_tel;
    private TextView dp_address;
    private TextView money_one;
    private TextView money_two;
    private TextView btn_money;

    private String emp_id_dianpu;//店铺用户ID
    private String goods_id;//商品ID
    private ManagerInfo managerInfo;//店铺详情对象
    private PaopaoGoods paopaoGoods;//商品详情

    private Button foot_cart;
    private Button foot_order;
    private TextView foot_goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_paopao_goods_activity);
        emp_id_dianpu = getIntent().getExtras().getString("emp_id_dianpu");
        goods_id = getIntent().getExtras().getString("goods_id");

        initView();
        if(!StringUtil.isNullOrEmpty(emp_id_dianpu)){
            //获取店铺详情
            getDetailDianpu();
        }else {
            initData();
        }

        //获取商品详情
        getDetailGoods();
        //获取商品评论
        getComment();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("商品详情");
        btn_favour = (ImageView) this.findViewById(R.id.btn_favour);
        btn_share = (ImageView) this.findViewById(R.id.btn_share);
        btn_favour.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        headLiner = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.detail_paopao_goods_header, null);
        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        ListView listView = lstv.getRefreshableView();

        listView.addHeaderView(headLiner);

        adapterComment = new ItemCommentAdapter(listComments, DetailPaopaoGoodsActivity.this);
        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//                IS_REFRESH = true;
//                pageIndex = 1;
//                getComment();
                lstv.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//                IS_REFRESH = false;
//                pageIndex++;
//                getComment();
                lstv.onRefreshComplete();
            }
        });
        lstv.setAdapter(adapterComment);
        adapterComment.setOnClickContentItemListener(this);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });


        btn_sst = (TextView) headLiner.findViewById(R.id.btn_sst);
        btn_gqt = (TextView) headLiner.findViewById(R.id.btn_gqt);
        sale_num = (TextView) headLiner.findViewById(R.id.sale_num);
        rate_comment = (TextView) headLiner.findViewById(R.id.rate_comment);
        comment_count = (TextView) headLiner.findViewById(R.id.comment_count);

        dp_title = (TextView) headLiner.findViewById(R.id.dp_title);
        dp_distance = (TextView) headLiner.findViewById(R.id.dp_distance);
        dp_count = (TextView) headLiner.findViewById(R.id.dp_count);
        dp_star = (ImageView) headLiner.findViewById(R.id.dp_star);
        dp_tel = (ImageView) headLiner.findViewById(R.id.dp_tel);
        dp_tel.setOnClickListener(this);
        dp_address = (TextView) headLiner.findViewById(R.id.dp_address);
        money_one = (TextView) headLiner.findViewById(R.id.money_one);
        money_two = (TextView) headLiner.findViewById(R.id.money_two);
        btn_money = (TextView) headLiner.findViewById(R.id.btn_money);

        foot_cart = (Button) this.findViewById(R.id.foot_cart);
        foot_order = (Button) this.findViewById(R.id.foot_order);
        foot_goods = (TextView) this.findViewById(R.id.foot_goods);
        foot_cart.setOnClickListener(this);
        foot_order.setOnClickListener(this);
        foot_goods.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_favour:
            {
                //收藏
                favour();
            }
            break;
            case R.id.btn_share:
            {
                //分享
            }
            break;
            case R.id.dp_tel:
            {
                //电话点击事件
                if(!StringUtil.isNullOrEmpty(managerInfo.getCompany_tel())){
                    showMsgDialog();
                }else{
                    showMsg(DetailPaopaoGoodsActivity.this, "暂无联系电话！");
                }
            }
            break;
            case R.id.foot_cart:
                //购物车
                //先查询是否已经存在该商品了
                if(DBHelper.getInstance(DetailPaopaoGoodsActivity.this).isSaved(paopaoGoods.getId())){
                    //如果已经加入购物车了
                    Toast.makeText(DetailPaopaoGoodsActivity.this, R.string.add_cart_is, Toast.LENGTH_SHORT).show();
                }else{
                    ShoppingCart shoppingCart = new ShoppingCart();
                    shoppingCart.setCartid(StringUtil.getUUID());
                    shoppingCart.setGoods_id(paopaoGoods.getId());
                    shoppingCart.setEmp_id(paopaoGoods.getEmpId() == null ? "" : paopaoGoods.getEmpId());
                    shoppingCart.setManager_id(paopaoGoods.getManager_id() == null ? "" : paopaoGoods.getManager_id());
                    shoppingCart.setGoods_name(paopaoGoods.getName());
                    shoppingCart.setGoods_cover(paopaoGoods.getCover());
                    shoppingCart.setSell_price(paopaoGoods.getSellPrice());
                    shoppingCart.setMarketPrice(paopaoGoods.getMarketPrice());
                    shoppingCart.setGoods_count("1");
                    shoppingCart.setDateline(DateUtil.getCurrentDateTime());
                    shoppingCart.setIs_select("0");//默认选中
                    shoppingCart.setIs_zhiying(paopaoGoods.getIs_zhiying());
                    if("0".equals(paopaoGoods.getIs_zhiying())){
                        //商家发布的商品
                        shoppingCart.setEmp_name(paopaoGoods.getNickName());
                        shoppingCart.setEmp_cover(paopaoGoods.getEmpCover());
                    }else{
                        shoppingCart.setEmp_name(paopaoGoods.getManagerName());
                        shoppingCart.setEmp_cover(paopaoGoods.getManagerCover());
                    }

                    DBHelper.getInstance(DetailPaopaoGoodsActivity.this).addShoppingToTable(shoppingCart);
                    Toast.makeText(DetailPaopaoGoodsActivity.this, R.string.add_cart_success, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.foot_order:
                if("0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))){
                    showMsg(DetailPaopaoGoodsActivity.this, "请先登录！");
                    return;
                }
                //订单
                Intent orderMakeView = new Intent(DetailPaopaoGoodsActivity.this, OrderMakeActivity.class);
                ArrayList<ShoppingCart> arrayList = new ArrayList<ShoppingCart>();

                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setCartid(StringUtil.getUUID());
                shoppingCart.setGoods_id(paopaoGoods.getId());
                shoppingCart.setEmp_id(paopaoGoods.getEmpId() == null ? "" : paopaoGoods.getEmpId());
                shoppingCart.setManager_id(paopaoGoods.getManager_id() == null ? "" : paopaoGoods.getManager_id());
                shoppingCart.setGoods_name(paopaoGoods.getName());
                shoppingCart.setGoods_cover(paopaoGoods.getCover());
                shoppingCart.setSell_price(paopaoGoods.getSellPrice());
                shoppingCart.setMarketPrice(paopaoGoods.getMarketPrice());
                shoppingCart.setGoods_count("1");
                shoppingCart.setDateline(DateUtil.getCurrentDateTime());
                shoppingCart.setIs_select("0");//默认选中
                shoppingCart.setIs_zhiying(paopaoGoods.getIs_zhiying());
                if("0".equals(paopaoGoods.getIs_zhiying())){
                    //商家发布的商品
                    shoppingCart.setEmp_name(paopaoGoods.getNickName());
                    shoppingCart.setEmp_cover(paopaoGoods.getEmpCover());
                }else{
                    shoppingCart.setEmp_name(paopaoGoods.getManagerName());
                    shoppingCart.setEmp_cover(paopaoGoods.getManagerCover());
                }
                arrayList.add(shoppingCart);
                if(arrayList !=null && arrayList.size() > 0){
                    orderMakeView.putExtra("listsgoods",arrayList);
                    startActivity(orderMakeView);
                }else{
                    Toast.makeText(DetailPaopaoGoodsActivity.this,R.string.cart_error_one,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.foot_goods:
                //进入购物车
                Intent cartView = new Intent(DetailPaopaoGoodsActivity.this, MineCartActivity.class);
                startActivity(cartView);
                break;

        }
    }


    private void showMsgDialog() {
        final Dialog picAddDialog = new Dialog(DetailPaopaoGoodsActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        final TextView cont = (TextView) picAddInflate.findViewById(R.id.cont);
        cont.setText("确定拨打电话？");
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + managerInfo.getCompany_tel()));
                DetailPaopaoGoodsActivity.this.startActivity(intent);
                picAddDialog.dismiss();
            }
        });

        //举报取消
        TextView btn_cancel = (TextView) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    private void initViewPager() {
        adapterAd = new AdViewPagerAdapter(DetailPaopaoGoodsActivity.this);
        adapterAd.change(listsAd);
        adapterAd.setOnClickContentItemListener(this);
        viewpager = (ViewPager) this.findViewById(R.id.viewpager);
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
        viewGroup = (LinearLayout) this.findViewById(R.id.viewGroup);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                20, 20);
        layoutParams.setMargins(4, 3, 4, 3);

        dots = new ImageView[adapterAd.getCount()];
        for (int i = 0; i < adapterAd.getCount(); i++) {
            dot = new ImageView(DetailPaopaoGoodsActivity.this);
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



    //获得店铺详情
    void getDetailDianpu(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_DIPU_DETAIL_LISTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    ManagerInfoSingleData data = getGson().fromJson(s, ManagerInfoSingleData.class);
                                    managerInfo = data.getData();
                                    initData();
                                } else {
                                    Toast.makeText(DetailPaopaoGoodsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DetailPaopaoGoodsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", emp_id_dianpu);
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

     //获得商品详情
    void getDetailGoods(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_GODS_DETAIL_LISTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    PaopaoGoodsSingleData data = getGson().fromJson(s, PaopaoGoodsSingleData.class);
                                    paopaoGoods = data.getData();
                                    initDataGoods();
                                } else {
                                    Toast.makeText(DetailPaopaoGoodsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DetailPaopaoGoodsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", goods_id);
                params.put("empid", getGson().fromJson(getSp().getString("empId", ""), String.class));
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

    //实例化内容--店铺模块
    void initData(){
        if(managerInfo != null){
            dp_title.setText(managerInfo.getCompany_name()==null?"":managerInfo.getCompany_name());

            int start_company = Integer.parseInt(managerInfo.getCompany_star()==null?"0":managerInfo.getCompany_star());
            if(start_company >=0 && start_company<0.5){
                dp_star.setImageDrawable(this.getResources().getDrawable(R.drawable.start_half));
            }
            if(start_company >=0.5 && start_company<1){
                dp_star.setImageDrawable(this.getResources().getDrawable(R.drawable.star_one));
            }
            if(start_company >=1 && start_company<1.5){
                dp_star.setImageDrawable(this.getResources().getDrawable(R.drawable.star_one_half));
            }
            if(start_company >=1.5 && start_company<2){
                dp_star.setImageDrawable(this.getResources().getDrawable(R.drawable.star_two));
            }
            if(start_company >=2 && start_company<2.5){
                dp_star.setImageDrawable(this.getResources().getDrawable(R.drawable.star_two_half));
            }
            if(start_company >=2.5 && start_company<3){
                dp_star.setImageDrawable(this.getResources().getDrawable(R.drawable.star_three));
            }
            if(start_company >=3 && start_company<3.5){
                dp_star.setImageDrawable(this.getResources().getDrawable(R.drawable.star_three_half));
            }
            if(start_company >=3.5 && start_company<4){
                dp_star.setImageDrawable(this.getResources().getDrawable(R.drawable.star_four));
            }
            if(start_company >=4 && start_company<4.5){
                dp_star.setImageDrawable(this.getResources().getDrawable(R.drawable.star_four_half));
            }
            if(start_company >=4.5 && start_company<5){
                dp_star.setImageDrawable(this.getResources().getDrawable(R.drawable.star_five));
            }
            if(!StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.latStr) && !StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.lngStr) && !StringUtil.isNullOrEmpty(managerInfo.getLat_company())&& !StringUtil.isNullOrEmpty(managerInfo.getLng_company()) ){
                LatLng latLng = new LatLng(Double.valueOf(MeirenmeibaAppApplication.latStr), Double.valueOf(MeirenmeibaAppApplication.lngStr));
                LatLng latLng1 = new LatLng(Double.valueOf(managerInfo.getLat_company()), Double.valueOf(managerInfo.getLng_company()));
                String distance = StringUtil.getDistance(latLng, latLng1);
                dp_distance.setText(distance + "km");
            }
            dp_address.setText(managerInfo.getCompany_address()==null?"":managerInfo.getCompany_address());
            dp_count.setText(managerInfo.getCompany_star()+"分");
        }else {
            dp_title.setText("平台自营");
            dp_star.setVisibility(View.GONE);
            dp_distance.setVisibility(View.GONE);
            dp_address.setVisibility(View.GONE);
            dp_count.setVisibility(View.GONE);
            dp_tel.setVisibility(View.GONE);
        }
    }

    //实例化商品详情部分
    void initDataGoods(){
        AdObj adObj = new AdObj();
        adObj.setMm_ad_id("");
        adObj.setMm_ad_url("");
        adObj.setMm_ad_pic(paopaoGoods.getCover());
        adObj.setMm_ad_title("");
        adObj.setEmp_id("");
        adObj.setMm_ad_num("");
        listsAd.add(adObj);

        AdObj adObj1 = new AdObj();
        adObj1.setMm_ad_id("");
        adObj1.setMm_ad_url("");
        adObj1.setMm_ad_pic(paopaoGoods.getGoods_cover1());
        adObj1.setMm_ad_title("");
        adObj1.setEmp_id("");
        adObj1.setMm_ad_num("");
        listsAd.add(adObj1);


        AdObj adObj2 = new AdObj();
        adObj2.setMm_ad_id("");
        adObj2.setMm_ad_url("");
        adObj2.setMm_ad_pic(paopaoGoods.getGoods_cover2());
        adObj2.setMm_ad_title("");
        adObj2.setEmp_id("");
        adObj2.setMm_ad_num("");
        listsAd.add(adObj2);

        //轮播广告
        initViewPager();
        money_one.setText("￥" + paopaoGoods.getSellPrice());
        btn_money.setText("￥"+paopaoGoods.getSellPrice() +"  限时抢购");
        money_two.setText("门市价：￥"+paopaoGoods.getMarketPrice());
        money_two.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
    }


    //获得评论
    void getComment(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_GOODS_COMMENT_LISTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    GoodsCommentData data = getGson().fromJson(s, GoodsCommentData.class);
                                    listComments.clear();
                                    listComments.addAll(data.getData());
                                    adapterComment.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(DetailPaopaoGoodsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DetailPaopaoGoodsActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("goodsId", goods_id);
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

    //收藏
    private void favour() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SAVE_FAVOUR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                btn_favour.setImageDrawable(getResources().getDrawable(R.drawable.top_star_p));
                                Toast.makeText(DetailPaopaoGoodsActivity.this, R.string.goods_favour_success, Toast.LENGTH_SHORT).show();
                            }else if(data.getCode() == 2){
                                btn_favour.setImageDrawable(getResources().getDrawable(R.drawable.top_star_p));
                                Toast.makeText(DetailPaopaoGoodsActivity.this, R.string.goods_favour_error_one, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(DetailPaopaoGoodsActivity.this, R.string.goods_favour_error_two, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DetailPaopaoGoodsActivity.this, R.string.goods_favour_error_two, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(DetailPaopaoGoodsActivity.this, R.string.goods_favour_error_two, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("goods_id", paopaoGoods.getId());
                params.put("emp_id_favour", getGson().fromJson(getSp().getString("empId", ""), String.class));
                params.put("emp_id_goods", paopaoGoods.getEmpId());
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
