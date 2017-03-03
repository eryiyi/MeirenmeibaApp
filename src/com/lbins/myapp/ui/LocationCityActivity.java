package com.lbins.myapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.SlideCityAdapter;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.CityDATA;
import com.lbins.myapp.db.DBHelper;
import com.lbins.myapp.entity.City;
import com.lbins.myapp.pinyin.PinyinComparator;
import com.lbins.myapp.pinyin.SideBar;
import com.lbins.myapp.util.StringUtil;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by zhl on 2016/9/14.
 */
public class LocationCityActivity extends BaseActivity implements View.OnClickListener {
    private EditText keywords;
    private Resources res;

    private ListView lvContact;
    private SideBar indexBar;
    private WindowManager mWindowManager;
    private TextView mDialogText;

    private List<City> listEmps = new ArrayList<City>();
    private List<City> listEmpsAll = new ArrayList<City>();
    SlideCityAdapter adapter;
    private LinearLayout liner_location;
    private TextView mine_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_city_activity);
        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        initView();
        //判断是否有城市列表
        List<City> citys = DBHelper.getInstance(LocationCityActivity.this).getCityList();
        if(citys !=null && citys.size() > 0 ){
            listEmpsAll.addAll(DBHelper.getInstance(LocationCityActivity.this).getCityList());//全部城市
            listEmps.addAll(listEmpsAll);//城市列表
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getCitys();
                }
            }).start();
        }

        adapter.notifyDataSetChanged();
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

                                    listEmpsAll.addAll(data.getData());//全部城市
                                    listEmps.addAll(listEmpsAll);//城市列表

//                                    Collections.sort(listEmps, new PinyinComparator());
                                    //处理数据，需要的话保存到数据库
                                    if (listEmps != null) {
                                        Collections.sort(listEmps, new PinyinComparator());
                                        DBHelper.getInstance(LocationCityActivity.this).saveCityList(listEmps);
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
                        Toast.makeText(LocationCityActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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


    private void initView() {
        keywords = (EditText) this.findViewById(R.id.keywords);
        this.findViewById(R.id.back).setOnClickListener(this);

        keywords.addTextChangedListener(new EditChangedListener());//事件监听

        lvContact = (ListView) this.findViewById(R.id.lvContact);
        adapter = new SlideCityAdapter(LocationCityActivity.this, listEmps);
        lvContact.setAdapter(adapter);
        indexBar = (SideBar) this.findViewById(R.id.sideBar);
        indexBar.setListView(lvContact);
        mDialogText = (TextView) LayoutInflater.from(LocationCityActivity.this).inflate(
                R.layout.list_position, null);
        mDialogText.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);
        indexBar.setTextView(mDialogText);
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(listEmps != null && listEmps.size()>0){
                    City bankEmpBean = listEmps.get(i);
                    if(bankEmpBean != null){
                        save("location_city", bankEmpBean.getCityName());
                        save("location_city_id", bankEmpBean.getCityid());
                    }
                }
                Intent intent1 = new Intent("update_location_success");
                sendBroadcast(intent1);
                finish();
            }
        });

        liner_location = (LinearLayout) this.findViewById(R.id.liner_location);
        mine_location = (TextView) this.findViewById(R.id.mine_location);
        liner_location.setOnClickListener(this);
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("location_city", ""), String.class))){
            //说明用户自己选择了城市
            mine_location.setText(getGson().fromJson(getSp().getString("location_city", ""), String.class));
        }else {
            if(!StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.locationCityName)){
                mine_location.setText(MeirenmeibaAppApplication.locationCityName);
            }else {
                mine_location.setText("郑州");
                liner_location.setVisibility(View.GONE);
            }
        }


    }

    class EditChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本
        private int editStart;//光标开始位置
        private int editEnd;//光标结束位置
        private final int charMaxNum = 10;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(listEmpsAll != null && listEmpsAll.size()>0 && !StringUtil.isNullOrEmpty(keywords.getText().toString())){
                listEmps.clear();
                for(City city:listEmpsAll){
                    if(city.getCityName().contains(keywords.getText().toString())){
                        listEmps.add(city);
                    }
                }
                adapter.notifyDataSetChanged();
            }

        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner_location:
            {

                save("location_city", mine_location.getText().toString());
                if(listEmps != null){
                    for(City city:listEmps){
                        if(city.getCityName().equals(mine_location.getText().toString())){
                            save("location_city_id", city.getCityid());
                            break;
                        }
                    }
                }
                Intent intent1 = new Intent("update_location_success");
                sendBroadcast(intent1);
                finish();
            }
                break;
        }
    }


}
