package com.lbins.myapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.entity.LxAd;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2015/5/24.
 */
public class ShangchengAdViewPagerAdapter extends PagerAdapter {
    private ViewHolder holder;
    private OnClickContentItemListener onClickContentItemListener;
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private List<LxAd> mPaths;
    private Context mContext;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public ShangchengAdViewPagerAdapter(Context cx) {
        mContext = cx;
    }

    public void change(List<LxAd> paths) {
        mPaths = paths;
    }

    @Override
    public int getCount() {
        return mPaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == (View) obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        holder = new ViewHolder();
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_main_viewpage_xml, null);
        holder.iv = (ImageView) convertView.findViewById(R.id.item_pic);
       final LxAd cell = mPaths.get(position);
        imageLoader.displayImage((cell.getAd_pic() == null ? "" : cell.getAd_pic()), holder.iv, MeirenmeibaAppApplication.options, animateFirstListener);
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 0, cell);
            }
        });
        ((ViewPager) container).addView(convertView, 0);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    class ViewHolder {
        ImageView iv;
    }

}
