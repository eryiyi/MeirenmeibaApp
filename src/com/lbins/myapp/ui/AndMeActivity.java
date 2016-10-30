package com.lbins.myapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;

/**
 * Created by zhl on 2016/8/30.
 * 与我相关
 */
public class AndMeActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.and_me_activity);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("与我相关");
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
