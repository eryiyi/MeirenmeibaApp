package com.lbins.myapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.entity.GoodsType;
import com.lbins.myapp.fragment.ProTypeFragment;
import com.lbins.myapp.fragment.TuijianFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/8/30.
 */
public class SearchMoreTypeActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;

    private List<GoodsType> list = new ArrayList<GoodsType>();
    private TextView[] tvList;
    private View[] views;
    private LayoutInflater inflater;
    private ScrollView scrollView;
    private ViewPager viewpager;
    private int currentItem = 0;
    private ShopAdapter shopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_more_type_activity);
        initView();
        //查询大类别
        list.clear();
        if(TuijianFragment.listGoodsType != null){
            list.addAll(TuijianFragment.listGoodsType);
        }
        if(list != null && list.size()>0){
            list.remove(list.size()-1);
        }
        showToolsView();
        initPager();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("全部分类");
        scrollView = (ScrollView) findViewById(R.id.tools_scrlllview);
        shopAdapter = new ShopAdapter(getSupportFragmentManager());
        inflater = LayoutInflater.from(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView() {
        LinearLayout toolsLayout = (LinearLayout) findViewById(R.id.tools);
        tvList = new TextView[list.size()];
        views = new View[list.size()];

        for (int i = 0; i < list.size(); i++) {
            View view = inflater.inflate(R.layout.item_list_layout, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(list.get(i).getTypeName());
            toolsLayout.addView(view);
            tvList[i] = textView;
            views[i] = view;
        }
        changeTextColor(0);
    }

    private View.OnClickListener toolsItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            viewpager.setCurrentItem(v.getId());
        }
    };

    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager() {
        viewpager = (ViewPager) findViewById(R.id.goods_pager);
        viewpager.setAdapter(shopAdapter);
        viewpager.setOnPageChangeListener(onPageChangeListener);
    }

    /**
     * OnPageChangeListener<br/>
     * 监听ViewPager选项卡变化事的事件
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            if (viewpager.getCurrentItem() != arg0)
                viewpager.setCurrentItem(arg0);
            if (currentItem != arg0) {
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }
            currentItem = arg0;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    };

    /**
     * ViewPager 加载选项卡
     *
     * @author Administrator
     *
     */
    private class ShopAdapter extends FragmentPagerAdapter {
        public ShopAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            Fragment fragment = new ProTypeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    /**
     * 改变textView的颜色
     *
     * @param id
     */
    private void changeTextColor(int id) {
        for (int i = 0; i < tvList.length; i++) {
            if (i != id) {
                tvList[i].setBackgroundColor(0x00000000);
                tvList[i].setTextColor(0xFF000000);
            }
        }
        tvList[id].setBackgroundColor(0xFFFFFFFF);
        tvList[id].setTextColor(0xFFFF5D5E);
    }

    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition) {
        int x = (views[clickPosition].getTop());
        scrollView.smoothScrollTo(0, x);
    }
}
