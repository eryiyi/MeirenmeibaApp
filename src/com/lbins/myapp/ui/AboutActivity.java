package com.lbins.myapp.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.ImageView;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.AboutViewPageAdapter;
import com.lbins.myapp.base.BaseActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AboutActivity extends BaseActivity {
    private static final int PICTURE_COUNT = 4;

    private static final int[] PICTURE_RESOURCES = {R.drawable.loading_one,
            R.drawable.loading_two, R.drawable.loading_three, R.drawable.loading_four};

    private static final String[] PICTURE_TITLE = {"第一张图片", "第二张图片", "第三张图片","第四张图片"};
    private JSONArray jsonArray;
    private ViewPager viewPager;
    private AboutViewPageAdapter adapter;
    private ImageView[] circles = new ImageView[PICTURE_RESOURCES.length];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_viewpage);
        initLoadData();
        initView();
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
    }
}
