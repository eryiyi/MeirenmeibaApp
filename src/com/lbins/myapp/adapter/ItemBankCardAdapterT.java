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
import com.lbins.myapp.entity.BankObj;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemBankCardAdapterT extends BaseAdapter {
    private ViewHolder holder;
    private List<BankObj> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemBankCardAdapterT(List<BankObj> lists, Context mContect) {
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_bank_card, null);
            holder.btn_select = (ImageView) convertView.findViewById(R.id.btn_select);
            holder.card = (TextView) convertView.findViewById(R.id.card);
            holder.bank_name = (TextView) convertView.findViewById(R.id.bank_name);
            holder.bank_kaihu_name = (TextView) convertView.findViewById(R.id.bank_kaihu_name);
            holder.bank_mobile = (TextView) convertView.findViewById(R.id.bank_mobile);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankObj cell = lists.get(position);
        if (cell != null) {
            holder.card.setText(cell.getBank_card()==null?"":cell.getBank_card());
            holder.bank_name.setText(cell.getBank_name()==null?"":cell.getBank_name());
            holder.bank_kaihu_name.setText(cell.getBank_kaihu_name()==null?"":cell.getBank_kaihu_name());
            holder.bank_mobile.setText(cell.getBank_mobile()==null?"":cell.getBank_mobile());

//            holder.btn_select.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onClickContentItemListener.onClickContentItem(position, 1, null);
//                }
//            });
        }

        return convertView;
    }

    class ViewHolder {
        ImageView btn_select;
        TextView card;
        TextView bank_name;
        TextView bank_kaihu_name;
        TextView bank_mobile;
    }
}
