/**
 *
 */
package com.lbins.myapp.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import org.json.JSONObject;

public class ViewPageItemView extends FrameLayout {

    /**
     * 图片的ImageView
     */
    private ImageView imageView;
    /**
     * 图片的名称
     */
    private TextView tvTitle;

    /**
     * 图片的Bitmap
     */
    private Bitmap bitmap;

    /**
     * 要显示图片的JsonObject
     */
    private JSONObject jsonObject;

    public ViewPageItemView(Context context) {
        super(context);

        initView(context);
    }

    public ViewPageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    public ViewPageItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initView(context);
    }

    // 初始控件
    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.viewpage_itemview, null);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        addView(view);
    }

    // 填充数据
    public void setData(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        String resourceId = jsonObject.optString("resourceId");

        if(imageView != null){
            imageLoader.displayImage(resourceId, imageView, MeirenmeibaAppApplication.options, animateFirstListener);
        }

    }
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类


    // 资源回收
    public void recycle() {
        if(imageView != null){
            imageView.setImageBitmap(null);
        }

        if (null == bitmap || bitmap.isRecycled()) {
            return;
        }

        bitmap.recycle();
        bitmap = null;
    }

    // 重新加载资源
    public void reload() {
        String resourceId = jsonObject.optString("resourceId");
        if(imageView != null){
            imageLoader.displayImage(resourceId, imageView, MeirenmeibaAppApplication.options, animateFirstListener);
        }
    }

}
