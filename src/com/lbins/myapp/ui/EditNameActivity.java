package com.lbins.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.util.StringUtil;

/**
 * Created by zhl on 2016/8/30.
 */
public class EditNameActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_name_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("修改昵称");
        name = (EditText) this.findViewById(R.id.name);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    public void sureAction(View view){
        if(StringUtil.isNullOrEmpty(name.getText().toString())){
            showMsg(EditNameActivity.this ,"请输入昵称");
            return;
        }
        if(name.getText().toString().length() > 10 || name.getText().toString().length()<2){
            showMsg(EditNameActivity.this ,"请输入正确格式的昵称，2到10位之间");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("name" , name.getText().toString());
        setResult(1000 , intent);
        finish();

    }
}
