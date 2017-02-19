package com.lbins.myapp.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.camera.createqr.CreateQRImageTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/8/30.
 * 商家三个二维码
 */
public class MineErweimaActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView btn_one;
    private TextView btn_two;
    private TextView btn_three;

    //viewPager
    private ViewPager vPager;
    private List<View> views;
    private View view1, view2, view3;
    private int currentSelect = 0;//当前选中的viewpage

    private ImageView pic_one;
    private ImageView pic_two;
    private ImageView pic_three;

    private String url1 ;//有偿消费
    private String url2 ;//无偿消费
    private String url3 ;//加粉丝
    Bitmap bitmap1 ;//有偿消费图片1
    Bitmap bitmap2 ;//无偿消费图片2
    Bitmap bitmap3 ;//加粉丝图片3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_erweima_activity);
        initView();

        //加粉丝
        url1 = InternetURL.GET_GET_GOODS_URN+"?emp_id="+getGson().fromJson(getSp().getString("empId", ""), String.class);
        url2 = InternetURL.GET_GET_DXK_GOODS_URN+"?emp_id="+getGson().fromJson(getSp().getString("empId", ""), String.class);
        url3 = InternetURL.SAVE_FAVOUR_URL+"?emp_id="+getGson().fromJson(getSp().getString("empId", ""), String.class);

        bitmap1 = CreateQRImageTest.createQRImage(url1);
        bitmap2 = CreateQRImageTest.createQRImage(url2);
        bitmap3 = CreateQRImageTest.createQRImage(url3);

        pic_one.setImageBitmap(bitmap1);
        pic_two.setImageBitmap(bitmap2);
        pic_three.setImageBitmap(bitmap3);
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("我的二维码");
        btn_one= (TextView) this.findViewById(R.id.btn_one);
        btn_two= (TextView) this.findViewById(R.id.btn_two);
        btn_three= (TextView) this.findViewById(R.id.btn_three);

        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);

        //viewPage
        vPager = (ViewPager) this.findViewById(R.id.vPager);
        views = new ArrayList<View>();
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.erweima_one, null);
        view2 = inflater.inflate(R.layout.erweima_two, null);
        view3 = inflater.inflate(R.layout.erweima_three, null);

        if("1".equals(getGson().fromJson(getSp().getString("is_card", ""), String.class))){
            views.add(view1);
            views.add(view2);
        }else {
            views.add(view1);
            btn_two.setVisibility(View.GONE);
        }
//        views.add(view3);

        pic_one = (ImageView) view1.findViewById(R.id.pic_one);
        pic_two = (ImageView) view2.findViewById(R.id.pic_one);
        pic_three = (ImageView) view3.findViewById(R.id.pic_one);

        vPager.setAdapter(new MyViewPagerAdapter(views));
        currentSelect = 0;
        vPager.setCurrentItem(currentSelect);
        vPager.setOnPageChangeListener(new MyOnPageChangeListener());

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

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int arg0) {
            if (arg0 == 0) {
                btn_one.setTextColor(getResources().getColor(R.color.red));
                btn_two.setTextColor(getResources().getColor(R.color.text_color));
                btn_three.setTextColor(getResources().getColor(R.color.text_color));
                currentSelect = 0;
            }
            if (arg0 == 1) {
                btn_one.setTextColor(getResources().getColor(R.color.text_color));
                btn_two.setTextColor(getResources().getColor(R.color.red));
                btn_three.setTextColor(getResources().getColor(R.color.text_color));
                currentSelect = 1;
            }
            if (arg0 == 2) {
                btn_one.setTextColor(getResources().getColor(R.color.text_color));
                btn_two.setTextColor(getResources().getColor(R.color.text_color));
                btn_three.setTextColor(getResources().getColor(R.color.red));
                currentSelect = 2;
            }
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_one:
            {
                btn_one.setTextColor(getResources().getColor(R.color.red));
                btn_two.setTextColor(getResources().getColor(R.color.text_color));
                btn_three.setTextColor(getResources().getColor(R.color.text_color));
                currentSelect = 0;
                vPager.setCurrentItem(currentSelect);
            }
                break;
            case R.id.btn_two:
            {
                btn_one.setTextColor(getResources().getColor(R.color.text_color));
                btn_two.setTextColor(getResources().getColor(R.color.red));
                btn_three.setTextColor(getResources().getColor(R.color.text_color));
                currentSelect = 1;
                vPager.setCurrentItem(currentSelect);
            }
                break;
            case R.id.btn_three:
            {
                btn_one.setTextColor(getResources().getColor(R.color.text_color));
                btn_two.setTextColor(getResources().getColor(R.color.text_color));
                btn_three.setTextColor(getResources().getColor(R.color.red));
                currentSelect = 2;
                vPager.setCurrentItem(currentSelect);
            }
                break;
        }
    }
}
