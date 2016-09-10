package com.lbins.myapp.ui;

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
 */
public class FindPwrMobileActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText mobile;
    private EditText card;
    private EditText pwr_one;
    private EditText pwr_two;
    private Button btn_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_pwr_mobile_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("通过手机号找回密码");
        mobile = (EditText) this.findViewById(R.id.mobile);
        card = (EditText) this.findViewById(R.id.card);
        pwr_one = (EditText) this.findViewById(R.id.pwr_one);
        pwr_two = (EditText) this.findViewById(R.id.pwr_two);
        btn_card = (Button) this.findViewById(R.id.btn_card);
        btn_card.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_card:
            {
                //获得验证码
            }
                break;
        }
    }
    public void findPwrAction(View view){
        if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
            showMsg(FindPwrMobileActivity.this, "请输入手机号");
            return;
        }
        if(StringUtil.isNullOrEmpty(card.getText().toString())){
            showMsg(FindPwrMobileActivity.this, "请输入验证码");
            return;
        }
        if(StringUtil.isNullOrEmpty(pwr_one.getText().toString())){
            showMsg(FindPwrMobileActivity.this, "请输入6到18位密码");
            return;
        }
        if(pwr_one.getText().toString().length() > 18 || pwr_one.getText().toString().length() < 6){
            showMsg(FindPwrMobileActivity.this, "请输入6到18位密码");
            return;
        }
        if(StringUtil.isNullOrEmpty(pwr_two.getText().toString())){
            showMsg(FindPwrMobileActivity.this, "请输入确认密码");
            return;
        }
        if(!pwr_one.getText().toString().equals(pwr_two.getText().toString())){
            showMsg(FindPwrMobileActivity.this, "两次输入密码不一致");
            return;
        }
    }
}
