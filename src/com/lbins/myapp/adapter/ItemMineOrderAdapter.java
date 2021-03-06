package com.lbins.myapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.entity.OrderVo;
import com.lbins.myapp.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * 我的订单
 */
public class ItemMineOrderAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<OrderVo> findEmps;
    private Context mContext;
    Resources res;

    private OnClickContentItemListener onClickContentItemListener;
    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    public ItemMineOrderAdapter(List<OrderVo> findEmps, Context mContext) {
        this.findEmps = findEmps;
        this.mContext = mContext;
        res = mContext.getResources();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mine_order_adapter, null);
            holder.item_head = (ImageView) convertView.findViewById(R.id.item_head);
            holder.item_nickname = (TextView) convertView.findViewById(R.id.item_nickname);
            holder.item_status = (TextView) convertView.findViewById(R.id.item_status);
            holder.item_pic = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.item_content = (TextView) convertView.findViewById(R.id.item_content);
            holder.item_prices = (TextView) convertView.findViewById(R.id.item_prices);
            holder.item_count = (TextView) convertView.findViewById(R.id.item_count);
            holder.item_money = (TextView) convertView.findViewById(R.id.item_money);
            holder.button_one = (TextView) convertView.findViewById(R.id.button_one);
            holder.button_two = (TextView) convertView.findViewById(R.id.button_two);
            holder.button_three = (TextView) convertView.findViewById(R.id.button_three);
            holder.button_four = (TextView) convertView.findViewById(R.id.button_four);
            holder.button_five = (TextView) convertView.findViewById(R.id.button_five);
            holder.button_six = (TextView) convertView.findViewById(R.id.button_six);
            holder.item_dateline = (TextView) convertView.findViewById(R.id.item_dateline);
            holder.button_seven = (TextView) convertView.findViewById(R.id.button_seven);//退货
            holder.button_wuliu = (TextView) convertView.findViewById(R.id.button_wuliu);//物流
            holder.relative_one = (RelativeLayout) convertView.findViewById(R.id.relative_one);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OrderVo cell = findEmps.get(position);//获得元素
        if (cell != null) {
            imageLoader.displayImage(cell.getEmpCover(), holder.item_head, MeirenmeibaAppApplication.txOptions, animateFirstListener);
            imageLoader.displayImage(cell.getGoodsCover(), holder.item_pic, MeirenmeibaAppApplication.txOptions, animateFirstListener);
            holder.item_nickname.setText(cell.getEmpName());
//            1生成订单,2支付订单,3取消订单,4作废订单,5完成订单，6物流运输中（卖家确认订单）',
//            TextView button_one;//确认收货
//            TextView button_two;//等待发货
//            TextView button_three;//去付款
//            TextView button_four;//取消订单
//            TextView button_five;//去评价
//            TextView button_six;//删除订单
//            TextView button_seven;//退货
            switch (Integer.parseInt(cell.getStatus())){
                case 1:
                    holder.item_status.setText("等待买家付款");
                    holder.button_one.setVisibility(View.GONE);
                    holder.button_two.setVisibility(View.GONE);
                    holder.button_three.setVisibility(View.VISIBLE);
                    holder.button_four.setVisibility(View.VISIBLE);
                    holder.button_five.setVisibility(View.GONE);
                    holder.button_six.setVisibility(View.GONE);
                    holder.button_seven.setVisibility(View.GONE);
                    holder.button_wuliu.setVisibility(View.GONE);
                    break;
                case 2:
                    holder.item_status.setText("等待卖家发货");
                    holder.button_one.setVisibility(View.GONE);
                    holder.button_two.setVisibility(View.GONE);
                    holder.button_three.setVisibility(View.GONE);
                    holder.button_four.setVisibility(View.GONE);
                    holder.button_five.setVisibility(View.GONE);
                    holder.button_six.setVisibility(View.GONE);
                    holder.button_wuliu.setVisibility(View.GONE);
                    holder.button_seven.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    holder.item_status.setText("交易已取消");
                    holder.button_one.setVisibility(View.GONE);
                    holder.button_two.setVisibility(View.GONE);
                    holder.button_three.setVisibility(View.GONE);
                    holder.button_four.setVisibility(View.GONE);
                    holder.button_five.setVisibility(View.GONE);
                    holder.button_six.setVisibility(View.VISIBLE);
                    holder.button_seven.setVisibility(View.GONE);
                    holder.button_wuliu.setVisibility(View.GONE);
                    break;
                case 4:
                    holder.item_status.setText("作废订单");
                    holder.button_one.setVisibility(View.GONE);
                    holder.button_two.setVisibility(View.GONE);
                    holder.button_three.setVisibility(View.GONE);
                    holder.button_four.setVisibility(View.GONE);
                    holder.button_five.setVisibility(View.GONE);
                    holder.button_six.setVisibility(View.GONE);
                    holder.button_seven.setVisibility(View.GONE);
                    holder.button_wuliu.setVisibility(View.GONE);
                    break;
                case 5:
                    holder.item_status.setText("交易完成");
                    holder.button_one.setVisibility(View.GONE);
                    holder.button_two.setVisibility(View.GONE);
                    holder.button_three.setVisibility(View.GONE);
                    holder.button_four.setVisibility(View.GONE);
                    holder.button_five.setVisibility(View.VISIBLE);
                    holder.button_six.setVisibility(View.VISIBLE);
                    holder.button_seven.setVisibility(View.GONE);
                    if("1".equals(cell.getIs_comment())){
                        holder.button_five.setVisibility(View.GONE);
                    }
                    break;
                case 6:
                    holder.item_status.setText("等待买家收货");
                    holder.button_one.setVisibility(View.VISIBLE);
                    holder.button_wuliu.setVisibility(View.VISIBLE);
                    holder.button_two.setVisibility(View.GONE);
                    holder.button_three.setVisibility(View.GONE);
                    holder.button_four.setVisibility(View.GONE);
                    holder.button_five.setVisibility(View.GONE);
                    holder.button_six.setVisibility(View.GONE);
                    holder.button_seven.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    if("0".equals(cell.getIs_return())){
                        holder.item_status.setText("退货中，等待卖家处理");
                    }else if("1".equals(cell.getIs_return())){
                        holder.item_status.setText("退货完成");
                    }
                    holder.button_one.setVisibility(View.GONE);
                    holder.button_two.setVisibility(View.GONE);
                    holder.button_three.setVisibility(View.GONE);
                    holder.button_four.setVisibility(View.GONE);
                    holder.button_five.setVisibility(View.GONE);
                    holder.button_six.setVisibility(View.GONE);
                    holder.button_seven.setVisibility(View.GONE);
                    break;
            }
            if("1".equals(cell.getIs_dxk_order())){
                //如果是0元订单
                if("1".equals(cell.getStatus())){
                    holder.button_three.setVisibility(View.VISIBLE);
                }else {
                    holder.button_three.setVisibility(View.GONE);
                }

                holder.button_seven.setVisibility(View.GONE);
                holder.relative_one.setVisibility(View.GONE);
            }

            if(StringUtil.isNullOrEmpty(cell.getGoods_id())){
                if("1".equals(cell.getStatus())){
                    holder.button_three.setVisibility(View.VISIBLE);
                }else {
                    holder.button_three.setVisibility(View.GONE);
                }
                holder.button_seven.setVisibility(View.GONE);
                holder.relative_one.setVisibility(View.GONE);
            }
            holder.item_content.setText(cell.getGoodsTitle()==null?"":cell.getGoodsTitle());
            holder.item_prices.setText(res.getString(R.string.money) +(cell.getGoodsPrice()==null?"":cell.getGoodsPrice()));
            holder.item_count.setText(String.format(res.getString(R.string.item_count_adapter),(cell.getGoods_count()==null?"0":cell.getGoods_count())));
            holder.item_money.setText(String.format(res.getString(R.string.item_money_adapter),Double.valueOf(cell.getPayable_amount()==null?"0":cell.getPayable_amount())));
            holder.item_dateline.setText(res.getString(R.string.create_time) +(cell.getCreate_time()==null?"":cell.getCreate_time()));
            holder.button_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickContentItemListener.onClickContentItem(position, 1, null);
                }
            });
            holder.button_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickContentItemListener.onClickContentItem(position, 2, null);
                }
            });
            holder.button_three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickContentItemListener.onClickContentItem(position, 3, null);
                }
            });
            holder.button_four.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickContentItemListener.onClickContentItem(position, 4, null);
                }
            });
            holder.button_five.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickContentItemListener.onClickContentItem(position, 5, null);
                }
            });
            holder.button_six.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickContentItemListener.onClickContentItem(position, 6, null);
                }
            });
            holder.button_seven.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickContentItemListener.onClickContentItem(position, 7, null);
                }
            });
            holder.button_wuliu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickContentItemListener.onClickContentItem(position, 8, null);
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        ImageView item_head;
        TextView item_nickname;
        TextView item_status;
        ImageView item_pic;
        TextView item_content;
        TextView item_prices;
        TextView item_count;
        TextView item_money;
        TextView button_one;//确认收货
        TextView button_two;//去付款
        TextView button_three;//投诉卖家
        TextView button_four;//取消订单
        TextView button_five;//pingjia
        TextView button_six;//删除订单
        TextView item_dateline;//下单时间
        TextView button_seven;//退货
        TextView button_wuliu;//物流信息
        RelativeLayout relative_one;
    }
}
