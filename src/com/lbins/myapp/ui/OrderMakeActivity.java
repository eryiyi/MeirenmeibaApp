package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.lbins.myapp.adapter.ItemCartAdapter;
import com.lbins.myapp.adapter.OnClickContentItemListener;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.OrderInfoAndSignDATA;
import com.lbins.myapp.data.ShoppingAddressSingleDATA;
import com.lbins.myapp.data.SuccessData;
import com.lbins.myapp.db.DBHelper;
import com.lbins.myapp.entity.Order;
import com.lbins.myapp.entity.OrdersForm;
import com.lbins.myapp.entity.ShoppingAddress;
import com.lbins.myapp.entity.ShoppingCart;
import com.lbins.myapp.pay.OrderInfoAndSign;
import com.lbins.myapp.pay.PayResult;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.CustomProgressDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/2.
 * 生成订单
 */
public class OrderMakeActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {

    private TextView order_name;//收货人
    private TextView order_tel;//电话
    private TextView order_location;//地址
    private ImageView select_location;//进入选择地址
    private TextView no_address;//收货地址暂无

    private Button order_sure;//确定按钮
    private TextView order_count;//价格合计
    private List<ShoppingCart> lists;//购物车集合
    private ItemCartAdapter adapter;
    private ListView lstv;
    private ShoppingCart shoppingCart;

    private ShoppingAddress shoppingAddress;

    private ArrayList<ShoppingCart> listsSelect = new ArrayList<ShoppingCart>();//被选中商品集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_make_activity);
        lists = (List<ShoppingCart>) getIntent().getExtras().get("listsgoods");
        initView();

        progressDialog = new CustomProgressDialog(OrderMakeActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        toCalculate();
        getMorenAddress();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        order_sure = (Button) this.findViewById(R.id.order_sure);
        order_count = (TextView) this.findViewById(R.id.order_count);
        order_name = (TextView) this.findViewById(R.id.order_name);
        order_tel = (TextView) this.findViewById(R.id.order_tel);
        order_location = (TextView) this.findViewById(R.id.order_location);
        select_location = (ImageView) this.findViewById(R.id.select_location);

        select_location.setOnClickListener(this);
        order_sure.setOnClickListener(this);
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemCartAdapter(lists , OrderMakeActivity.this, false);
        adapter.setOnClickContentItemListener(this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                shoppingCart = lists.get(position);
//                if (shoppingCart != null) {
//                    //查询商品信息，根据商品ID
//                    getGoodsById();
//                }
            }
        });
        no_address = (TextView) this.findViewById(R.id.no_address);
        no_address.setVisibility(View.GONE);
        no_address.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.order_sure:
                if(shoppingAddress != null){
                    //先传值给服务端
                    if(lists != null && lists.size() > 0){
                        for(int i=0;i<lists.size();i++){
                            ShoppingCart shoppingCart = lists.get(i);
                            if(shoppingCart!=null && shoppingCart.getIs_select().equals("0")){
                                listsSelect.add(shoppingCart);
                            }
                        }
                    }
                    Intent intent = new Intent(OrderMakeActivity.this, PaySelectActivity.class);
                    intent.putExtra("listsSelect", listsSelect);
                    intent.putExtra("shoppingAddress", shoppingAddress);
                    startActivity(intent);
                }else{
                    order_sure.setClickable(true);
                    Toast.makeText(OrderMakeActivity.this, R.string.no_address_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.select_location:
            case R.id.no_address:
                Intent selectAddressView = new Intent(OrderMakeActivity.this, SelectAddressActivity.class);
                if(shoppingAddress != null){
                    selectAddressView.putExtra("address_id", shoppingAddress.getAddress_id());
                }else {
                    selectAddressView.putExtra("address_id", "0");
                }
                startActivityForResult(selectAddressView, 0);
                break;
        }
    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 1:
                //左侧选择框按钮
                if("0".equals(lists.get(position).getIs_select())){
                    lists.get(position).setIs_select("1");
                }else {
                    lists.get(position).setIs_select("0");
                }
                adapter.notifyDataSetChanged();
                toCalculate();
                break;
            case 2:
                //加号
                lists.get(position).setGoods_count(String.valueOf((Integer.parseInt(lists.get(position).getGoods_count()) + 1)));
                adapter.notifyDataSetChanged();
                toCalculate();
                break;
            case 3:
                //减号
                int selectNum = Integer.parseInt(lists.get(position).getGoods_count());
                if(selectNum == 0){
                    Toast.makeText(OrderMakeActivity.this, R.string.select_zero,Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    lists.get(position).setGoods_count(String.valueOf((Integer.parseInt(lists.get(position).getGoods_count()) - 1)));
                    adapter.notifyDataSetChanged();
                }
                toCalculate();
                break;
            case 4:
                //删除
                DBHelper.getInstance(OrderMakeActivity.this).deleteShoppingByGoodsId(lists.get(position).getCartid());
                lists.remove(position);
                adapter.notifyDataSetChanged();
                toCalculate();
                break;
        }
    }

    //计算金额总的
    void toCalculate(){
        DecimalFormat df = new DecimalFormat("0.00");
        if (lists != null){
            Double doublePrices = 0.0;
            for(int i=0; i<lists.size() ;i++){
                ShoppingCart shoppingCart = lists.get(i);
                if(shoppingCart.getIs_select() .equals("0")){
                    //默认是选中的
                    doublePrices = doublePrices + Double.parseDouble(shoppingCart.getSell_price()) * Double.parseDouble(shoppingCart.getGoods_count());
                }
            }
            order_count.setText(getResources().getString(R.string.countPrices) + df.format(doublePrices).toString());
        }
    }


    //获得默认收货地址
    public void getMorenAddress(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_MOREN_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            if (StringUtil.isJson(s)) {
                                ShoppingAddressSingleDATA data = getGson().fromJson(s, ShoppingAddressSingleDATA.class);
                                if (data.getCode() == 200) {
                                    //获得默认收货地址
                                    shoppingAddress = data.getData();
                                    if(shoppingAddress != null){
                                        initData();
                                    }else{
                                        //没有收货地址的话
                                        no_address.setVisibility(View.VISIBLE);
                                        order_name.setVisibility(View.GONE);
                                        order_tel.setVisibility(View.GONE);
                                        order_location.setVisibility(View.GONE);
                                        if (progressDialog != null) {
                                            progressDialog.dismiss();
                                        }
                                    }
                                } else {
                                    if (progressDialog != null) {
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(OrderMakeActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(OrderMakeActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(OrderMakeActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(OrderMakeActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empId", getGson().fromJson(getSp().getString("empId", ""), String.class));
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

    void initData(){
        no_address.setVisibility(View.GONE);
        order_name.setVisibility(View.VISIBLE);
        order_tel.setVisibility(View.VISIBLE);
        order_location.setVisibility(View.VISIBLE);
        order_name.setText(shoppingAddress.getAccept_name());
        order_tel.setText(shoppingAddress.getPhone());
        order_location.setText(shoppingAddress.getProvinceName() + shoppingAddress.getCityName() + shoppingAddress.getAreaName() + shoppingAddress.getAddress());
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                shoppingAddress = (ShoppingAddress) data.getExtras().get("select_address");
                if(shoppingAddress != null){
                    initData();
                }
                break;
        }
    }



}
