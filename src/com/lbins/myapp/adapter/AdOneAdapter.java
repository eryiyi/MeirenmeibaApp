package com.lbins.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.entity.LxAd;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * 广告一
 */
public class AdOneAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<LxAd> goodstypes;
    private Context mContext;

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public AdOneAdapter(List<LxAd> goodstypes, Context mContext) {
        this.goodstypes = goodstypes;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return goodstypes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_ad_one, parent, false);
            holder.cover = (ImageView) convertView.findViewById(R.id.cover);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final LxAd cell = goodstypes.get(position);//获得元素
        if (cell != null) {
            imageLoader.displayImage(cell.getAd_pic(), holder.cover, MeirenmeibaAppApplication.options, animateFirstListener);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView cover;
    }

}
