package com.lbins.myapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.util.StringUtil;

/**
 * Created by zhl on 2016/8/30.
 * 支付密码
 */
public class UpdatePayPwrActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText pwr_two;
    private EditText pwr_three;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pay_pwr_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("设置");
        pwr_two = (EditText) this.findViewById(R.id.pwr_two);
        pwr_three = (EditText) this.findViewById(R.id.pwr_three);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
    public void updatePayPwrAction(View view){
        if(StringUtil.isNullOrEmpty(pwr_two.getText().toString())){
            showMsg(UpdatePayPwrActivity.this, "请输入支付密码");
            return;
        }
        if(StringUtil.isNullOrEmpty(pwr_three.getText().toString())){
            showMsg(UpdatePayPwrActivity.this, "请输入确认密码");
            return;
        }

        if(!pwr_three.getText().toString().equals(pwr_two.getText().toString())){
            showMsg(UpdatePayPwrActivity.this, "两次输入密码不一致！！");
            return;
        }
    }
}
