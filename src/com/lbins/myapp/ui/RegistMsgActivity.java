package com.lbins.myapp.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.widget.MyTextView2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/2/18.
 * 注册协议
 */
public class RegistMsgActivity extends BaseActivity implements View.OnClickListener {
    MyTextView2 view;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_msg_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        view = (MyTextView2) findViewById(R.id.view);
        view.setText(getAssetsString(this, "reg_xieyi"));
        title = (TextView) this.findViewById(R.id.title);
        title.setText("注册协议");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    public String getAssetsString(Context context, String fileName) {
        StringBuffer sb = new StringBuffer();
        try {
            AssetManager am = context.getAssets();
            InputStream in = am.open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                line += ("\n");
                sb.append(line);
            }
            reader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
