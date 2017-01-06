package com.lbins.myapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemKefuTelAdapter;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.KefuTelData;
import com.lbins.myapp.entity.KefuTel;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.CustomProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 */
public class KefuTelActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ListView lstv;
    private ImageView back;
    private ItemKefuTelAdapter adapter;
    private List<KefuTel> lists = new ArrayList<KefuTel>();

    private ListView classtype_lstv1;//列表
    private ItemKefuTelAdapter adapter1;
    private List<KefuTel> goods1 = new ArrayList<KefuTel>();
    private int pageIndex1 = 1;
    private static boolean IS_REFRESH1 = true;
    //viewPager
    private ViewPager vPager;
    private List<View> views;
    private View view1, view2;
    private int currentSelect = 0;//当前选中的viewpage

    private TextView btn_one;
    private TextView btn_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kefu_tel_activity);
        initView();
        progressDialog = new CustomProgressDialog(KefuTelActivity.this, "",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getData();

        initData2();
    }

    private void initView() {

        btn_one= (TextView) this.findViewById(R.id.btn_one);
        btn_two= (TextView) this.findViewById(R.id.btn_two);

        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);

        //viewPage
        vPager = (ViewPager) this.findViewById(R.id.vPager);
        views = new ArrayList<View>();
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.mine_keft__one, null);
        view2 = inflater.inflate(R.layout.mine_keft__one, null);
        views.add(view1);
        views.add(view2);

        vPager.setAdapter(new MyViewPagerAdapter(views));
        currentSelect = 0;
        vPager.setCurrentItem(currentSelect);
        vPager.setOnPageChangeListener(new MyOnPageChangeListener());




        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("客服QQ");

        lstv = (ListView) view1.findViewById(R.id.minegoods_lstv);
        adapter = new ItemKefuTelAdapter(lists, KefuTelActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(lists.size() > i){
                    KefuTel kefuTel = lists.get(i);
                    if(kefuTel != null && !StringUtil.isNullOrEmpty(kefuTel.getMm_tel())){
                        String url="mqqwpa://im/chat?chat_type=wpa&uin="+kefuTel.getMm_tel();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                }

            }
        });


        //第二部分
        classtype_lstv1 = (ListView) view2.findViewById(R.id.minegoods_lstv);
        adapter1 = new ItemKefuTelAdapter(goods1, KefuTelActivity.this);

        classtype_lstv1.setAdapter(adapter1);
        classtype_lstv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(goods1.size() > position){
                    KefuTel kefuTel = goods1.get(position);
                    if(kefuTel != null && !StringUtil.isNullOrEmpty(kefuTel.getMm_tel())){
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + kefuTel.getMm_tel()));
                        startActivity(intent);
                    }
                }

            }
        });

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

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int arg0) {
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



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_one:
            {
                btn_one.setTextColor(getResources().getColor(R.color.red));
                btn_two.setTextColor(getResources().getColor(R.color.text_color));
                currentSelect = 0;
                vPager.setCurrentItem(currentSelect);
            }
            break;
            case R.id.btn_two:
            {
                btn_one.setTextColor(getResources().getColor(R.color.text_color));
                btn_two.setTextColor(getResources().getColor(R.color.red));
                currentSelect = 1;
                vPager.setCurrentItem(currentSelect);
            }
            break;
        }
    }

    public void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_KEFU_TEL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            KefuTelData data = getGson().fromJson(s, KefuTelData.class);
                            if (data.getCode() == 200) {
                                lists.clear();
                                lists.addAll(data.getData());
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(KefuTelActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(KefuTelActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(KefuTelActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mm_tel_type", "0");
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

    private void initData2() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_KEFU_TEL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            KefuTelData data = getGson().fromJson(s, KefuTelData.class);
                            if (data.getCode() == 200) {
                                goods1.clear();
                                goods1.addAll(data.getData());
                                adapter1.notifyDataSetChanged();
                            } else {
                                Toast.makeText(KefuTelActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(KefuTelActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(KefuTelActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mm_tel_type", "1");
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
