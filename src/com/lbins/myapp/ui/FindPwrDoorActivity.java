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
 * 找回密码  两种方式
 */
public class FindPwrDoorActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView cityName;//城市名
    private EditText shanghuming;//商户名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_pwr_door_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("通过门店找回");
        cityName = (TextView) this.findViewById(R.id.cityName);
        cityName.setOnClickListener(this);
        shanghuming = (EditText) this.findViewById(R.id.shanghuming);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.cityName:
            {
                //城市名
            }
                break;
        }
    }

    public void searchAction(View view){
        //搜索
        if(StringUtil.isNullOrEmpty(shanghuming.getText().toString())){
            showMsg(FindPwrDoorActivity.this, "请输入商户名！");
            return;
        }

    }
}
