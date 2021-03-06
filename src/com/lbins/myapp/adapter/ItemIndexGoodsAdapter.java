package com.lbins.myapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.entity.PaihangObj;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 * 推荐下方的推荐商品套餐
 */
public class ItemIndexGoodsAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<PaihangObj> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemIndexGoodsAdapter(List<PaihangObj> lists, Context mContect) {
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_index_goods, null);
            holder.cover = (ImageView) convertView.findViewById(R.id.cover);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.money_one = (TextView) convertView.findViewById(R.id.money_one);
            holder.money_two = (TextView) convertView.findViewById(R.id.money_two);
            holder.sale_num = (TextView) convertView.findViewById(R.id.sale_num);
            holder.typeName = (TextView) convertView.findViewById(R.id.typeName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PaihangObj cell = lists.get(position);
        if (cell != null) {
            imageLoader.displayImage(cell.getGoods_cover(), holder.cover, MeirenmeibaAppApplication.options, animateFirstListener);
            holder.name.setText(cell.getGoods_name());
            holder.money_one.setText("￥"+cell.getSell_price());
            holder.money_two.setText("￥"+cell.getMarket_price());
            holder.money_two.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
            holder.sale_num.setText("已售:"+cell.getGoods_count_sale());
        }

        return convertView;
    }

    class ViewHolder {
        ImageView cover;
        TextView name;
        TextView money_one;
        TextView money_two;
        TextView sale_num;
        TextView typeName;
    }
}
