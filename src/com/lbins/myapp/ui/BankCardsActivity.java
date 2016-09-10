package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemBankCardAdapter;
import com.lbins.myapp.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/8/30.
 * 充值提现 要选择银行卡
 */

public class BankCardsActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ListView lstv;
    private ItemBankCardAdapter adapter;
    List<String> listsBank = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_card_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("选择银行卡");
        lstv = (ListView) this.findViewById(R.id.lstv);
        listsBank.add("");
        listsBank.add("");
        listsBank.add("");
        listsBank.add("");
        listsBank.add("");
        adapter = new ItemBankCardAdapter(listsBank, BankCardsActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                listsBank
                Intent intent = new Intent();
//                intent.putExtra("bankJobTaskEmp", bankJobTaskEmp);
                setResult(10001, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
