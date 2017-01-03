package com.lbins.myapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.myapp.MainActivity;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.AboutViewPageAdapter;
import com.lbins.myapp.base.BaseActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class AboutActivity extends BaseActivity implements View.OnClickListener{
    private static final int PICTURE_COUNT = 4;

    private static final int[] PICTURE_RESOURCES = {R.drawable.loading_one,
            R.drawable.loading_two, R.drawable.loading_three, R.drawable.loading_four};

    private static final String[] PICTURE_TITLE = {"第一张图片", "第二张图片", "第三张图片","第四张图片"};
    private JSONArray jsonArray;
    private ViewPager viewPager;
    private AboutViewPageAdapter adapter;
    private ImageView[] circles = new ImageView[PICTURE_RESOURCES.length];

    private TextView skip;
    private Resources res;
    MyTimer myTimer = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_viewpage);
        res = getResources();

        initLoadData();
        initView();

        boolean isFirstRun = getSp().getBoolean("isFirstRun", true);
            if (isFirstRun) {
                //第一次执行
                SharedPreferences.Editor editor = getSp().edit();
                editor.putBoolean("isFirstRun", false);
                editor.commit();
            } else {
                //不是第一次执行
                myTimer = new MyTimer(5000, 1000);
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



    private void initLoadData() {
        jsonArray = new JSONArray();
        for (int i = 0; i < PICTURE_COUNT; i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("resourceId", PICTURE_RESOURCES[i]);
                jsonObject.put("title", PICTURE_TITLE[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
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
                for (int j = 0; j < circles.length; j++) {
                }
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
                myTimer.cancel();
                myTimer.onFinish();
            }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
