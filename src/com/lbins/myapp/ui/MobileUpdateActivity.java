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
 * 手机验证
 */
public class MobileUpdateActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText mobile;//手机号
    private Button btn_card;
    private EditText card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_update_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("设置");
        mobile = (EditText) this.findViewById(R.id.mobile);
        card = (EditText) this.findViewById(R.id.card);
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
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    showMsg(MobileUpdateActivity.this, "请输入新手机号码！");
                    recreate();
                }
            }
                break;
        }
    }

    public void updateMobileAction(View view){
        if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
            showMsg(MobileUpdateActivity.this, "请输入新手机号码！");
            return;
        }
        if(StringUtil.isNullOrEmpty(card.getText().toString())){
            showMsg(MobileUpdateActivity.this, "请输入验证码！");
            return;
        }
    }
}
