package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.util.StringUtil;

/**
 * Created by zhl on 2016/8/30.
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private Button btn_card;
    private EditText mobile;
    private EditText card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initView();
    }

    private void initView() {
        btn_card = (Button) this.findViewById(R.id.btn_card);
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("手机快速登录");
        mobile = (EditText) this.findViewById(R.id.mobile);
        card = (EditText) this.findViewById(R.id.card);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    public void loginAction(View view){
        //登录
        if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
            showMsg(LoginActivity.this, "请输入手机号！");
            return;
        }
        if(StringUtil.isNullOrEmpty(card.getText().toString())){
            showMsg(LoginActivity.this, "请输入验证码！");
            return;
        }
        Intent intent = new Intent(LoginActivity.this, LoginSuccessActivity.class);
        startActivity(intent);
    }

    public void findAction(View view){
        //找回账号密码
        Intent intent = new Intent(LoginActivity.this, FindPwrSelectActivity.class);
        startActivity(intent);
    }
}
