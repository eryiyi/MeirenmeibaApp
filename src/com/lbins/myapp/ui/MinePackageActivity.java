package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.AnimateFirstDisplayListener;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.BankObjData;
import com.lbins.myapp.data.CountData;
import com.lbins.myapp.data.MinePackageData;
import com.lbins.myapp.entity.Count;
import com.lbins.myapp.entity.MinePackage;
import com.lbins.myapp.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 钱包
 */
public class MinePackageActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView name;
    private TextView mine_money;
    private TextView mine_count_num;
    private TextView mine_bank_card_num_two;
    private TextView mine_bank_card_num_one;
    private TextView mine_jinbi_num;
    private ImageView cover;//头像

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private MinePackage minePackage;//我的钱包

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_package_activity);

        initView();
        initData();
        //获得钱包
        getData();
        //获得银行卡列表
        getDataBanks();
        //获得积分
        getCount();

    }

    Count count = null;
    private void getCount() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_COUNT_RETURN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            CountData data = getGson().fromJson(s, CountData.class);
                            if (data.getCode() == 200) {
                                count = data.getData();
                                if(count != null){
                                    mine_count_num.setText("现有积分:￥"+count.getCount());
                                }
                            } else {
                                Toast.makeText(MinePackageActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MinePackageActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MinePackageActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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

    //获得钱包
    public void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.APP_GET_PACKAGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            MinePackageData data = getGson().fromJson(s, MinePackageData.class);
                            if (data.getCode() == 200) {
                                minePackage = data.getData();
                                if(minePackage != null){
                                    initDataPackage();
                                }
                            } else {
                                Toast.makeText(MinePackageActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MinePackageActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MinePackageActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("钱包");
        cover = (ImageView) this.findViewById(R.id.cover);
        name = (TextView) this.findViewById(R.id.name);
        mine_money = (TextView) this.findViewById(R.id.mine_money);
        mine_bank_card_num_one = (TextView) this.findViewById(R.id.mine_bank_card_num_one);
        mine_bank_card_num_two = (TextView) this.findViewById(R.id.mine_bank_card_num_two);
        mine_jinbi_num = (TextView) this.findViewById(R.id.mine_jinbi_num);
        mine_count_num = (TextView) this.findViewById(R.id.mine_count_num);
        this.findViewById(R.id.liner_profile_packget).setOnClickListener(this);
        this.findViewById(R.id.liner_profile_cztx).setOnClickListener(this);
        this.findViewById(R.id.liner_profile_bank_card).setOnClickListener(this);
        this.findViewById(R.id.liner_profile_count).setOnClickListener(this);
    }

    void initData(){
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("empCover", ""), String.class))){
            imageLoader.displayImage(getGson().fromJson(getSp().getString("empCover", ""), String.class), cover, MeirenmeibaAppApplication.txOptions, animateFirstListener);
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("emp_number", ""), String.class))){
            name.setText("账号:"+getGson().fromJson(getSp().getString("emp_number", ""), String.class));
        }
    }

    void initDataPackage(){
        mine_money.setText("￥"+ (minePackage.getPackage_money()==null?"":minePackage.getPackage_money()));
        mine_jinbi_num.setText("￥"+ (minePackage.getPackage_money()==null?"":minePackage.getPackage_money()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner_profile_packget:
            {
                //可用金币
                Intent intent = new Intent(MinePackageActivity.this, MineConsumptionActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_profile_cztx:
            {
                //充值提现
                Intent intent = new Intent(MinePackageActivity.this, BankCardCztxActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.liner_profile_bank_card:
            {
                //绑定解绑银行卡
                Intent intent = new Intent(MinePackageActivity.this, BankCardDoneActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.liner_profile_count:
            {
                //积分
                Intent intent = new Intent(MinePackageActivity.this, MineCountActivity.class);
                startActivity(intent);
            }
                break;
        }
    }

    //获得银行卡列表
    public void getDataBanks(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.APP_GET_BANK_CARDS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            BankObjData data = getGson().fromJson(s, BankObjData.class);
                            if (data.getCode() == 200) {
                                mine_bank_card_num_two.setText("已绑定"+data.getData().size()+"张");
                                mine_bank_card_num_one.setText("已绑定"+data.getData().size()+"张");
                            } else {
                                Toast.makeText(MinePackageActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MinePackageActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MinePackageActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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
