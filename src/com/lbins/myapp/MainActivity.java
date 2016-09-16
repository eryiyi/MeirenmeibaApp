package com.lbins.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.CityDATA;
import com.lbins.myapp.data.LxAdData;
import com.lbins.myapp.data.ManagerInfoData;
import com.lbins.myapp.db.DBHelper;
import com.lbins.myapp.entity.City;
import com.lbins.myapp.entity.LxAd;
import com.lbins.myapp.fragment.ShangchengFragment;
import com.lbins.myapp.fragment.ProfileFragment;
import com.lbins.myapp.fragment.TuijianFragment;
import com.lbins.myapp.fragment.NearbyFragment;
import com.lbins.myapp.pinyin.PinyinComparator;
import com.lbins.myapp.ui.LoginActivity;
import com.lbins.myapp.util.HttpUtils;
import com.lbins.myapp.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class MainActivity extends BaseActivity implements View.OnClickListener,Runnable{
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fm;

    private TuijianFragment oneFragment;
    private ShangchengFragment twoFragment;
    private NearbyFragment nearbyFragment;
    private ProfileFragment profileFragment;

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

    private  List<City> listEmps = new ArrayList<City>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        res = getResources();
        fm = getSupportFragmentManager();
        initView();

        switchFragment(R.id.foot_liner_one);

        //如果是第一次启动 去加载城市数据
        SharedPreferences.Editor editor = getSp().edit();
        boolean isFirstRun = getSp().getBoolean("isFirstRunCity", true);
        if (isFirstRun) {
            editor.putBoolean("isFirstRunCity", false);
            editor.commit();
            // 启动一个线程 加载城市数据
            new Thread(MainActivity.this).start();
        }
    }

    private List<LxAd> lxads = new ArrayList<LxAd>();


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
                    oneFragment = new TuijianFragment();
                    fragmentTransaction.add(R.id.content_frame, oneFragment);
                } else {
                    fragmentTransaction.show(oneFragment);
                }
                foot_one.setImageResource(R.drawable.main_good_p);
                foot_two.setImageResource(R.drawable.main_home);
                foot_three.setImageResource(R.drawable.main_near);
                foot_four.setImageResource(R.drawable.main_mine);

                foot_one_text.setTextColor(res.getColor(R.color.red));
                foot_two_text.setTextColor(res.getColor(R.color.text_color));
                foot_three_text.setTextColor(res.getColor(R.color.text_color));
                foot_four_text.setTextColor(res.getColor(R.color.text_color));

                break;
            case R.id.foot_liner_two:
                if (twoFragment == null) {
                    twoFragment = new ShangchengFragment();
                    fragmentTransaction.add(R.id.content_frame, twoFragment);
                } else {
                    fragmentTransaction.show(twoFragment);
                }
                foot_one.setImageResource(R.drawable.main_good);
                foot_two.setImageResource(R.drawable.main_home_p);
                foot_three.setImageResource(R.drawable.main_near);
                foot_four.setImageResource(R.drawable.main_mine);

                foot_one_text.setTextColor(res.getColor(R.color.text_color));
                foot_two_text.setTextColor(res.getColor(R.color.red));
                foot_three_text.setTextColor(res.getColor(R.color.text_color));
                foot_four_text.setTextColor(res.getColor(R.color.text_color));
                break;
            case R.id.foot_liner_three:
                if (nearbyFragment == null) {
                    nearbyFragment = new NearbyFragment();
                    fragmentTransaction.add(R.id.content_frame, nearbyFragment);
                } else {
                    fragmentTransaction.show(nearbyFragment);
                }
                foot_one.setImageResource(R.drawable.main_good);
                foot_two.setImageResource(R.drawable.main_home);
                foot_three.setImageResource(R.drawable.main_near_p);
                foot_four.setImageResource(R.drawable.main_mine);

                foot_one_text.setTextColor(res.getColor(R.color.text_color));
                foot_two_text.setTextColor(res.getColor(R.color.text_color));
                foot_three_text.setTextColor(res.getColor(R.color.red));
                foot_four_text.setTextColor(res.getColor(R.color.text_color));
                break;
            case R.id.foot_liner_four:
                if (profileFragment == null) {
                    profileFragment = new ProfileFragment();
                    fragmentTransaction.add(R.id.content_frame, profileFragment);
                } else {
                    fragmentTransaction.show(profileFragment);
                }
                foot_one.setImageResource(R.drawable.main_good);
                foot_two.setImageResource(R.drawable.main_home);
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
        if (nearbyFragment != null) {
            ft.hide(nearbyFragment);
        }
        if (profileFragment != null) {
            ft.hide(profileFragment);
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
        if (StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) ||
                "0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) ) {
            //未登录
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            switchFragment(v.getId());
        }
    }

    @Override
    public void run() {
        getCitys();
    }

    void getCitys(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SELECT_CITY_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    CityDATA data = getGson().fromJson(s, CityDATA.class);
                                    listEmps.clear();
                                    listEmps.addAll(data.getData());
//                                    Collections.sort(listEmps, new PinyinComparator());
                                    //处理数据，需要的话保存到数据库
                                    if (listEmps != null) {
                                        Collections.sort(listEmps, new PinyinComparator());
                                        DBHelper.getInstance(MainActivity.this).saveCityList(listEmps);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MainActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("areaid", "");
//                params.put("cityName", keywords.getText().toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(request);
    }
}
