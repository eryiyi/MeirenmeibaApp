package com.lbins.myapp.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.*;
import com.alipay.sdk.app.PayTask;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.MinePackageData;
import com.lbins.myapp.data.OrderInfoAndSignDATA;
import com.lbins.myapp.data.SuccessData;
import com.lbins.myapp.data.WxPayObjData;
import com.lbins.myapp.db.DBHelper;
import com.lbins.myapp.entity.*;
import com.lbins.myapp.pay.OrderInfoAndSign;
import com.lbins.myapp.pay.PayResult;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.wxpay.MD5;
import com.lbins.myapp.wxpay.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.*;

/**
 * Created by zhl on 2016/8/30.
 * 选择支付方式 扫一扫付款
 */
public class PaySelectTwoActivity extends BaseActivity implements View.OnClickListener,Runnable {
    private OrdersForm SGform = new OrdersForm();
    private List<Order> listOrders = new ArrayList<Order>();//订单集合 --传给服务器
    private ShoppingAddress shoppingAddress;

    //---------------------------------支付开始----------------------------------------
    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
//                        Toast.makeText(OrderMakeActivity.this, "支付成功",
//                                Toast.LENGTH_SHORT).show();
                        //更新订单状态
//                        updateMineOrder();
                        Toast.makeText(PaySelectTwoActivity.this, R.string.order_success, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PaySelectTwoActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PaySelectTwoActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(PaySelectTwoActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };
    //------------------------------------------------------------------------------------

    private TextView title;
    private EditText count_money;
    private String out_trade_no;
    private ImageView check_btn_wx;
    private ImageView check_btn_ali;
    private ImageView check_btn_ling;

    private PayScanObj payScanObj;

    //微信支付
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    private Button btn_pay_lq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_select_two_activity);
        registerBoradcastReceiver();
        payScanObj = (PayScanObj) getIntent().getExtras().get("payScanObj");
        //微信支付
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, InternetURL.WEIXIN_APPID, false);

        initView();
//        toCalculate();

//        payScanObj.setPay_count(cont.getText().toString());

    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("选择支付方式");
        count_money = (EditText) this.findViewById(R.id.count_money);
        check_btn_wx = (ImageView) this.findViewById(R.id.check_btn_wx);
        check_btn_ali = (ImageView) this.findViewById(R.id.check_btn_ali);
        check_btn_ling = (ImageView) this.findViewById(R.id.check_btn_ling);
        check_btn_wx.setOnClickListener(this);
        check_btn_ali.setOnClickListener(this);
        check_btn_ling.setOnClickListener(this);
        btn_pay_lq = (Button) this.findViewById(R.id.btn_pay_lq);
        btn_pay_lq.setOnClickListener(this);
    }

    //计算金额总的
    void toCalculate(){
        if(payScanObj != null){
            count_money.setText(getResources().getString(R.string.countPrices) + payScanObj.getPay_count());
        }
    }

    private int selectPayWay = 0;//0微信 1支付宝  2零钱
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.check_btn_wx:
            {
                //微信
                check_btn_wx.setImageDrawable(getResources().getDrawable(R.drawable.cart_selected));
                check_btn_ali.setImageDrawable(getResources().getDrawable(R.drawable.cart_selectno));
                check_btn_ling.setImageDrawable(getResources().getDrawable(R.drawable.cart_selectno));
                selectPayWay = 0;
            }
                break;
            case R.id.check_btn_ali:
            {
                //阿里
                check_btn_wx.setImageDrawable(getResources().getDrawable(R.drawable.cart_selectno));
                check_btn_ali.setImageDrawable(getResources().getDrawable(R.drawable.cart_selected));
                check_btn_ling.setImageDrawable(getResources().getDrawable(R.drawable.cart_selectno));
                selectPayWay = 1;
            }
            break;
            case R.id.check_btn_ling:
            {
                //零钱
                check_btn_wx.setImageDrawable(getResources().getDrawable(R.drawable.cart_selectno));
                check_btn_ali.setImageDrawable(getResources().getDrawable(R.drawable.cart_selectno));
                check_btn_ling.setImageDrawable(getResources().getDrawable(R.drawable.cart_selected));
                selectPayWay = 2;
            }
            break;
            case R.id.btn_pay_lq:
            {
                btn_pay_lq.setClickable(false);
                if(StringUtil.isNullOrEmpty(count_money.getText().toString())){
                    showMsg(PaySelectTwoActivity.this, "请输入金额！");
                    btn_pay_lq.setClickable(true);
                    return;
                }else {
                    view.setClickable(false);
                    payScanObj.setPay_count(count_money.getText().toString());
                    switch (selectPayWay){
                        case 0:
                        {
                            //微信
                            //先传值给服务端
                            if(payScanObj != null ){
                                listOrders.add(new Order("", getGson().fromJson(getSp().getString("empId", ""), String.class), payScanObj.getEmp_id()
                                        ,"", "1", payScanObj.getPay_count()
                                        ,"0","0","","","","","","","","1", payScanObj.getPay_count(), "0", "0"));
                            }
                            SGform.setList(listOrders);
                            if(listOrders!=null && listOrders.size() > 0){
                                //传值给服务端
                                goToPayWeixin();
                            }
                        }
                        break;
                        case 1:
                        {
                            //先传值给服务端
                            if(payScanObj != null){
                                listOrders.add(new Order("", getGson().fromJson(getSp().getString("empId", ""), String.class), payScanObj.getEmp_id()
                                        ,"", "1", payScanObj.getPay_count()
                                        ,"0","0","","","","","","","","0", payScanObj.getPay_count(), "0", "0"));
                            }
                            SGform.setList(listOrders);
                            //支付宝
                            if(listOrders!=null && listOrders.size() > 0){
                                //传值给服务端
                                sendOrderToServer();
                            }
                        }
                        break;
                        case 2:
                        {
                            if(StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("emp_pay_pass", ""), String.class))){
                                //如果支付密码为空
                                btn_pay_lq.setClickable(true);
                                Intent intent = new Intent(PaySelectTwoActivity.this, UpdatePayPwrActivity.class);
                                startActivity(intent);
                                return;
                            }else {
                                btn_pay_lq.setClickable(true);
                                //输入支付密码
                                showMsgDialog();
                            }
                        }
                        break;
                    }
                }
            }
                break;
        }
    }

    private void showMsgDialog() {
        final Dialog picAddDialog = new Dialog(PaySelectTwoActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_pay_dialog, null);
        Button btn_sure = (Button) picAddInflate.findViewById(R.id.btn_sure);
        final EditText cont = (EditText) picAddInflate.findViewById(R.id.cont);
        cont.setHint("请输入支付密码");
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtil.isNullOrEmpty(cont.getText().toString())){
                    showMsg(PaySelectTwoActivity.this, "请输入支付密码");
                }else {
                    if(getGson().fromJson(getSp().getString("emp_pay_pass", ""), String.class).equals(cont.getText().toString())){
                        //等于
                        picAddDialog.dismiss();
                        //零钱支付
                        getLingqian();
                    }else {
                        showMsg(PaySelectTwoActivity.this, "请输入正确的支付密码");
                    }
                }
            }
        });

        //取消
        Button btn_cancel = (Button) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }


    String xmlStr = "";
    WxPayObj wxPayObj;


    private MinePackage minePackage;//我的钱包
    //获得钱包
    public void getLingqian(){
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
                                    String package_money = minePackage.getPackage_money();//金额
                                    if(!StringUtil.isNullOrEmpty(package_money)){
                                        payLingqian();
                                    }else {
                                        showMsg(PaySelectTwoActivity.this, "支付出现问题，请稍后！");
                                    }
                                }else{
                                    showMsg(PaySelectTwoActivity.this, "支付出现问题，请稍后！");
                                }
                            } else {
                                showMsg(PaySelectTwoActivity.this, "支付出现问题，请稍后！");
                            }
                        } else {
                            showMsg(PaySelectTwoActivity.this, "支付出现问题，请稍后！");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showMsg(PaySelectTwoActivity.this, "支付出现问题，请稍后！");
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

    //零钱支付处理
    void payLingqian(){
        //先传值给服务端
        if(payScanObj != null){
            listOrders.add(new Order("", getGson().fromJson(getSp().getString("empId", ""), String.class), payScanObj.getEmp_id()
                    ,"", "1", payScanObj.getPay_count()
                    ,"0","0","","","","","","","","2", payScanObj.getPay_count(), "0", "0"));
        }
        SGform.setList(listOrders);
        //支付宝
        if(listOrders!=null && listOrders.size() > 0){
            //传值给服务端---零钱
            sendOrderToServerLq();
        }
    }


    //传order给服务器---零钱
    private void sendOrderToServerLq() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SEND_ORDER_TOSERVER_LQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            OrderInfoAndSignDATA data = getGson().fromJson(s, OrderInfoAndSignDATA.class);
                            if (data.getCode() == 200) {
                                //删除购物车商品
                                deleteCart();
                                //已经生成订单，等待支付，下面去支付
                                out_trade_no= data.getData().getOut_trade_no();
                                //更新订单状态
                                updateMineOrder();
                                //通知修改零钱显示
                                Intent intent1 = new Intent("update_mine_package_success");
                                sendBroadcast(intent1);
                            }else if(data.getCode() == 2){
                                Toast.makeText(PaySelectTwoActivity.this, R.string.order_error_three, Toast.LENGTH_SHORT).show();
                                finish();
                            }else if(data.getCode() == 3){
                                Toast.makeText(PaySelectTwoActivity.this, R.string.order_error_four, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(PaySelectTwoActivity.this, R.string.order_error_one, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(PaySelectTwoActivity.this, R.string.order_error_one, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(PaySelectTwoActivity.this, R.string.order_error_one, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("list", new Gson().toJson(SGform));
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

    //传order给服务器
    private void sendOrderToServer() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SEND_ORDER_TOSERVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            OrderInfoAndSignDATA data = getGson().fromJson(s, OrderInfoAndSignDATA.class);
                            if (data.getCode() == 200) {
                                //删除购物车商品
                                deleteCart();
                                //已经生成订单，等待支付，下面去支付
                                out_trade_no= data.getData().getOut_trade_no();
                                pay(data.getData());//调用支付接口
                            }else if(data.getCode() == 2){
                                Toast.makeText(PaySelectTwoActivity.this, R.string.order_error_three, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(PaySelectTwoActivity.this, R.string.order_error_one, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(PaySelectTwoActivity.this, R.string.order_error_one, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(PaySelectTwoActivity.this, R.string.order_error_one, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("list", new Gson().toJson(SGform));
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

    //清空购物车
    void deleteCart(){
        DBHelper.getInstance(PaySelectTwoActivity.this).deleteShopping();
        Intent clear_cart = new Intent("cart_clear");
        sendBroadcast(clear_cart);
    }

    //---------------------------------------------------------支付宝------------------------------------------

    /**
     * call alipay sdk pay. 调用SDK支付
     *
     */
    public void pay(final OrderInfoAndSign orderInfoAndSign) {

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfoAndSign.getOrderInfo() + "&sign=\"" + orderInfoAndSign.getSign() + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PaySelectTwoActivity.this);
                Map<String, String> result = alipay.payV2(payInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }


    //更新订单状态
    void updateMineOrder(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_ORDER_TOSERVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                Toast.makeText(PaySelectTwoActivity.this, R.string.order_success, Toast.LENGTH_SHORT).show();
                                //跳转到订单列表
                                //跳转到订单列表
//                                Intent orderView =  new Intent(PaySelectTwoActivity.this, MineOrdersActivity.class);
//                                orderView.putExtra("status", "");
//                                startActivity(orderView);
                                finish();
                            } else {
                                Toast.makeText(PaySelectTwoActivity.this, R.string.order_error_two, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(PaySelectTwoActivity.this, R.string.order_error_two, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(PaySelectTwoActivity.this, R.string.order_error_two, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("out_trade_no",  out_trade_no);
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
    //--------------------zhifubao -------------------
    //----------------微信---------------
    public void goToPayWeixin(){
        // 将该app注册到微信
        api.registerApp(InternetURL.WEIXIN_APPID);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SEND_ORDER_TOSERVER_WX,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            WxPayObjData data = getGson().fromJson(s, WxPayObjData.class);
                            if (data.getCode() == 200) {
                                //我们服务端已经生成订单，微信支付统一下单
                                wxPayObj = data.getData();
                                if(wxPayObj != null){
                                    xmlStr =wxPayObj.getXmlStr();
                                    out_trade_no = wxPayObj.getOut_trade_no();
                                }
                                // 启动一个线程
                                new Thread(PaySelectTwoActivity.this).start();
                            } else {
                                Toast.makeText(PaySelectTwoActivity.this, R.string.order_error_one, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(PaySelectTwoActivity.this, R.string.order_error_one, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(PaySelectTwoActivity.this, R.string.order_error_one, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("list", new Gson().toJson(SGform));
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
    public Map<String,String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName=parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if("xml".equals(nodeName)==false){
                            xml.put(nodeName,parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;
    }

    @Override
    public void run() {
        String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
        byte[] buf = Util.httpPost(url, xmlStr);
        String content = new String(buf);
        Map<String,String> xmlMap=decodeXml(content);
        PayReq req = new PayReq();

        req.appId			= xmlMap.get("appid");
        req.partnerId		=  xmlMap.get("mch_id");
        req.prepayId		= xmlMap.get("prepay_id");
        req.nonceStr		= xmlMap.get("nonce_str");
        req.packageValue			= " Sign=WXPay";
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams).toUpperCase();

        api.sendReq(req);
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }
    StringBuffer sb=new StringBuffer();;

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(InternetURL.WX_API_KEY);//微信支付key

        this.sb.append("sign str\n"+sb.toString()+"\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        return appSign;
    }


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("pay_wx_success")) {
//                updateMineOrder();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("pay_wx_success");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }


}
