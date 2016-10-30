package com.lbins.myapp.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.*;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;

/**
 * Created by zhl on 2016/8/30.
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;

    private WebView detail_webview;
    private String strurl;

    private static final String APP_CACAHE_DIRNAME = "/webcache";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        strurl = getIntent().getExtras().getString("strurl");

        initView();

        detail_webview.setInitialScale(35);
        detail_webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        detail_webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        detail_webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式
        // 开启 DOM storage API 功能
        detail_webview.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        detail_webview.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME;
//      String cacheDirPath = getCacheDir()getCacheDir.getAbsolutePath()+Constant.APP_DB_DIRNAME;
        //设置数据库缓存路径
        detail_webview.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        detail_webview.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        detail_webview.getSettings().setAppCacheEnabled(true);
        detail_webview.getSettings().setJavaScriptEnabled(true);
        detail_webview.requestFocus();
        detail_webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, true);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });


        detail_webview.loadUrl(strurl);
        detail_webview.setWebViewClient(new HelloWebViewClient());

    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("美人美吧");

        detail_webview = (WebView) this.findViewById(R.id.detail_webview);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && detail_webview.canGoBack()) {
            detail_webview.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            detail_webview.loadData("", "text/html; charset=UTF-8", null);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause ()
    {
        detail_webview.reload ();
        super.onPause ();
    }

}
