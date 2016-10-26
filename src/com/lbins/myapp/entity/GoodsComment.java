package com.lbins.myapp.entity;

/**
 * Created by zhl on 2016/9/16.
 */
public class GoodsComment {
    private String id;//评论ID
    private String goodsId;//商品iD
    private String content;//商品内容
    private String fplid;//父评论ID
    private String empId;//评论者ID
    private String dateline;//评论时间
    private String goodsEmpId;//商品所有者ID
    private String comment_pic;//评论图片 逗号隔开 最多三图
    private String fempId;//父评论者ID
    private String nickName;//评论人昵称
    private String cover;//评论人头像
    private String fNickName;//父评论者昵称
    private String starNumber;//评论星级

    public String getStarNumber() {
        return starNumber;
    }

    public void setStarNumber(String starNumber) {
        this.starNumber = starNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFplid() {
        return fplid;
    }

    public void setFplid(String fplid) {
        this.fplid = fplid;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getGoodsEmpId() {
        return goodsEmpId;
    }

    public void setGoodsEmpId(String goodsEmpId) {
        this.goodsEmpId = goodsEmpId;
    }

    public String getFempId() {
        return fempId;
    }

    public void setFempId(String fempId) {
        this.fempId = fempId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getfNickName() {
        return fNickName;
    }

    public void setfNickName(String fNickName) {
        this.fNickName = fNickName;
    }

    public String getComment_pic() {
        return comment_pic;
    }

    public void setComment_pic(String comment_pic) {
        this.comment_pic = comment_pic;
    }
}
