package com.lbins.myapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.fragment.FirstFragment;
import com.lbins.myapp.fragment.FourFragment;
import com.lbins.myapp.fragment.SecondFragment;
import com.lbins.myapp.fragment.ThreeFragment;
import com.lbins.myapp.util.HttpUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    /**
     * Called when the activity is first created.
     */

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fm;

    private FirstFragment oneFragment;
    private SecondFragment twoFragment;
    private ThreeFragment threeFragment;
    private FourFragment fourFragment;

    private ImageView foot_one;
    private ImageView foot_two;
    private ImageView foot_three;
    private ImageView foot_four;

    private TextView foot_one_text;
    private TextView foot_two_text;
    private TextView foot_three_text;
    private TextView foot_four_text;

    //设置底部图标
    Resources res;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        res = getResources();
        fm = getSupportFragmentManager();
        initView();
        switchFragment(R.id.foot_liner_one);
    }

    private void initView() {
        foot_one = (ImageView) this.findViewById(R.id.foot_one);
        foot_two = (ImageView) this.findViewById(R.id.foot_two);
        foot_three = (ImageView) this.findViewById(R.id.foot_three);
        foot_four = (ImageView) this.findViewById(R.id.foot_four);
        this.findViewById(R.id.foot_liner_one).setOnClickListener(this);
        this.findViewById(R.id.foot_liner_two).setOnClickListener(this);
        this.findViewById(R.id.foot_liner_three).setOnClickListener(this);
        this.findViewById(R.id.foot_liner_four).setOnClickListener(this);

        foot_one_text = (TextView) this.findViewById(R.id.foot_one_text);
        foot_two_text = (TextView) this.findViewById(R.id.foot_two_text);
        foot_three_text = (TextView) this.findViewById(R.id.foot_three_text);
        foot_four_text = (TextView) this.findViewById(R.id.foot_four_text);
    }

    public void switchFragment(int id) {
        fragmentTransaction = fm.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (id) {
            case R.id.foot_liner_one:
                if (oneFragment == null) {
                    oneFragment = new FirstFragment();
                    fragmentTransaction.add(R.id.content_frame, oneFragment);
                } else {
                    fragmentTransaction.show(oneFragment);
                }
                foot_one.setImageResource(R.drawable.main_home_p);
                foot_two.setImageResource(R.drawable.main_good);
                foot_three.setImageResource(R.drawable.main_near);
                foot_four.setImageResource(R.drawable.main_mine);

                foot_one_text.setTextColor(res.getColor(R.color.red));
                foot_two_text.setTextColor(res.getColor(R.color.text_color));
                foot_three_text.setTextColor(res.getColor(R.color.text_color));
                foot_four_text.setTextColor(res.getColor(R.color.text_color));

                break;
            case R.id.foot_liner_two:
                if (twoFragment == null) {
                    twoFragment = new SecondFragment();
                    fragmentTransaction.add(R.id.content_frame, twoFragment);
                } else {
                    fragmentTransaction.show(twoFragment);
                }
                foot_one.setImageResource(R.drawable.main_home);
                foot_two.setImageResource(R.drawable.main_good_p);
                foot_three.setImageResource(R.drawable.main_near);
                foot_four.setImageResource(R.drawable.main_mine);

                foot_one_text.setTextColor(res.getColor(R.color.text_color));
                foot_two_text.setTextColor(res.getColor(R.color.red));
                foot_three_text.setTextColor(res.getColor(R.color.text_color));
                foot_four_text.setTextColor(res.getColor(R.color.text_color));
                break;
            case R.id.foot_liner_three:
                if (threeFragment == null) {
                    threeFragment = new ThreeFragment();
                    fragmentTransaction.add(R.id.content_frame, threeFragment);
                } else {
                    fragmentTransaction.show(threeFragment);
                }
                foot_one.setImageResource(R.drawable.main_home);
                foot_two.setImageResource(R.drawable.main_good);
                foot_three.setImageResource(R.drawable.main_near_p);
                foot_four.setImageResource(R.drawable.main_mine);

                foot_one_text.setTextColor(res.getColor(R.color.text_color));
                foot_two_text.setTextColor(res.getColor(R.color.text_color));
                foot_three_text.setTextColor(res.getColor(R.color.red));
                foot_four_text.setTextColor(res.getColor(R.color.text_color));
                break;
            case R.id.foot_liner_four:
                if (fourFragment == null) {
                    fourFragment = new FourFragment();
                    fragmentTransaction.add(R.id.content_frame, fourFragment);
                } else {
                    fragmentTransaction.show(fourFragment);
                }
                foot_one.setImageResource(R.drawable.main_home);
                foot_two.setImageResource(R.drawable.main_good);
                foot_three.setImageResource(R.drawable.main_near);
                foot_four.setImageResource(R.drawable.main_mine_p);

                foot_one_text.setTextColor(res.getColor(R.color.text_color));
                foot_two_text.setTextColor(res.getColor(R.color.text_color));
                foot_three_text.setTextColor(res.getColor(R.color.text_color));
                foot_four_text.setTextColor(res.getColor(R.color.red));
                break;

        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction ft) {
        if (oneFragment != null) {
            ft.hide(oneFragment);
        }
        if (twoFragment != null) {
            ft.hide(twoFragment);
        }
        if (threeFragment != null) {
            ft.hide(threeFragment);
        }
        if (fourFragment != null) {
            ft.hide(fourFragment);
        }
    }

    boolean isMobileNet, isWifiNet;

    @Override
    public void onClick(View v) {
        try {
            isMobileNet = HttpUtils.isMobileDataEnable(getApplicationContext());
            isWifiNet = HttpUtils.isWifiDataEnable(getApplicationContext());
            if (!isMobileNet && !isWifiNet) {
                Toast.makeText(this, R.string.net_work_error, Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        switchFragment(v.getId());
    }
}
