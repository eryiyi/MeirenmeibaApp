package com.lbins.myapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseFragment;
import com.lbins.myapp.ui.MinePackageActivity;
import com.lbins.myapp.ui.SetActivity;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by zhl on 2016/7/1.
 */
public class FourFragment extends BaseFragment implements View.OnClickListener{
    private View view;
    private Resources res;
    private TextView title;

    private ImageView mine_cover;//我的头像
    private TextView mine_type;//我的类型
    private TextView mine_name;//我的名字
    private TextView mine_money;//我的金钱
    private ImageView mine_erweima;//我的二维码

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.four_fragment, null);
        res = getActivity().getResources();
        initView();


        return view;
    }

    void initView(){
        view.findViewById(R.id.back).setVisibility(View.GONE);
        view.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) view.findViewById(R.id.title);
        title.setText("个人中心");
        mine_cover = (ImageView) view.findViewById(R.id.mine_cover);
        mine_type = (TextView) view.findViewById(R.id.mine_type);
        mine_name = (TextView) view.findViewById(R.id.mine_name);
        mine_money = (TextView) view.findViewById(R.id.mine_money);
        mine_erweima = (ImageView) view.findViewById(R.id.mine_erweima);


        mine_cover.setOnClickListener(this);
        mine_erweima.setOnClickListener(this);
        view.findViewById(R.id.liner_profile_unpay).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_ungetgoods).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_uncomment).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_unback).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_unpay).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_order).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_comment).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_favour).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_cart).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_packget).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_meet).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_fensi).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_count).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_ad).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_set).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_ruzhu).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_dianjia).setOnClickListener(this);
        view.findViewById(R.id.liner_profile_liulan).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mine_cover:
            {
                //头像点击
            }
                break;
            case R.id.liner_profile_unpay:
            {
                //未付款
            }
            break;
            case R.id.liner_profile_ungetgoods:
            {
                //待收货
            }
                break;
            case R.id.liner_profile_uncomment:
            {
                //待评价
            }
                break;
            case R.id.liner_profile_unback:
            {
                //退款退回
            }
                break;
            case R.id.liner_profile_order:
            {
                //我的订单
            }
                break;
            case R.id.liner_profile_comment:
            {
                //我的点评
            }
                break;
            case R.id.liner_profile_favour:
            {
                //我的收藏
            }
                break;
            case R.id.liner_profile_cart:
            {
                //我的购物车
            }
                break;
            case R.id.liner_profile_packget:
            {
                //我的钱包
                Intent intent = new Intent(getActivity(),   MinePackageActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_profile_meet:
            {
                //我的预约
            }
                break;
            case R.id.liner_profile_fensi:
            {
                //粉丝
            }
                break;
            case R.id.liner_profile_count:
            {
                //积分
            }
                break;
            case R.id.liner_profile_ad:
            {
                //推广
            }
                break;
            case R.id.liner_profile_set:
            {
                //我的设置
                Intent setV =  new Intent(getActivity(), SetActivity.class);
                startActivity(setV);
            }
                break;
            case R.id.liner_profile_ruzhu:
            {
                //我的入驻
            }
            break;
            case R.id.liner_profile_dianjia:
            {
                //我的店家
            }
                break;
            case R.id.liner_profile_liulan:
            {
                //我的浏览
            }
                break;

        }
    }
}
