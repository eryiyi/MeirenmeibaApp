package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.lbins.myapp.MainActivity;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.util.StringUtil;

/**
 * Created by zhl on 2016/8/30.
 * 登录成功
 */
public class LoginSuccessActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView zhanghao;//成功账号
    private EditText pwr_one;
    private EditText pwr_two;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_success_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("注册");
        zhanghao = (TextView) this.findViewById(R.id.zhanghao);
        pwr_one = (EditText) this.findViewById(R.id.pwr_one);
        pwr_two = (EditText) this.findViewById(R.id.pwr_two);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    public void pwrAction(View view){
        if(StringUtil.isNullOrEmpty(pwr_one.getText().toString())){
            showMsg(LoginSuccessActivity.this, "请输入6到18位密码");
            return;
        }
        if(pwr_one.getText().toString().length() > 18 || pwr_one.getText().toString().length() < 6){
            showMsg(LoginSuccessActivity.this, "请输入6到18位密码");
            return;
        }
        if(StringUtil.isNullOrEmpty(pwr_two.getText().toString())){
            showMsg(LoginSuccessActivity.this, "请输入确认密码");
            return;
        }
        if(!pwr_one.getText().toString().equals(pwr_two.getText().toString())){
            showMsg(LoginSuccessActivity.this, "两次输入密码不一致");
            return;
        }
        Intent intent = new Intent(LoginSuccessActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
