package com.lbins.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.entity.GoodsComment;
import com.lbins.myapp.util.StringUtil;
import com.lbins.myapp.widget.ClassifyGridview;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/3/9
 * Time: 8:42
 * 评价 商品的
 */
public class ItemCommentAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<GoodsComment> findEmps;
    private Context mContext;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }



    public ItemCommentAdapter(List<GoodsComment> findEmps, Context mContext) {
        this.findEmps = findEmps;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return findEmps.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_goods_comment, null);
            holder.cover = (ImageView) convertView.findViewById(R.id.cover);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.dateline = (TextView) convertView.findViewById(R.id.dateline);
            holder.cont = (TextView) convertView.findViewById(R.id.cont);
            holder.lstv = (ClassifyGridview) convertView.findViewById(R.id.lstv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GoodsComment cell = findEmps.get(position);
        if (findEmps != null) {
            imageLoader.displayImage(cell.getCover(), holder.cover, MeirenmeibaAppApplication.txOptions, animateFirstListener);
            holder.name.setText(cell.getNickName());
            holder.dateline.setText(cell.getDateline());
            holder.cont.setText(cell.getContent());
        }
        return convertView;
    }

    class ViewHolder {
        ImageView cover;
        TextView name;
        TextView dateline;
        TextView cont;
        ClassifyGridview lstv;
    }
}
