package com.lbins.myapp.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseFragment;
import com.lbins.myapp.ui.LoginActivity;

/**
 * Created by zhl on 2016/7/1.
 */
public class FirstFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private Resources res;

    private TextView title;
    private ImageView right_img;
    private TextView back;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.one_fragment, null);
        res = getActivity().getResources();
        initView();
        return view;
    }

    private void initView() {
        title = (TextView) view.findViewById(R.id.title);
        right_img = (ImageView) view.findViewById(R.id.right_img);
        back = (TextView) view.findViewById(R.id.back);
        back.setVisibility(View.GONE);
        right_img.setOnClickListener(this);
        right_img.setVisibility(View.VISIBLE);
        title.setText("首页");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.right_img:
                //登录
            {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
                break;
        }
    }
}
