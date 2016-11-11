package com.lbins.myapp;

import android.app.Dialog;
import android.content.*;
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
import com.lbins.myapp.data.CardEmpData;
import com.lbins.myapp.data.CityDATA;
import com.lbins.myapp.db.DBHelper;
import com.lbins.myapp.db.RecordLogin;
import com.lbins.myapp.entity.CardEmp;
import com.lbins.myapp.entity.City;
import com.lbins.myapp.entity.LxAd;
import com.lbins.myapp.fragment.NearbyFragment;
import com.lbins.myapp.fragment.ProfileFragment;
import com.lbins.myapp.fragment.ShangchengFragment;
import com.lbins.myapp.fragment.TuijianFragment;
import com.lbins.myapp.pinyin.PinyinComparator;
import com.lbins.myapp.ui.CaptureActivity;
import com.lbins.myapp.ui.KefuTelActivity;
import com.lbins.myapp.ui.LoginActivity;
import com.lbins.myapp.util.DateUtil;
import com.lbins.myapp.util.HttpUtils;
import com.lbins.myapp.util.StringUtil;
import org.json.JSONObject;

import java.util.*;

public class MainActivity extends BaseActivity implements View.OnClickListener{
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
        registerMessageReceiver();
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getCitys();
                }
            }).start();
        }

        if (StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) ||
                "0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) ) {
            //未登录
        } else {
            if("1".equals(getGson().fromJson(getSp().getString("is_card_emp", ""), String.class))){
                //是定向卡会员
                getCardEmp();
            }else {
                //不是定向卡会员
                showMsgDialog();
            }
        }
    }

    private void showMsgDialog() {

            final Dialog picAddDialog = new Dialog(MainActivity.this, R.style.dialog);
            View picAddInflate = View.inflate(this, R.layout.msg_dialog, null);
            TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
            final TextView cont = (TextView) picAddInflate.findViewById(R.id.cont);
            cont.setText("充值成为定向卡会员，享受免费消费一年");
            btn_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, KefuTelActivity.class);
                    startActivity(intent);
                    picAddDialog.dismiss();
                }
            });

            //取消
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
    CardEmp cardEmp;//定向卡会还详情
    public void getCardEmp(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_CARD_EMP_BY_ID_URN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            CardEmpData data = getGson().fromJson(s, CardEmpData.class);
                            if (data.getCode() == 200) {
                                cardEmp = data.getData();
                                showMsgDialogDxk();
                            }else {
                            }
                        } else {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
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

    private void showMsgDialogDxk() {
        //判断今天是否弹框了
        if(DBHelper.getInstance(MainActivity.this).isRecordLogin(getGson().fromJson(getSp().getString("empId", ""), String.class), DateUtil.getDate())){
            //已经存在了
        }else{
            final Dialog picAddDialog = new Dialog(MainActivity.this, R.style.spinner_Dialog);
            View picAddInflate = View.inflate(this, R.layout.msg_dialog, null);
            TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
            final TextView cont = (TextView) picAddInflate.findViewById(R.id.cont);
            cont.setText("定向卡会员到期日："+ DateUtil.getDate(cardEmp.getLx_card_emp_end_time(), "yyyy-MM-dd"));
            btn_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    picAddDialog.dismiss();
                }
            });

            //取消
            TextView btn_cancel = (TextView) picAddInflate.findViewById(R.id.btn_cancel);
            btn_cancel.setVisibility(View.GONE);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    picAddDialog.dismiss();
                }
            });
            picAddDialog.setContentView(picAddInflate);
            picAddDialog.show();

            RecordLogin recordLogin = new RecordLogin();
            recordLogin.setId(System.currentTimeMillis()+"");
            recordLogin.setEmp_id(getGson().fromJson(getSp().getString("empId", ""), String.class));
            recordLogin.setDateline(DateUtil.getDate());
            DBHelper.getInstance(MainActivity.this).addRecordLogin(recordLogin);
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




    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.lbins.myapp.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                //todo
            }
        }
    }


    public static boolean isForeground = false;
    @Override
    public void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    public void onPause() {
        isForeground = false;
        super.onPause();
    }

    public void scanAction(View view){
        //扫描
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivity(intent);
    }
}
