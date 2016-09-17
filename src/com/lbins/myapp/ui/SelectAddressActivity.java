package com.lbins.myapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemSelectAddressAdapter;
import com.lbins.myapp.adapter.OnClickContentItemListener;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.MineAddressDATA;
import com.lbins.myapp.entity.ShoppingAddress;
import com.lbins.myapp.util.StringUtil;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/9.
 * 选择收货地址
 */
public class SelectAddressActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {
    private TextView title;
    private ListView lstv;
    private ItemSelectAddressAdapter adapter;
    private List<ShoppingAddress> lists = new ArrayList<>();

    //新增收货地址
    private Button button_add_address;
    private String address_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        address_id  = getIntent().getExtras().getString("address_id");
        setContentView(R.layout.mine_address_activity);
        initView();
        getData();
    }

    private void initView() {
        title = (TextView) this.findViewById(R.id.title);
        title.setText("选择地址");
        this.findViewById(R.id.back).setOnClickListener(this);
        button_add_address = (Button) this.findViewById(R.id.button_add_address);
        button_add_address.setOnClickListener(this);
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemSelectAddressAdapter(lists, SelectAddressActivity.this, address_id);
        lstv.setAdapter(adapter);
        adapter.setOnClickContentItemListener(this);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShoppingAddress goodsAddress = lists.get(i);
                Intent intent = new Intent(SelectAddressActivity.this, OrderMakeActivity.class);
                intent.putExtra("select_address", goodsAddress);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.button_add_address:
                Intent addAddressView = new Intent(SelectAddressActivity.this, MineAddressAddProvinceActivity.class);
                startActivity(addAddressView);
                break;
        }
    }
    public void getData(){
        //获得收货地址列表
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.MINE_ADDRSS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            MineAddressDATA data = getGson().fromJson(s, MineAddressDATA.class);
                            if (data.getCode() == 200) {
                                lists.clear();
                                lists.addAll(data.getData());
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(SelectAddressActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SelectAddressActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SelectAddressActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("add_address_success")) {
                //刷新内容
                getData();
            }
            if (action.equals("update_address_success")) {
                //刷新内容
                getData();
            }
        }

    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("add_address_success");
        myIntentFilter.addAction("update_address_success");
        //注册广播
        this.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 1:
                //进入详细页
                break;
        }
    }
}
