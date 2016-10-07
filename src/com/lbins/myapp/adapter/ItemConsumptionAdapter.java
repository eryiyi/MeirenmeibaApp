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
import com.lbins.myapp.entity.DianPuFavour;
import com.lbins.myapp.entity.LxConsumption;
import com.lbins.myapp.util.DateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 * 我的消费记录
 */
public class ItemConsumptionAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<LxConsumption> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemConsumptionAdapter(List<LxConsumption> lists, Context mContect) {
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_onsumption, null);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            holder.dateline = (TextView) convertView.findViewById(R.id.dateline);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LxConsumption cell = lists.get(position);
        if (cell != null) {
            holder.content.setText(cell.getLx_consumption_cont()==null?"":cell.getLx_consumption_cont());
            holder.count.setText("金额:"+(cell.getLx_consumption_count()==null?"":cell.getLx_consumption_count()));
            holder.dateline.setText("时间"+(DateUtil.getDate(cell.getDateline()==null?"":cell.getDateline(),"yyyy-MM-dd HH:mm")));
        }

        return convertView;
    }

    class ViewHolder {
        TextView content;
        TextView count;
        TextView dateline;
    }
}
