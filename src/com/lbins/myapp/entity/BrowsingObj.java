package com.lbins.myapp.entity;

/**
 * Created by zhl on 2016/9/24.
 * 用户浏览记录
 */
public class BrowsingObj {
    private String browsingid;
    private String empid;
    private String goodsid;
    private String dateline;
    private String goods_name;
    private String goods_cover;
    private String emp_id_dianpu;

    public String getEmp_id_dianpu() {
        return emp_id_dianpu;
    }

    public void setEmp_id_dianpu(String emp_id_dianpu) {
        this.emp_id_dianpu = emp_id_dianpu;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_cover() {
        return goods_cover;
    }

    public void setGoods_cover(String goods_cover) {
        this.goods_cover = goods_cover;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getBrowsingid() {
        return browsingid;
    }

    public void setBrowsingid(String browsingid) {
        this.browsingid = browsingid;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }
}
