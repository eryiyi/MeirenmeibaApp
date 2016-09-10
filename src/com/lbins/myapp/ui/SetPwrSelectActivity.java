package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;

/**
 * Created by zhl on 2016/8/30.
 * 修改密码选择
 */
public class SetPwrSelectActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_pwr_select_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("设置");
        this.findViewById(R.id.liner_set_pwr_one).setOnClickListener(this);
        this.findViewById(R.id.liner_set_pwr_two).setOnClickListener(this);
        this.findViewById(R.id.liner_set_pwr_three).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner_set_pwr_one:
            {
                //登录密码
                Intent intent = new Intent(SetPwrSelectActivity.this, UpdateLoginPwrActivity.class);
                startActivity(intent);
            }
              break;
            case R.id.liner_set_pwr_two:
            {
                //手机验证
                Intent intent = new Intent(SetPwrSelectActivity.this, MobileUpdateActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.liner_set_pwr_three:
            {
                //支付密码
                Intent intent = new Intent(SetPwrSelectActivity.this, UpdatePayPwrActivity.class);
                startActivity(intent);
            }
            break;
        }
    }
}
