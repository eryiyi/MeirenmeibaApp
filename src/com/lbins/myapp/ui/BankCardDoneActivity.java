package com.lbins.myapp.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemBankCardAdapter;
import com.lbins.myapp.adapter.OnClickContentItemListener;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.BankObjData;
import com.lbins.myapp.data.SuccessData;
import com.lbins.myapp.entity.BankObj;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.CustomProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 绑定解绑银行卡
 */
public class BankCardDoneActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {
    private TextView title;
    private TextView btn_one;
    private TextView btn_two;
    //--------------------
    private EditText bank_card;//银行卡号
    private EditText mobile;//手机号
    private EditText bank_emp_name;//银行预留名
    private EditText bank_name;//银行名
    private EditText bank_kaihu_name;//银行开户名

    //----------------------
    private ListView lstv;
    private ItemBankCardAdapter adapter;
    List<BankObj> listsBank = new ArrayList<BankObj>();

    //viewPager
    private ViewPager vPager;
    private List<View> views;
    private View view1, view2;
    private int currentSelect = 0;//当前选中的viewpage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_card_done_activity);

        initView();

        progressDialog = new CustomProgressDialog(BankCardDoneActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        //获得银行卡列表
        getData();
    }
    //获得银行卡列表
    public void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.APP_GET_BANK_CARDS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            BankObjData data = getGson().fromJson(s, BankObjData.class);
                            if (data.getCode() == 200) {
                                listsBank.clear();
                                listsBank.addAll(data.getData());
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(BankCardDoneActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(BankCardDoneActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BankCardDoneActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
        title.setText("绑定/解绑银行卡");
        btn_one= (TextView) this.findViewById(R.id.btn_one);
        btn_two= (TextView) this.findViewById(R.id.btn_two);

        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);

        //viewPage
        vPager = (ViewPager) this.findViewById(R.id.vPager);
        views = new ArrayList<View>();
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.bank_card_one, null);
        view2 = inflater.inflate(R.layout.bank_card_two, null);
        views.add(view1);
        views.add(view2);

        vPager.setAdapter(new MyViewPagerAdapter(views));
        currentSelect = 0;
        vPager.setCurrentItem(currentSelect);
        vPager.setOnPageChangeListener(new MyOnPageChangeListener());

        //第一部分
        bank_card= (EditText) view1.findViewById(R.id.bank_card);
        mobile= (EditText) view1.findViewById(R.id.mobile);
        bank_emp_name= (EditText) view1.findViewById(R.id.bank_emp_name);
        bank_name= (EditText) view1.findViewById(R.id.bank_name);
        bank_kaihu_name= (EditText) view1.findViewById(R.id.bank_kaihu_name);

        //第二部分
        lstv = (ListView) view2.findViewById(R.id.lstv);
        adapter = new ItemBankCardAdapter(listsBank, BankCardDoneActivity.this);
        adapter.setOnClickContentItemListener(this);
        lstv.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_card:
                //验证码获得
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(BankCardDoneActivity.this, "请输入手机号！");
                    return;
                }
                break;
            case R.id.btn_one:
            {
                //绑定
                btn_one.setTextColor(getResources().getColor(R.color.red));
                btn_two.setTextColor(getResources().getColor(R.color.text_color));
                currentSelect = 0;
                vPager.setCurrentItem(currentSelect);
            }
                break;
            case R.id.btn_two:
            {
                //解绑
                btn_one.setTextColor(getResources().getColor(R.color.text_color));
                btn_two.setTextColor(getResources().getColor(R.color.red));
                currentSelect = 1;
                vPager.setCurrentItem(currentSelect);
            }
            break;

        }
    }

    public void sureBankCardAction(View view){
        if(StringUtil.isNullOrEmpty(bank_card.getText().toString())){
            showMsg(BankCardDoneActivity.this, "请输入银行卡号！");
            return;
        }
        if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
            showMsg(BankCardDoneActivity.this, "请输入手机号！");
            return;
        } if(StringUtil.isNullOrEmpty(bank_emp_name.getText().toString())){
            showMsg(BankCardDoneActivity.this, "请输入开户人姓名！");
            return;
        }if(StringUtil.isNullOrEmpty(bank_name.getText().toString())){
            showMsg(BankCardDoneActivity.this, "请输入银行名！");
            return;
        }if(StringUtil.isNullOrEmpty(bank_kaihu_name.getText().toString())){
            showMsg(BankCardDoneActivity.this, "请输入开户行信息！");
            return;
        }

        progressDialog = new CustomProgressDialog(BankCardDoneActivity.this, "正在加载中",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        saveBankCard();

    }

    BankObj bankObjTmp;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag) {
            case 1:
                bankObjTmp  = listsBank.get(position);
                showMsgDialog();
                break;
             }

    }


    private void showMsgDialog() {
        final Dialog picAddDialog = new Dialog(BankCardDoneActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        final TextView cont = (TextView) picAddInflate.findViewById(R.id.cont);
        cont.setText("确定解绑该银行卡？");
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                delBankCard();
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

    public class MyViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public MyViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
//        int one = offset * 1 + bmpW;
//        int two = one * 1;

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int arg0) {
//            Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);
//            currIndex = arg0;
//            animation.setFillAfter(true);
//            animation.setDuration(300);
//            imageView.startAnimation(animation);
            if (arg0 == 0) {
                btn_one.setTextColor(getResources().getColor(R.color.red));
                btn_two.setTextColor(getResources().getColor(R.color.text_color));
                currentSelect = 0;
            }
            if (arg0 == 1) {
                btn_one.setTextColor(getResources().getColor(R.color.text_color));
                btn_two.setTextColor(getResources().getColor(R.color.red));
                currentSelect = 1;
            }
        }
    }


    //保存银行卡信息
    public void saveBankCard(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.APP_SAVE_BANK_CARDS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                showMsg(BankCardDoneActivity.this, "绑定银行卡成功！");
                                getData();
                                btn_one.setTextColor(getResources().getColor(R.color.text_color));
                                btn_two.setTextColor(getResources().getColor(R.color.red));
                                currentSelect = 1;
                                vPager.setCurrentItem(currentSelect);
                                mobile.setText("");
                                bank_emp_name.setText("");
                                bank_kaihu_name.setText("");
                                bank_name.setText("");
                                bank_card.setText("");
                                Intent intent1 = new Intent("add_bank_card_success");
                                sendBroadcast(intent1);
                            } else if(data.getCode() == 2){
                                Toast.makeText(BankCardDoneActivity.this, "绑定失败，最多绑定5张银行卡！", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(BankCardDoneActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(BankCardDoneActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BankCardDoneActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", getGson().fromJson(getSp().getString("empId", ""), String.class));
                params.put("bank_mobile", mobile.getText().toString());
                params.put("bank_emp_name", bank_emp_name.getText().toString());
                params.put("bank_kaihu_name", bank_kaihu_name.getText().toString());
                params.put("bank_name", bank_name.getText().toString());
                params.put("bank_card", bank_card.getText().toString());
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


    //解绑银行卡
    public void delBankCard(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.APP_DELETE_BANK_CARDS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                showMsg(BankCardDoneActivity.this, "解绑银行卡成功！");
                                getData();
                            } else if(data.getCode() == 2){
                                Toast.makeText(BankCardDoneActivity.this, "绑定失败，最多绑定5张银行卡！", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(BankCardDoneActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(BankCardDoneActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BankCardDoneActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("bank_id", bankObjTmp.getBank_id());
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
