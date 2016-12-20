package com.lbins.myapp.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.receiver.SMSBroadcastReceiver;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 找回支付密码
 */
public class FindPwrMobilePayActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText mobile;
    private EditText card;
    private EditText pwr_one;
    private EditText pwr_two;
    private Button btn_card;

    private Resources res;

    //mob短信
    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = InternetURL.APP_MOB_KEY;//"69d6705af33d";0d786a4efe92bfab3d5717b9bc30a10d
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = InternetURL.APP_MOB_SCRECT;
    public String phString;//手机号码

    //短信读取
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_pwr_mobile_pay_activity);
        res = getResources();
        //mob短信无GUI
        SMSSDK.initSDK(this, APPKEY, APPSECRET, true);
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("找回支付密码");
        mobile = (EditText) this.findViewById(R.id.mobile);
        card = (EditText) this.findViewById(R.id.card);
        pwr_one = (EditText) this.findViewById(R.id.pwr_one);
        pwr_two = (EditText) this.findViewById(R.id.pwr_two);
        btn_card = (Button) this.findViewById(R.id.btn_card);
        btn_card.setOnClickListener(this);

        //生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();
        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册广播
        this.registerReceiver(mSMSBroadcastReceiver, intentFilter);
        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(String message) {
                //花木通的验证码：8469【掌淘科技】
                if (!StringUtil.isNullOrEmpty(message)) {
                    String codestr = StringUtil.valuteNumber(message);
                    if (!StringUtil.isNullOrEmpty(codestr)) {
                        card.setText(codestr);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_card:
            {
                //获得验证码
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(FindPwrMobilePayActivity.this, "请输入手机号！");
                    return;
                }
                if(!mobile.getText().toString().equals(getGson().fromJson(getSp().getString("empMobile", ""), String.class))){
                    showMsg(FindPwrMobilePayActivity.this, "请输入您登陆该账户的手机号码！");
                    return;
                }
                SMSSDK.getVerificationCode("86", mobile.getText().toString());//发送请求验证码，手机10s之内会获得短信验证码
                phString = mobile.getText().toString();
                btn_card.setClickable(false);//不可点击
                MyTimer myTimer = new MyTimer(60000, 1000);
                myTimer.start();
            }
                break;
        }
    }

    class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btn_card.setText(res.getString(R.string.daojishi_three));
            btn_card.setClickable(true);//可点击
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_card.setText(res.getString(R.string.daojishi_one) + millisUntilFinished / 1000 + res.getString(R.string.daojishi_two));
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                System.out.println("--------result" + event);
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
//                    Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
                    reg();

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //已经验证
                    Toast.makeText(getApplicationContext(), R.string.code_msg_one, Toast.LENGTH_SHORT).show();
                }

            } else {
//				((Throwable) data).printStackTrace();
                Toast.makeText(FindPwrMobilePayActivity.this, R.string.code_msg_two, Toast.LENGTH_SHORT).show();
//					Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();
                int status = 0;
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(FindPwrMobilePayActivity.this, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }
            if(progressDialog != null){
                progressDialog.dismiss();
            }
        }

        ;
    };

    public void onDestroy() {
        super.onPause();
        SMSSDK.unregisterAllEventHandler();
        //注销短信监听广播
        this.unregisterReceiver(mSMSBroadcastReceiver);
    }

    public void findPwrAction(View view){
        if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
            showMsg(FindPwrMobilePayActivity.this, "请输入手机号");
            return;
        }
        if(!mobile.getText().toString().equals(getGson().fromJson(getSp().getString("empMobile", ""), String.class))){
            showMsg(FindPwrMobilePayActivity.this, "请输入您登陆该账户的手机号码！");
            return;
        }
        if(StringUtil.isNullOrEmpty(card.getText().toString())){
            showMsg(FindPwrMobilePayActivity.this, "请输入验证码");
            return;
        }
        if(StringUtil.isNullOrEmpty(pwr_one.getText().toString())){
            showMsg(FindPwrMobilePayActivity.this, "请输入6到18位密码");
            return;
        }
        if(pwr_one.getText().toString().length() > 18 || pwr_one.getText().toString().length() < 6){
            showMsg(FindPwrMobilePayActivity.this, "请输入6到18位密码");
            return;
        }
        if(StringUtil.isNullOrEmpty(pwr_two.getText().toString())){
            showMsg(FindPwrMobilePayActivity.this, "请输入确认密码");
            return;
        }
        if(!pwr_one.getText().toString().equals(pwr_two.getText().toString())){
            showMsg(FindPwrMobilePayActivity.this, "两次输入密码不一致");
            return;
        }
        progressDialog = new CustomProgressDialog(FindPwrMobilePayActivity.this, "",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        SMSSDK.submitVerificationCode("86", phString, card.getText().toString());
    }


    private void reg() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.FIND_PWR_PAY__URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    showMsg(FindPwrMobilePayActivity.this, "修改支付密码成功");
                                    finish();
//                                    Intent intent = new Intent(FindPwrMobilePayActivity.this, LoginActivity.class);
//                                    startActivity(intent);
//                                    ActivityTack.getInstanse().popUntilActivity(LoginActivity.class);
                                }else {
                                    switch (Integer.parseInt(code)){
                                        case 1:
                                            showMsg(FindPwrMobilePayActivity.this, "找回支付密码失败，请稍后重试！");
                                            break;
                                        case 2:
                                            showMsg(FindPwrMobilePayActivity.this, "手机号为空！");
                                            break;
                                        case 3:
                                            showMsg(FindPwrMobilePayActivity.this, "支付密码为空！");
                                            break;
                                        case 4:
                                            showMsg(FindPwrMobilePayActivity.this, "请确认该手机号是否已经注册！");
                                            break;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(FindPwrMobilePayActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(FindPwrMobilePayActivity.this, R.string.add_failed, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empMobile", mobile.getText().toString());
                params.put("emp_pay_pass", pwr_one.getText().toString());
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
