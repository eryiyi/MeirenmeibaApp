package com.lbins.myapp.entity;

/**
 * Created by zhl on 2016/9/13.
 * 商品
 */
public class PaopaoGoods {
    private String id;
    private String typeId;
    private String name;
    private String cover;
    private String cont;
    private String sellPrice;//销售价格
    private String marketPrice;//市场价格
    private String address;
    private String person;//联系人
    private String tel;
    private String qq;

    private String isUse;
    private String isDel;
    private String upTime;//上架时间
    private String count;//商品数量
    private String goods_count_sale;//已卖商品数量
    private String goodsPosition;
    private String is_zhiying;//0 商家发布 1自营

    private String empId;//商家商品发布者
    private String manager_id;//自营商品发布者


    private String nickName;
    private String empCover;

    private String managerName;
    private String managerCover;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getGoods_count_sale() {
        return goods_count_sale;
    }

    public void setGoods_count_sale(String goods_count_sale) {
        this.goods_count_sale = goods_count_sale;
    }

    public String getGoodsPosition() {
        return goodsPosition;
    }

    public void setGoodsPosition(String goodsPosition) {
        this.goodsPosition = goodsPosition;
    }

    public String getIs_zhiying() {
        return is_zhiying;
    }

    public void setIs_zhiying(String is_zhiying) {
        this.is_zhiying = is_zhiying;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getManager_id() {
        return manager_id;
    }

    public void setManager_id(String manager_id) {
        this.manager_id = manager_id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmpCover() {
        return empCover;
    }

    public void setEmpCover(String empCover) {
        this.empCover = empCover;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerCover() {
        return managerCover;
    }

    public void setManagerCover(String managerCover) {
        this.managerCover = managerCover;
    }
}
