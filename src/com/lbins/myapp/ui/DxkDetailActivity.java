package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;

/**
 * Created by zhl on 2016/8/30.
 * 定向卡详情
 */
public class DxkDetailActivity extends BaseActivity implements View.OnClickListener {
//    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dxk_detail_activity);
        initView();
    }

    private void initView() {
//        this.findViewById(R.id.back).setOnClickListener(this);
//        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
//        title = (TextView) this.findViewById(R.id.title);
//        title.setText("定向卡详情");
//        this.findViewById(R.id.btn_dxk).setOnClickListener(this);
        this.findViewById(R.id.btn_click).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_click:
            {
                Intent intent = new Intent(DxkDetailActivity.this, DxkOrderActivity.class);
                startActivity(intent);
                finish();
            }
                break;
        }
    }

    public void dxkAction(View v){
        Intent intent = new Intent(DxkDetailActivity.this, DxkOrderActivity.class);
        startActivity(intent);
    }
}
