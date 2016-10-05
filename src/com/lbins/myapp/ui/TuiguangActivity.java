package com.lbins.myapp.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.camera.util.CreateQRImageTest;

/**
 * Created by zhl on 2016/8/30.
 * 我的推广二维码 别人通过我的二维码注册  我就是他们的上级
 */
public class TuiguangActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView right_btn;
    private String urlErweima= "";

    private ImageView share_pic;//二维码
    Bitmap bitmap ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuiguang_activity);
        urlErweima = InternetURL.APP_SHARE_REG_URL+"?emp_id=" +getGson().fromJson(getSp().getString("empId", ""), String.class);
        bitmap = CreateQRImageTest.createQRImage(urlErweima);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        right_btn.setText("分享");
        right_btn.setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("我的推广");
        share_pic = (ImageView) this.findViewById(R.id.share_pic);
        if(bitmap != null){
            share_pic.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
            {
                //分享
            }
                break;
        }
    }
}
