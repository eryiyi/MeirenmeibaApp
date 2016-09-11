package com.lbins.myapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 * 推荐店铺
 */
public class ItemTuijianDianpusAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<String> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemTuijianDianpusAdapter(List<String> lists, Context mContect) {
        this.lists = lists;
        this.mContect = mContect;
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
        res = mContect.getResources();
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_tuijian_dianpu, null);
            holder.cover = (ImageView) convertView.findViewById(R.id.cover);
            holder.icon_type = (TextView) convertView.findViewById(R.id.icon_type);
            holder.icon_star = (ImageView) convertView.findViewById(R.id.icon_star);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.typename = (TextView) convertView.findViewById(R.id.typename);
            holder.distance_km = (TextView) convertView.findViewById(R.id.distance_km);
            holder.txt_count = (TextView) convertView.findViewById(R.id.txt_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String cell = lists.get(position);
        if (cell != null) {
        }

        return convertView;
    }

    class ViewHolder {
        ImageView cover;
        TextView icon_type;
        ImageView icon_star;
        TextView name;
        TextView typename;
        TextView distance_km;
        TextView txt_count;
    }
}
