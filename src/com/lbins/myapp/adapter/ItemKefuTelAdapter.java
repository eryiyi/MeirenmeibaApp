package com.lbins.myapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.entity.KefuTel;
import com.lbins.myapp.entity.ShoppingAddress;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/6
 * Time: 14:06
 */
public class ItemKefuTelAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<KefuTel> lists;
    private Context mContext;
    Resources res;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public ItemKefuTelAdapter(List<KefuTel> lists, Context mContext) {
        this.lists = lists;
        this.mContext = mContext;
        res = mContext.getResources();
    }

    @Override
    public int getCount() {
        return lists.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_kefu_tel_adapter, null);
            holder.cover = (ImageView) convertView.findViewById(R.id.cover);
            holder.name = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final KefuTel favour = lists.get(position);
        if (favour != null) {
            holder.name.setText(favour.getMm_name());
            imageLoader.displayImage(favour.getMm_cover(), holder.cover, MeirenmeibaAppApplication.txOptions, animateFirstListener);

        }

        return convertView;
    }

    class ViewHolder {
        ImageView cover;
        TextView name;
    }

}