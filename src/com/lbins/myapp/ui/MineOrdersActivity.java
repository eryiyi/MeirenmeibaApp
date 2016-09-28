package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.alipay.sdk.app.PayTask;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemMineOrderAdapter;
import com.lbins.myapp.adapter.OnClickContentItemListener;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.OrderInfoAndSignDATA;
import com.lbins.myapp.data.OrdersVoDATA;
import com.lbins.myapp.data.SuccessData;
import com.lbins.myapp.entity.OrderVo;
import com.lbins.myapp.library.PullToRefreshBase;
import com.lbins.myapp.library.PullToRefreshListView;
import com.lbins.myapp.pay.OrderInfoAndSign;
import com.lbins.myapp.pay.PayResult;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.OrderCancelPopWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/15.
 * 我的订单
 */
public class MineOrdersActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {
    private String order_no;
    //---------------------------------支付开始----------------------------------------
    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
//                        Toast.makeText(OrderMakeActivity.this, "支付成功",
//                                Toast.LENGTH_SHORT).show();
                        //更新订单状态
                        updateMineOrder();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(MineOrdersActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(MineOrdersActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(MineOrdersActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };
    //------------------------------------------------------------------------------------
    private PullToRefreshListView classtype_lstv;//列表
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    private ItemMineOrderAdapter adapter;
    private List<OrderVo> orderVos = new ArrayList<>();
    private TextView text_one;
    private TextView text_two;
    private TextView text_three;
    private TextView text_four;
    private String status="";

    private OrderCancelPopWindow orderCancelPopWindow;//取消订单
    private OrderCancelPopWindow orderCancelPopWindowTwo;//确认收货
    private OrderCancelPopWindow orderCancelPopWindowThree;//删除订单

    private TextView title;
    private ImageView search_null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_order_activity);
        initView();
        initData();
    }

    private void initView() {
        search_null = (ImageView) this.findViewById(R.id.search_null);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("我的订单");
        this.findViewById(R.id.back).setOnClickListener(this);
        classtype_lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        adapter = new ItemMineOrderAdapter(orderVos,MineOrdersActivity.this);
        adapter.setOnClickContentItemListener(this);

        classtype_lstv.setMode(PullToRefreshBase.Mode.BOTH);
        classtype_lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MineOrdersActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MineOrdersActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                initData();
            }
        });
        classtype_lstv.setAdapter(adapter);
        classtype_lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderVo orderVo = orderVos.get(position-1);
                Intent detailView = new Intent(MineOrdersActivity.this, DetailOrderActivity.class);
                detailView.putExtra("orderVo",orderVo);
                startActivity(detailView);
            }
        });

        text_one = (TextView) this.findViewById(R.id.text_one);
        text_two = (TextView) this.findViewById(R.id.text_two);
        text_three = (TextView) this.findViewById(R.id.text_three);
        text_four = (TextView) this.findViewById(R.id.text_four);
        text_one.setOnClickListener(this);
        text_two.setOnClickListener(this);
        text_three.setOnClickListener(this);
        text_four.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text_one:
                text_one.setTextColor(getResources().getColor(R.color.red));
                text_two.setTextColor(getResources().getColor(R.color.text_color));
                text_three.setTextColor(getResources().getColor(R.color.text_color));
                text_four.setTextColor(getResources().getColor(R.color.text_color));
                status = "";
                initData();
                break;
            case R.id.text_two:
                text_one.setTextColor(getResources().getColor(R.color.text_color));
                text_two.setTextColor(getResources().getColor(R.color.red));
                text_three.setTextColor(getResources().getColor(R.color.text_color));
                text_four.setTextColor(getResources().getColor(R.color.text_color));
                status = "1";
                initData();
                break;
            case R.id.text_three:
                text_one.setTextColor(getResources().getColor(R.color.text_color));
                text_two.setTextColor(getResources().getColor(R.color.text_color));
                text_three.setTextColor(getResources().getColor(R.color.red));
                text_four.setTextColor(getResources().getColor(R.color.text_color));
                status = "2";
                initData();
                break;
            case R.id.text_four:
                text_one.setTextColor(getResources().getColor(R.color.text_color));
                text_two.setTextColor(getResources().getColor(R.color.text_color));
                text_three.setTextColor(getResources().getColor(R.color.text_color));
                text_four.setTextColor(getResources().getColor(R.color.red));
                status = "5";
                initData();
                break;
            case R.id.back:
                finish();
                break;
        }
    }


    OrderVo orderVoTmp;
    int tmpPosition;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        orderVoTmp = orderVos.get(position);
        tmpPosition = position;
        switch (flag){
            case 1:
                //确认收货
                showSure();
                break;
            case 2:
                //去付款
                sendOrderSingle();
                break;
            case 3:
                //投诉
                break;
            case 4:
                //取消订单
                showCancel();
                break;
            case 5:
                //评价
                Intent comment = new Intent(this, PublishGoodCommentActivity.class);
                comment.putExtra("goods_id", orderVoTmp.getGoods_id());
                comment.putExtra("emp_id", (orderVoTmp.getEmp_id()==null?"":orderVoTmp.getEmp_id()));//商品所有者
                startActivity(comment);
                break;
            case 6:
                //删除订单
                showDelete();
                break;
        }
    }
    //取订单
    private void initData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.MINE_ORDERS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            OrdersVoDATA data = getGson().fromJson(s, OrdersVoDATA.class);
                            if (data.getCode() == 200) {
                                if (IS_REFRESH) {
                                    orderVos.clear();
                                }
                                orderVos.addAll(data.getData());
                                classtype_lstv.onRefreshComplete();
                                adapter.notifyDataSetChanged();
                                if(orderVos.size() == 0){
                                    search_null.setVisibility(View.VISIBLE);
                                    classtype_lstv.setVisibility(View.GONE);
                                }else{
                                    search_null.setVisibility(View.GONE);
                                    classtype_lstv.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(MineOrdersActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineOrdersActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MineOrdersActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("page", String.valueOf(pageIndex));
                params.put("empId", getGson().fromJson(getSp().getString("empId", ""), String.class));
                params.put("status", status);
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

    private void showCancel() {
        orderCancelPopWindow = new OrderCancelPopWindow(MineOrdersActivity.this, itemsOnClick);
        //显示窗口
        orderCancelPopWindow.showAtLocation(MineOrdersActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    private void showSure() {
        orderCancelPopWindowTwo = new OrderCancelPopWindow(MineOrdersActivity.this, itemsOnClickTwo);
        //显示窗口
        orderCancelPopWindowTwo.showAtLocation(MineOrdersActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    private void showDelete() {
        orderCancelPopWindowThree = new OrderCancelPopWindow(MineOrdersActivity.this, itemsOnClickThree);
        //显示窗口
        orderCancelPopWindowThree.showAtLocation(MineOrdersActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            orderCancelPopWindow.dismiss();
            switch (v.getId()) {
                case R.id.sure:
                {
                    //取消订单
                    cancelOrder();
                }
                break;
            }
        }
    };
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClickTwo = new View.OnClickListener() {

        public void onClick(View v) {
            orderCancelPopWindowTwo.dismiss();
            switch (v.getId()) {
                case R.id.sure:
                {
                    //确认收货
                    sureOrder();
                }
                break;
            }
        }
    };

    private View.OnClickListener itemsOnClickThree = new View.OnClickListener() {

        public void onClick(View v) {
            orderCancelPopWindowThree.dismiss();
            switch (v.getId()) {
                case R.id.sure:
                {
                    //删除订单
                    deleteOrder();
                }
                break;
            }
        }
    };

    //取消订单
    private void cancelOrder() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
               InternetURL.UPDATE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                orderVos.get(tmpPosition).setStatus("3");
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MineOrdersActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineOrdersActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MineOrdersActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_no", orderVoTmp.getOrder_no());
                params.put("status", "3");
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

    //确认收货
    private void sureOrder() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                orderVos.get(tmpPosition).setStatus("5");
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MineOrdersActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineOrdersActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MineOrdersActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_no", orderVoTmp.getOrder_no());
                params.put("status", "5");
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

    //去付款
    private void sendOrderSingle(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SAVE_ORDER_SIGNLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            OrderInfoAndSignDATA data = getGson().fromJson(s, OrderInfoAndSignDATA.class);
                            if (data.getCode() == 200) {
                                order_no= data.getData().getOut_trade_no();
                                pay(data.getData());//调用支付接口
                            } else {
                                Toast.makeText(MineOrdersActivity.this, R.string.order_error_one, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineOrdersActivity.this, R.string.order_error_one, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MineOrdersActivity.this, R.string.order_error_one, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_no", orderVoTmp.getOrder_no());
                params.put("doublePrices", String.valueOf(orderVoTmp.getPayable_amount()));
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

    //---------------------------------------------------------支付宝------------------------------------------

    /**
     * call alipay sdk pay. 调用SDK支付
     *
     */
    public void pay(OrderInfoAndSign orderInfoAndSign) {

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfoAndSign.getOrderInfo() + "&sign=\"" + orderInfoAndSign.getSign() + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(MineOrdersActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

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
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     *
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(MineOrdersActivity.this);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
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
                InternetURL.UPDATE_ORDER_TOSERVER_SINGLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                Toast.makeText(MineOrdersActivity.this, R.string.order_success, Toast.LENGTH_SHORT).show();
                                orderVos.get(tmpPosition).setStatus("2");
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MineOrdersActivity.this, R.string.order_error_two, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineOrdersActivity.this, R.string.order_error_two, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MineOrdersActivity.this, R.string.order_error_two, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_no",  order_no);
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


    //删除订单
    public void deleteOrder() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                orderVos.remove(tmpPosition);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(MineOrdersActivity.this, R.string.delete_order_success, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MineOrdersActivity.this, R.string.cancel_order_error_one, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MineOrdersActivity.this, R.string.cancel_order_error_one, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MineOrdersActivity.this, R.string.cancel_order_error_one, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_no", orderVoTmp.getOrder_no());
                params.put("status", "4");//4作废订单
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
