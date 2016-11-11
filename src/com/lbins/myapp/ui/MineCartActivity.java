package com.lbins.myapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemCartAdapter;
import com.lbins.myapp.adapter.OnClickContentItemListener;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.db.DBHelper;
import com.lbins.myapp.entity.ShoppingCart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/7.
 * 购物车
 */
public class MineCartActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {
    private ListView lstv;
    private ItemCartAdapter adapter;
    private List<ShoppingCart> lists = new ArrayList<>();
    private TextView countPrice;
    private ShoppingCart shoppingCart;
    private Button goAccount;
    private TextView title;//标题
    private boolean flag = false;
    private ImageView search_null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        setContentView(R.layout.mine_cart_activity);
        lists = DBHelper.getInstance(MineCartActivity.this).getShoppingList();
        initView();
        toCalculate();
        if(lists.size() == 0){
            search_null.setVisibility(View.VISIBLE);
            lstv.setVisibility(View.GONE);
        }else{
            search_null.setVisibility(View.GONE);
            lstv.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        search_null = (ImageView) this.findViewById(R.id.search_null);
        this.findViewById(R.id.back).setOnClickListener(this);
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemCartAdapter(lists , MineCartActivity.this , flag);
        adapter.setOnClickContentItemListener(this);
        lstv.setAdapter(adapter);
        countPrice = (TextView) this.findViewById(R.id.countPrice);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                if(lists.size()>position){
//                    shoppingCart = lists.get(position);
//                    if (shoppingCart != null) {
//                        Intent goodsdetail = new Intent(MineCartActivity.this, DetailPaopaoGoodsActivity.class);
//                        goodsdetail.putExtra("emp_id_dianpu", shoppingCart.getEmp_id());
//                        goodsdetail.putExtra("goods_id", shoppingCart.getGoods_id());
//                        startActivity(goodsdetail);
//                    }
//                }
            }
        });
        goAccount = (Button) this.findViewById(R.id.goAccount);
        goAccount.setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        String titleStr = getResources().getString(R.string.mine_cart_title);
        title.setText(String.format(titleStr, lists.size()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.goAccount:
                if("0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))){
                    showMsg(MineCartActivity.this, "请先登录！");
                    return;
                }
                //去结算
                Intent orderMakeView = new Intent(MineCartActivity.this, OrderMakeActivity.class);
                ArrayList<ShoppingCart> arrayList = new ArrayList<ShoppingCart>();
                if(lists != null){
                  for(int i=0;i<lists.size();i++){
                      if(lists.get(i).getIs_select().equals("0")){
                          arrayList.add(lists.get(i));
                      }
                  }
                }
                if(arrayList !=null && arrayList.size() > 0){
                    orderMakeView.putExtra("listsgoods",arrayList);
                    startActivity(orderMakeView);
                    finish();
                }else{
                    Toast.makeText(MineCartActivity.this,R.string.cart_error_one,Toast.LENGTH_SHORT).show();
                }
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
                    Toast.makeText(MineCartActivity.this, R.string.select_zero,Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    lists.get(position).setGoods_count(String.valueOf((Integer.parseInt(lists.get(position).getGoods_count()) - 1)));
                    adapter.notifyDataSetChanged();
                }
                toCalculate();
                break;
            case 4:
                //删除
                DBHelper.getInstance(MineCartActivity.this).deleteShoppingByGoodsId(lists.get(position).getCartid());
                lists.remove(position);
                adapter.notifyDataSetChanged();
                toCalculate();
                break;
            case 5:
            {
                shoppingCart = lists.get(position);
                if (shoppingCart != null) {
                    Intent goodsdetail = new Intent(MineCartActivity.this, DetailPaopaoGoodsActivity.class);
                    goodsdetail.putExtra("emp_id_dianpu", shoppingCart.getEmp_id());
                    goodsdetail.putExtra("goods_id", shoppingCart.getGoods_id());
                    startActivity(goodsdetail);
                }
            }
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
            countPrice.setText(getResources().getString(R.string.countPrices) + df.format(doublePrices).toString());
        }
    }



    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("cart_clear")) {
                //刷新内容
                toCalculate();
                lists.clear();
                adapter.notifyDataSetChanged();
            }
        }

    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("cart_clear");//设置刷新
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
    public void goEdite(View view){
        //编辑
        flag = true;
        adapter.notifyDataSetChanged();
    }

}
