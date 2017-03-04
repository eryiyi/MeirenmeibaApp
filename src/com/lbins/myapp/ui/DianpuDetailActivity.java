package com.lbins.myapp.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
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
import com.lbins.myapp.adapter.ItemGoodsAdapter;
import com.lbins.myapp.adapter.ItemTopDianpusAdapter;
import com.lbins.myapp.adapter.OnClickContentItemListener;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.*;
import com.lbins.myapp.entity.*;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/9/11.
 * 店铺首页
 */
public class DianpuDetailActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {

    private ImageView btn_favour;
    private ImageView btn_share;
    private TextView title;

    //轮播广告
    private ViewPager viewpager;
    private AdViewPagerAdapter adapterAd;
    private LinearLayout viewGroup;
    private ImageView dot, dots[];
    private Runnable runnable;
    private int autoChangeTime = 5000;
    private List<AdObj> listsAd = new ArrayList<AdObj>();

    private TextView title_one;
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

    private TextView meigou_num;
    private TextView btn_more;

    private GridView gridv_one;
    private ItemGoodsAdapter adapterGoods;
    private List<PaopaoGoods> listsGoods = new ArrayList<PaopaoGoods>();

    private GridView gridv_two;
    private ItemTopDianpusAdapter adapterDianpu;
    private List<PaihangDianpu> listsDianpus = new ArrayList<PaihangDianpu>();
    private String emp_id_dianpu;//店铺

    private ManagerInfo managerInfo;//店铺详情对象
    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dianpu_detail_activity);
        emp_id_dianpu = getIntent().getExtras().getString("emp_id_dianpu");
        initView();

        progressDialog = new CustomProgressDialog(DianpuDetailActivity.this, "",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        //获得店铺详情
        getDetailDianpu();
        //获得店铺广告轮播图
        getAds();
        //美购
        getMeigou();

        //查询商品的好评度和消费评价数量
        getDetailComment();

        //获得更多商家
        getMoreDianpu();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        btn_favour = (ImageView) this.findViewById(R.id.btn_favour);
        btn_share = (ImageView) this.findViewById(R.id.btn_share);
        btn_favour.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        title_one = (TextView) this.findViewById(R.id.title_one);
        btn_sst = (TextView) this.findViewById(R.id.btn_sst);
        btn_gqt = (TextView) this.findViewById(R.id.btn_gqt);
        sale_num = (TextView) this.findViewById(R.id.sale_num);
        rate_comment = (TextView) this.findViewById(R.id.rate_comment);
        comment_count = (TextView) this.findViewById(R.id.comment_count);

        dp_title = (TextView) this.findViewById(R.id.dp_title);
        dp_distance = (TextView) this.findViewById(R.id.dp_distance);
        dp_count = (TextView) this.findViewById(R.id.dp_count);
        dp_star = (ImageView) this.findViewById(R.id.dp_star);
        dp_tel = (ImageView) this.findViewById(R.id.dp_tel);
        dp_tel.setOnClickListener(this);
        dp_address = (TextView) this.findViewById(R.id.dp_address);
        this.findViewById(R.id.liner_address).setOnClickListener(this);
        this.findViewById(R.id.comment_liner).setOnClickListener(this);

        meigou_num = (TextView) this.findViewById(R.id.meigou_num);
        btn_more = (TextView) this.findViewById(R.id.btn_more);
        btn_more.setOnClickListener(this);
        gridv_one = (GridView) this.findViewById(R.id.gridv_one);
        gridv_two = (GridView) this.findViewById(R.id.gridv_two);
        gridv_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(listsGoods.size() > (i)){
                    PaopaoGoods paopaoGoods = listsGoods.get(i);
                    if(paopaoGoods != null){
                        Intent intent = new Intent(DianpuDetailActivity.this, DetailPaopaoGoodsActivity.class);
                        intent.putExtra("emp_id_dianpu", paopaoGoods.getEmpId());
                        intent.putExtra("goods_id", paopaoGoods.getId());
                        startActivity(intent);
                    }
                }

            }
        });
        adapterGoods = new ItemGoodsAdapter(listsGoods, DianpuDetailActivity.this);
        gridv_one.setAdapter(adapterGoods);

        adapterDianpu = new ItemTopDianpusAdapter(listsDianpus, DianpuDetailActivity.this);
        gridv_two.setAdapter(adapterDianpu);

        gridv_one.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridv_two.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridv_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(listsDianpus.size() > (i)){
                    PaihangDianpu paihangDianpu = listsDianpus.get(i);
                    if(paihangDianpu != null){
                        Intent intent = new Intent(DianpuDetailActivity.this, DianpuDetailActivity.class);
                        intent.putExtra("emp_id_dianpu", paihangDianpu.getEmp_id());
                        startActivity(intent);
                    }
                }
            }
        });
        title.setText("店铺详情");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.dp_tel:
            {
                if(managerInfo != null){
                    //电话点击事件
                    if(!StringUtil.isNullOrEmpty(managerInfo.getCompany_tel())){
                        showMsgDialog();
                    }else{
                        showMsg(DianpuDetailActivity.this, "暂无联系电话！");
                    }
                }
            }
                break;
            case R.id.btn_more:
            {
                //更多
                Intent intent = new Intent(DianpuDetailActivity.this, MoreDianpuPaopaoGoodsActivity.class);
                intent.putExtra("emp_id_dianpu", emp_id_dianpu);
                startActivity(intent);
            }
                break;
            case R.id.btn_favour:
            {

                if (StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) ||
                        "0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) ) {
                    //未登录
                    showMsg(DianpuDetailActivity.this, "请先登录！");
                    return;
                }else {
                    //收藏
                    saveFavour();
                }

            }
                break;
            case R.id.btn_share:
            {
                //分享
            }
                break;
            case R.id.liner_address:
            {
                if(managerInfo != null){
                    //地址点击
                    if(StringUtil.isNullOrEmpty(managerInfo.getLat_company()) || StringUtil.isNullOrEmpty(managerInfo.getLng_company())){
                        //如果有地址为空
                        showMsg(DianpuDetailActivity.this, "该店铺尚未定位！");
                    }else{
                        Intent intent = new Intent(DianpuDetailActivity.this, DianpuLocationMapActivity.class);
                        intent.putExtra("lat", managerInfo.getLat_company());
                        intent.putExtra("lng", managerInfo.getLng_company());
                        intent.putExtra("name", managerInfo.getCompany_name());
                        intent.putExtra("address", managerInfo.getCompany_address());
                        startActivity(intent);
                    }
                }

            }
                break;
            case R.id.comment_liner:
            {
                if (StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) ||
                        "0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) ) {
                    //未登录
                    showMsg(DianpuDetailActivity.this, "请先登录！");
                    return;
                }else {
                    //评论列表
                    Intent intent = new Intent(DianpuDetailActivity.this, CommentListDianpuActivity.class);
                    intent.putExtra("emp_id_seller", emp_id_dianpu);
                    startActivity(intent);
                }
            }
                break;
        }
    }

    //收藏店铺
    void saveFavour(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SAVE_FAVOUR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                btn_favour.setImageDrawable(getResources().getDrawable(R.drawable.top_star_p));
                                showMsg(DianpuDetailActivity.this, "收藏店铺成功！");
                            } else if(data.getCode() == 2){
                                btn_favour.setImageDrawable(getResources().getDrawable(R.drawable.top_star_p));
                                showMsg(DianpuDetailActivity.this, "已经收藏了！");
                            }
                            else {
                                Toast.makeText(DianpuDetailActivity.this, "收藏店铺失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DianpuDetailActivity.this, "收藏店铺失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(DianpuDetailActivity.this, "收藏店铺失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", emp_id_dianpu);
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("empId", ""), String.class))){
                    params.put("emp_id_favour", getGson().fromJson(getSp().getString("empId", ""), String.class));
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



    private void showMsgDialog() {
        final Dialog picAddDialog = new Dialog(DianpuDetailActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        final TextView cont = (TextView) picAddInflate.findViewById(R.id.cont);
        cont.setText("确定拨打电话？");
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(managerInfo != null){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + managerInfo.getCompany_tel()));
                    DianpuDetailActivity.this.startActivity(intent);
                    picAddDialog.dismiss();
                }
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
        adapterAd = new AdViewPagerAdapter(DianpuDetailActivity.this);
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
            dot = new ImageView(DianpuDetailActivity.this);
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
                                    Toast.makeText(DianpuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DianpuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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

    //实例化内容
    void initData(){
        if(managerInfo != null){
            dp_title.setText(managerInfo.getCompany_name()==null?"":managerInfo.getCompany_name());

            Double start_company = Double.valueOf(managerInfo.getCompany_star()==null?"0":managerInfo.getCompany_star());
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
        }
    }

    //获得店铺广告轮播图
    void getAds(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_DIPU_ADS_LISTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    AdObjData data = getGson().fromJson(s, AdObjData.class);
                                    listsAd.clear();
                                    listsAd.addAll(data.getData());
                                    //轮播广告
                                    initViewPager();
                                } else {
                                    Toast.makeText(DianpuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DianpuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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

    //美购
    void getMeigou(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_GOODS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    PaopaoGoodsData data = getGson().fromJson(s, PaopaoGoodsData.class);
                                    listsGoods.clear();
                                    listsGoods.addAll(data.getData());
                                    adapterGoods.notifyDataSetChanged();
                                    meigou_num.setText("美购("+listsGoods.size()+")");
                                } else {
                                    Toast.makeText(DianpuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DianpuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("index", String.valueOf(pageIndex));
                params.put("empId", emp_id_dianpu);
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

    CommentNumber commentNumber;
    //获得商评论总数和好评度
    void getDetailComment(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_COMMENT_ALL_DIANPU_URN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    CommentNumberData data = getGson().fromJson(s, CommentNumberData.class);
                                    commentNumber = data.getData();
                                    if(commentNumber != null){
                                        rate_comment.setText(String.valueOf(commentNumber.getStarDouble())+"%");
                                        comment_count.setText("共"+commentNumber.getCommentCount()+"个消费评价");
                                    }
                                } else {
                                    Toast.makeText(DianpuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DianpuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", emp_id_dianpu);
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

    void getMoreDianpu(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.APP_GET_TUIJIAN_DIANPU_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    PaihangDianpuData data = getGson().fromJson(s, PaihangDianpuData.class);
                                    listsDianpus.clear();
                                    listsDianpus.addAll(data.getData());
                                    adapterDianpu.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(DianpuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DianpuDetailActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("index", String.valueOf(pageIndex));
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
