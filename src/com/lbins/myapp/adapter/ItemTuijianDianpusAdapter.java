package com.lbins.myapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.amap.api.maps.model.LatLng;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.entity.ManagerInfo;
import com.lbins.myapp.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 * 附近店铺
 */
public class ItemTuijianDianpusAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<ManagerInfo> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemTuijianDianpusAdapter(List<ManagerInfo> lists, Context mContect) {
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

        ManagerInfo cell = lists.get(position);
        if (cell != null) {
            imageLoader.displayImage(cell.getCompany_pic(), holder.cover, MeirenmeibaAppApplication.options, animateFirstListener);
            holder.name.setText(cell.getCompany_name());
            holder.typename.setText(cell.getType_name()==null?(cell.getLx_class_name()==null?"":cell.getLx_class_name()):cell.getType_name());
            if(!StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.latStr) && !StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.lngStr) && !StringUtil.isNullOrEmpty(cell.getLat_company())&& !StringUtil.isNullOrEmpty(cell.getLng_company()) ){
                LatLng latLng = new LatLng(Double.valueOf(MeirenmeibaAppApplication.latStr), Double.valueOf(MeirenmeibaAppApplication.lngStr));
                LatLng latLng1 = new LatLng(Double.valueOf(cell.getLat_company()), Double.valueOf(cell.getLng_company()));
                String distance = StringUtil.getDistance(latLng, latLng1);
                holder.distance_km.setText(distance + "km");
            }
            Double start_company = Double.valueOf(cell.getCompany_star() == null ? "0" : cell.getCompany_star());
            holder.txt_count.setText(start_company+"分");
            if(start_company >=0 && start_company<0.5){
                holder.icon_star.setImageDrawable(mContect.getResources().getDrawable(R.drawable.start_half));
            }
            if(start_company >=0.5 && start_company<1){
                holder.icon_star.setImageDrawable(mContect.getResources().getDrawable(R.drawable.star_one));
            }
            if(start_company >=1 && start_company<1.5){
                holder.icon_star.setImageDrawable(mContect.getResources().getDrawable(R.drawable.star_one_half));
            }
            if(start_company >=1.5 && start_company<2){
                holder.icon_star.setImageDrawable(mContect.getResources().getDrawable(R.drawable.star_two));
            }
            if(start_company >=2 && start_company<2.5){
                holder.icon_star.setImageDrawable(mContect.getResources().getDrawable(R.drawable.star_two_half));
            }
            if(start_company >=2.5 && start_company<3){
                holder.icon_star.setImageDrawable(mContect.getResources().getDrawable(R.drawable.star_three));
            }
            if(start_company >=3 && start_company<3.5){
                holder.icon_star.setImageDrawable(mContect.getResources().getDrawable(R.drawable.star_three_half));
            }
            if(start_company >=3.5 && start_company<4){
                holder.icon_star.setImageDrawable(mContect.getResources().getDrawable(R.drawable.star_four));
            }
            if(start_company >=4 && start_company<4.5){
                holder.icon_star.setImageDrawable(mContect.getResources().getDrawable(R.drawable.star_four_half));
            }
            if(start_company >=4.5 && start_company<5){
                holder.icon_star.setImageDrawable(mContect.getResources().getDrawable(R.drawable.star_five));
            }
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
