package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;

/**
 * Created by zhl on 2016/8/30.
 * 设置
 */
public class SetActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("设置");
        this.findViewById(R.id.liner_set_profile).setOnClickListener(this);
        this.findViewById(R.id.liner_set_pwr).setOnClickListener(this);
        this.findViewById(R.id.liner_set_address).setOnClickListener(this);
        this.findViewById(R.id.liner_set_liulan).setOnClickListener(this);
        this.findViewById(R.id.liner_set_version).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner_set_profile:
            {
                //个人资料
                Intent intent = new Intent(SetActivity.this, SetProfileActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.liner_set_pwr:
            {
                //密码
                Intent pwrV = new Intent(SetActivity.this, SetPwrSelectActivity.class);
                startActivity(pwrV);
            }
            break;
            case R.id.liner_set_address:
            {
                //地址
                Intent pwrV = new Intent(SetActivity.this, MineAddressActivity.class);
                startActivity(pwrV);
            }
            break;
            case R.id.liner_set_liulan:
            {
                //浏览
            }
            break;
            case R.id.liner_set_version:
            {
                //版本
            }
            break;
        }
    }
}
