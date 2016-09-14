package com.lbins.myapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
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
import com.lbins.myapp.widget.CustomProgressDialog;
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
    SlideCityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_city_activity);
        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        initView();
        listEmps.addAll(DBHelper.getInstance(LocationCityActivity.this).getCityList());
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        keywords = (EditText) this.findViewById(R.id.keywords);
        this.findViewById(R.id.back).setOnClickListener(this);

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }


}
