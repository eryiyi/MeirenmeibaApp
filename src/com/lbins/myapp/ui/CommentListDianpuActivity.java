package com.lbins.myapp.ui;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.ItemCommentAdapter;
import com.lbins.myapp.adapter.ItemDianpuCommentAdapter;
import com.lbins.myapp.adapter.OnClickContentItemListener;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.DianpuCommentData;
import com.lbins.myapp.data.GoodsCommentData;
import com.lbins.myapp.entity.DianpuComment;
import com.lbins.myapp.entity.GoodsComment;
import com.lbins.myapp.library.PullToRefreshBase;
import com.lbins.myapp.library.PullToRefreshListView;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.CustomProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2016/8/30.
 * 店铺评论列表
 */
public class CommentListDianpuActivity extends BaseActivity implements View.OnClickListener ,OnClickContentItemListener{
    private TextView title;
    private ImageView no_result;
    private PullToRefreshListView lstv;
    private ItemDianpuCommentAdapter adapterComment;
    private List<DianpuComment> listComments = new ArrayList<DianpuComment>();
    private boolean IS_REFRESH = true;
    private int pageIndex = 1;

    private String emp_id_seller;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_list_goods_activity);
        emp_id_seller = getIntent().getExtras().getString("emp_id_seller");
        initView();
        progressDialog = new CustomProgressDialog(CommentListDianpuActivity.this, "",R.anim.custom_dialog_frame);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        getComment();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("店铺评论列表");
        no_result = (ImageView) this.findViewById(R.id.no_result);
        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);


        adapterComment = new ItemDianpuCommentAdapter(listComments, CommentListDianpuActivity.this);
        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                getComment();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                getComment();
            }
        });
        lstv.setAdapter(adapterComment);
        adapterComment.setOnClickContentItemListener(this);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    //获得评论
    void getComment(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.appGetDianpuComment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code = jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    DianpuCommentData data = getGson().fromJson(s, DianpuCommentData.class);
                                    if(IS_REFRESH ){
                                        listComments.clear();
                                    }
                                    listComments.addAll(data.getData());
                                    lstv.onRefreshComplete();
                                    adapterComment.notifyDataSetChanged();
                                    if(listComments.size() == 0){
                                        no_result.setVisibility(View.VISIBLE);
                                        lstv.setVisibility(View.GONE);
                                    }else {
                                        no_result.setVisibility(View.GONE);
                                        lstv.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Toast.makeText(CommentListDianpuActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(CommentListDianpuActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id_seller", emp_id_seller);
                params.put("page", String.valueOf(pageIndex));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
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
    public void onClickContentItem(int position, int flag, Object object) {

    }
}
