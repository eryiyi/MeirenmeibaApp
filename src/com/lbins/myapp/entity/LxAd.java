package com.lbins.myapp.entity;

/**
 * Created by zhl on 2016/9/17.
 */
public class LxAd {
    private String ad_id;
    private String ad_type;//广告类别  1推荐顶部轮播图  2推荐中部广告（大） 3 推荐中部广告（小） 4 商城顶部轮播图  5 商城首发新品 6 商城特惠专区
    private String ad_pic;
    private String ad_url_type;//1商品详情(默认)   2商店详情
    private String ad_emp_id;//要跳转的用户id
    private String ad_msg_id;//要跳转的商品（默认）   或商城id
    private String top_num;//排序
    private String dateline;

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public String getAd_pic() {
        return ad_pic;
    }

    public void setAd_pic(String ad_pic) {
        this.ad_pic = ad_pic;
    }

    public String getAd_url_type() {
        return ad_url_type;
    }

    public void setAd_url_type(String ad_url_type) {
        this.ad_url_type = ad_url_type;
    }

    public String getAd_emp_id() {
        return ad_emp_id;
    }

    public void setAd_emp_id(String ad_emp_id) {
        this.ad_emp_id = ad_emp_id;
    }

    public String getAd_msg_id() {
        return ad_msg_id;
    }

    public void setAd_msg_id(String ad_msg_id) {
        this.ad_msg_id = ad_msg_id;
    }

    public String getTop_num() {
        return top_num;
    }

    public void setTop_num(String top_num) {
        this.top_num = top_num;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }
}
