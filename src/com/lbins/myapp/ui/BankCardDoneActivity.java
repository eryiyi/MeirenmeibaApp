package com.lbins.myapp.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemBankCardAdapter;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/8/30.
 * 绑定解绑银行卡
 */
public class BankCardDoneActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView btn_one;
    private TextView btn_two;
    //--------------------
    private EditText bank_card;//银行卡号
    private EditText mobile;//手机号
    private EditText card;//验证码
    private Button btn_card;

    //----------------------
    private ListView lstv;
    private ItemBankCardAdapter adapter;
    List<String> listsBank = new ArrayList<String>();

    //viewPager
    private ViewPager vPager;
    private List<View> views;
    private View view1, view2;
    private int currentSelect = 0;//当前选中的viewpage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_card_done_activity);

        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("绑定/解绑银行卡");
        btn_one= (TextView) this.findViewById(R.id.btn_one);
        btn_two= (TextView) this.findViewById(R.id.btn_two);

        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);

        //viewPage
        vPager = (ViewPager) this.findViewById(R.id.vPager);
        views = new ArrayList<View>();
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.bank_card_one, null);
        view2 = inflater.inflate(R.layout.bank_card_two, null);
        views.add(view1);
        views.add(view2);

        vPager.setAdapter(new MyViewPagerAdapter(views));
        currentSelect = 0;
        vPager.setCurrentItem(currentSelect);
        vPager.setOnPageChangeListener(new MyOnPageChangeListener());

        //第一部分
        bank_card= (EditText) view1.findViewById(R.id.bank_card);
        mobile= (EditText) view1.findViewById(R.id.mobile);
        card= (EditText) view1.findViewById(R.id.card);
        btn_card= (Button) view1.findViewById(R.id.btn_card);

        btn_card.setOnClickListener(this);
        //第二部分
        lstv = (ListView) view2.findViewById(R.id.lstv);
        listsBank.add("");
        listsBank.add("");
        listsBank.add("");
        listsBank.add("");
        listsBank.add("");
        adapter = new ItemBankCardAdapter(listsBank, BankCardDoneActivity.this);
        lstv.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_card:
                //验证码获得
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(BankCardDoneActivity.this, "请输入手机号！");
                    return;
                }
                break;
            case R.id.btn_one:
            {
                //绑定
                btn_one.setTextColor(getResources().getColor(R.color.red));
                btn_two.setTextColor(getResources().getColor(R.color.text_color));
                currentSelect = 0;
                vPager.setCurrentItem(currentSelect);
            }
                break;
            case R.id.btn_two:
            {
                //解绑
                btn_one.setTextColor(getResources().getColor(R.color.text_color));
                btn_two.setTextColor(getResources().getColor(R.color.red));
                currentSelect = 1;
                vPager.setCurrentItem(currentSelect);
            }
            break;

        }
    }

    public void sureBankCardAction(View view){
        if(StringUtil.isNullOrEmpty(bank_card.getText().toString())){
            showMsg(BankCardDoneActivity.this, "请输入银行卡号！");
            return;
        }
        if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
            showMsg(BankCardDoneActivity.this, "请输入手机号！");
            return;
        } if(StringUtil.isNullOrEmpty(card.getText().toString())){
            showMsg(BankCardDoneActivity.this, "请输入验证码！");
            return;
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


}
