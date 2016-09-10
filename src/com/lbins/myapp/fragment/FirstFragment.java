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
import com.lbins.myapp.adapter.ItemIndexGoodsAdapter;
import com.lbins.myapp.base.BaseFragment;
import com.lbins.myapp.ui.LoginActivity;
import com.lbins.myapp.widget.ContentListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/7/1.
 */
public class FirstFragment extends BaseFragment implements View.OnClickListener,ContentListView.OnRefreshListener,ContentListView.OnLoadListener {
    private View view;
    private Resources res;

    private TextView location;
    private ImageView btn_scan;
    private TextView keywords;

    private ContentListView lstv;
    private ItemIndexGoodsAdapter adapter;
    List<String> listsgoods = new ArrayList<String>();
    private View headerLine;
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
        location = (TextView) view.findViewById(R.id.location);
        btn_scan = (ImageView) view.findViewById(R.id.btn_scan);
        keywords = (TextView) view.findViewById(R.id.keywords);
        lstv = (ContentListView) view.findViewById(R.id.lstv);
//        headerLine =
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        listsgoods.add("");
        adapter = new ItemIndexGoodsAdapter(listsgoods, getActivity());
        lstv.setAdapter(adapter);
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

    @Override
    public void onLoad() {

    }

    @Override
    public void onRefresh() {

    }
}
