package com.lbins.myapp.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseFragment;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhl on 2016/7/1.
 */
public class SecondFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private Resources res;

    private TextView title;
    private TextView right_btn;
    private TextView back;
    private TextView daily_count;
    private TextView xiashuCount;
    private TextView comment_count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.two_fragment, null);
        res = getActivity().getResources();
        initView();

        return view;
    }

    private void initView() {
        title = (TextView) view.findViewById(R.id.title);
        right_btn = (TextView) view.findViewById(R.id.right_btn);
        back = (TextView) view.findViewById(R.id.back);
        back.setVisibility(View.GONE);
        right_btn.setOnClickListener(this);
        right_btn.setText("添加");
        title.setText("日报目录");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

}