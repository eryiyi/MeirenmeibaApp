package com.lbins.myapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;

/**
 * Created by zhl on 2016/8/30.
 * 设置个人资料
 */
public class SetProfileActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ImageView mine_cover;//头像
    private TextView name;//用户名
    private TextView nick;//
    private TextView sex;//
    private TextView birth;//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_profile_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("设置");
        mine_cover = (ImageView) this.findViewById(R.id.mine_cover);
        name = (TextView) this.findViewById(R.id.name);
        nick = (TextView) this.findViewById(R.id.nick);
        sex = (TextView) this.findViewById(R.id.sex);
        birth = (TextView) this.findViewById(R.id.birth);

        mine_cover.setOnClickListener(this);
        this.findViewById(R.id.liner_set_profile_name).setOnClickListener(this);
        this.findViewById(R.id.liner_set_profile_nick).setOnClickListener(this);
        this.findViewById(R.id.liner_set_profile_sex).setOnClickListener(this);
        this.findViewById(R.id.liner_set_profile_birth).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.mine_cover:
            {
                //头像点击
            }
                break;
            case R.id.liner_set_profile_name:
            {
                //用户名点击
            }
                break;
            case R.id.liner_set_profile_nick:
            {
                //昵称点击
            }
            break;
            case R.id.liner_set_profile_sex:
            {
                //性别点击
            }
            break;
            case R.id.liner_set_profile_birth:
            {
                //生日
            }
            break;
        }
    }
}
