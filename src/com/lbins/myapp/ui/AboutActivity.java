package com.lbins.myapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.lbins.myapp.MainActivity;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.AboutViewPageAdapter;
import com.lbins.myapp.base.BaseActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AboutActivity extends BaseActivity implements View.OnClickListener{
    private JSONArray jsonArray = new JSONArray();;
    private ViewPager viewPager;
    private AboutViewPageAdapter adapter;
    ArrayList<String> pics = new ArrayList<String>();
    private TextView skip;
    private Resources res;
    MyTimer myTimer = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_viewpage);
        pics = getIntent().getExtras().getStringArrayList("picsStr");
        res = getResources();

        for (int i = 0; i < pics.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("resourceId", pics.get(i));
                jsonObject.put("title", pics.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        initView();

        boolean isFirstRun = getSp().getBoolean("isFirstRun", true);
        if (isFirstRun) {
            //第一次执行
            SharedPreferences.Editor editor = getSp().edit();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        } else {
            //不是第一次执行
            myTimer = new MyTimer(6000, 1000);
            myTimer.start();
        }
    }

    class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            skip.setText("跳过");
            Intent main = new Intent(AboutActivity.this, MainActivity.class);
            startActivity(main);
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            skip.setText("跳过" + millisUntilFinished / 1000 + "s");
        }
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpage);
        adapter = new AboutViewPageAdapter(AboutActivity.this, jsonArray);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        skip = (TextView) this.findViewById(R.id.skip);
        skip.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.skip:
            {
                if(myTimer != null){
                    myTimer.cancel();
                    myTimer.onFinish();
                }
                Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(intent);
            }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
