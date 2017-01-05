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
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.SlideCityAdapter;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.db.DBHelper;
import com.lbins.myapp.entity.City;
import com.lbins.myapp.pinyin.SideBar;
import com.lbins.myapp.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_city_activity);
        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        initView();
        listEmpsAll.addAll(DBHelper.getInstance(LocationCityActivity.this).getCityList());//全部城市
        listEmps.addAll(listEmpsAll);//城市列表
        adapter.notifyDataSetChanged();
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
        }
    }


}
