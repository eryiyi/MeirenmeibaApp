package com.lbins.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.lbins.myapp.R;
import com.lbins.myapp.entity.LxClass;

import java.util.List;

/**
 * author: zhanghl
 * Date: 2014/8/9
 * Time: 11:07
 */
public class ItemLxClassAdapter extends BaseAdapter {
    private ViewHolder holder;
    private Context context;
    private List<LxClass> list;
    private LayoutInflater inflater;


    public ItemLxClassAdapter(Context context, List<LxClass> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_cp_guige, null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final LxClass commentContent = list.get(position);
        if (commentContent != null) {
            holder.title.setText(commentContent.getLx_class_name());
        }
        return convertView;
    }

    class ViewHolder {
        TextView title;
    }
}
