package com.lbins.myapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemFensiAdapter;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.widget.ContentListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/8/30.
 * 我的粉丝
 */
public class MineFensiActivity extends BaseActivity implements View.OnClickListener,ContentListView.OnRefreshListener,ContentListView.OnLoadListener {
    private TextView title;
    private ContentListView lstv;
    private int pageCurrent = 0;
    private boolean refresh = true;
    private ItemFensiAdapter adapter;
    List<String> lists = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_fensi_activity);
        initView();

    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("我的粉丝");
        lstv = (ContentListView) this.findViewById(R.id.lstv);
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        adapter = new ItemFensiAdapter(lists, MineFensiActivity.this);
        lstv.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onLoad() {
        pageCurrent ++;
        refresh = false;
    }

    @Override
    public void onRefresh() {
        pageCurrent = 1;
        refresh = true;
    }
}
