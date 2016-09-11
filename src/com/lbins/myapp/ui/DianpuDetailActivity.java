package com.lbins.myapp.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.AdViewPagerAdapter;
import com.lbins.myapp.adapter.ItemIndexGoodsAdapter;
import com.lbins.myapp.adapter.ItemTuijianDianpusAdapter;
import com.lbins.myapp.adapter.OnClickContentItemListener;
import com.lbins.myapp.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> listsAd = new ArrayList<String>();

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
    private ItemIndexGoodsAdapter adapterGoods;
    private List<String> listsGoods = new ArrayList<String>();

    private GridView gridv_two;
    private ItemTuijianDianpusAdapter adapterDianpu;
    private List<String> listsDianpus = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dianpu_detail_activity);
        initView();
        //轮播广告
        initViewPager();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        btn_favour = (ImageView) this.findViewById(R.id.btn_favour);
        btn_share = (ImageView) this.findViewById(R.id.btn_share);
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
        dp_address = (TextView) this.findViewById(R.id.dp_address);

        meigou_num = (TextView) this.findViewById(R.id.meigou_num);
        btn_more = (TextView) this.findViewById(R.id.btn_more);

        gridv_one = (GridView) this.findViewById(R.id.gridv_one);
        gridv_two = (GridView) this.findViewById(R.id.gridv_two);
        listsGoods.add("");
        listsGoods.add("");
        listsGoods.add("");
        listsGoods.add("");
        listsGoods.add("");
        adapterGoods = new ItemIndexGoodsAdapter(listsGoods, DianpuDetailActivity.this);
        gridv_one.setAdapter(adapterGoods);

        listsDianpus.add("");
        listsDianpus.add("");
        listsDianpus.add("");
        listsDianpus.add("");
        listsDianpus.add("");
        adapterDianpu = new ItemTuijianDianpusAdapter(listsDianpus, DianpuDetailActivity.this);
        gridv_two.setAdapter(adapterDianpu);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    private void initViewPager() {
        listsAd.add("");
        listsAd.add("");
        listsAd.add("");
        listsAd.add("");
        listsAd.add("");
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
}
