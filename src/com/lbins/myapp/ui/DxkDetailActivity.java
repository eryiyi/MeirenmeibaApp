package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.DxkViewPageAdapter;
import com.lbins.myapp.adapter.RuzhuViewPageAdapter;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.util.StringUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhl on 2016/8/30.
 * 定向卡详情
 */
public class DxkDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;

    private static final int PICTURE_COUNT = 5;

    private static final int[] PICTURE_RESOURCES = {R.drawable.dxk_icon_001,
            R.drawable.dxk_icon_002, R.drawable.dxk_icon_003, R.drawable.dxk_icon_004, R.drawable.dxk_icon_005};

    private static final String[] PICTURE_TITLE = {"第一张图片", "第二张图片", "第三张图片", "第四张图片", "第五张图片"};
    private JSONArray jsonArray;
    private ViewPager viewPager;
    private DxkViewPageAdapter adapter;
    private ImageView[] circles = new ImageView[PICTURE_RESOURCES.length];

    private Button btn_one;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_dianpu_activity);
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
        viewPager = (ViewPager) this.findViewById(R.id.viewpage);
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("定向卡介绍");
        adapter = new DxkViewPageAdapter(this, jsonArray);
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
        btn_one = (Button) this.findViewById(R.id.btn_one);
        btn_one.setText("立即加入");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    public void ruzhuAction(View view){
        Intent intent = new Intent(DxkDetailActivity.this, DxkOrderActivity.class);
        startActivity(intent);
        finish();
    }

}
