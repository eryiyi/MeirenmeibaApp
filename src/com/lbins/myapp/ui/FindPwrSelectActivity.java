package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;

/**
 * Created by zhl on 2016/8/30.
 * 找回密码  两种方式
 */
public class FindPwrSelectActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_pwr_select_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("找回账号/密码");
        this.findViewById(R.id.liner_door).setOnClickListener(this);
        this.findViewById(R.id.liner_mobile).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner_door:
            {
                Intent intent = new Intent(FindPwrSelectActivity.this, FindPwrDoorActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_mobile:
            {
                Intent intent = new Intent(FindPwrSelectActivity.this, FindPwrMobileActivity.class);
                startActivity(intent);
            }
                break;

        }
    }
}
