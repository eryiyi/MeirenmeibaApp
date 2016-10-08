package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.entity.BankObj;
import com.lbins.myapp.util.StringUtil;

/**
 * Created by zhl on 2016/8/30.
 * 充值提现
 */
public class BankCardCztxActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView btn_one;
    private TextView btn_two;
    private TextView money_count;//余额
    private EditText money_cztx;//充值金额
    private TextView ban_card;//选择的银行卡
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_card_cztx_activity);

        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("充值");
        btn_one = (TextView) this.findViewById(R.id.btn_one);
        btn_two = (TextView) this.findViewById(R.id.btn_two);
        money_count = (TextView) this.findViewById(R.id.money_count);
        ban_card = (TextView) this.findViewById(R.id.ban_card);
        money_cztx = (EditText) this.findViewById(R.id.money_cztx);


        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        this.findViewById(R.id.liner_card).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_one:
            {
                //绑定
                btn_one.setTextColor(getResources().getColor(R.color.red));
                btn_two.setTextColor(getResources().getColor(R.color.text_color));
            }
            break;
            case R.id.btn_two:
            {
                //解绑
                btn_one.setTextColor(getResources().getColor(R.color.text_color));
                btn_two.setTextColor(getResources().getColor(R.color.red));
            }
            break;
            case R.id.liner_card:
            {
                //选择银行卡
                Intent intent = new Intent(BankCardCztxActivity.this, BankCardsActivity.class);
                startActivityForResult(intent, 1000);
            }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == 10001 ){
            BankObj bankObj = (BankObj) data.getExtras().get("bankObj");
        }
    }

    public void cztxAction(View view){
        if(StringUtil.isNullOrEmpty(money_cztx.getText().toString())){
            showMsg(BankCardCztxActivity.this, "请输入充值/提现金额！");
            return;
        }
    }
}
