package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;

/**
 * Created by zhl on 2016/8/30.
 * 钱包
 */
public class MinePackageActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView name;
    private TextView mine_money;
    private TextView mine_bank_card_num_two;
    private TextView mine_bank_card_num_one;
    private TextView mine_jinbi_num;
    private ImageView cover;//头像
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_package_activity);

        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("钱包");
        cover = (ImageView) this.findViewById(R.id.cover);
        name = (TextView) this.findViewById(R.id.name);
        mine_money = (TextView) this.findViewById(R.id.mine_money);
        mine_bank_card_num_one = (TextView) this.findViewById(R.id.mine_bank_card_num_one);
        mine_bank_card_num_one = (TextView) this.findViewById(R.id.mine_bank_card_num_one);
        mine_jinbi_num = (TextView) this.findViewById(R.id.mine_jinbi_num);
        this.findViewById(R.id.liner_profile_packget).setOnClickListener(this);
        this.findViewById(R.id.liner_profile_cztx).setOnClickListener(this);
        this.findViewById(R.id.liner_profile_bank_card).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.liner_profile_packget:
            {
                //可用金币
            }
                break;
            case R.id.liner_profile_cztx:
            {
                //充值提现
                Intent intent = new Intent(MinePackageActivity.this, BankCardCztxActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.liner_profile_bank_card:
            {
                //绑定解绑银行卡
                Intent intent = new Intent(MinePackageActivity.this, BankCardDoneActivity.class);
                startActivity(intent);
            }
            break;
        }
    }
}
