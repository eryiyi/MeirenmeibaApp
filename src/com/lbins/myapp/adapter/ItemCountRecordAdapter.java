package com.lbins.myapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.entity.CountRecord;
import com.lbins.myapp.util.DateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 * 我的积分记录
 */
public class ItemCountRecordAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<CountRecord> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemCountRecordAdapter(List<CountRecord> lists, Context mContect) {
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_count_record, null);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            holder.dateline = (TextView) convertView.findViewById(R.id.dateline);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CountRecord cell = lists.get(position);
        if (cell != null) {
            holder.content.setText(cell.getLx_count_record_cont()==null?"":cell.getLx_count_record_cont());
            holder.count.setText("获得积分:"+(cell.getLx_count_record_count()==null?"":cell.getLx_count_record_count()));
            holder.dateline.setText("时间:"+(DateUtil.getDate(cell.getDateline()==null?"":cell.getDateline(),"yyyy-MM-dd HH:mm")));
        }

        return convertView;
    }

    class ViewHolder {
        TextView content;
        TextView count;
        TextView dateline;
    }
}
