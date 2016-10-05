package com.lbins.myapp.entity;

/**
 * Created by zhanghailong on 2016/3/4.
 */
public class KefuTel {
    private String mm_tel_id;//唯一标志
    private String mm_tel;//电话
    private String mm_name;//昵称
    private String mm_cover;//头像
    private String mm_tel_type;//暂无用处
    private String top_num;//置顶 后天查询排序用

    public String getTop_num() {
        return top_num;
    }

    public void setTop_num(String top_num) {
        this.top_num = top_num;
    }

    public String getMm_tel_id() {
        return mm_tel_id;
    }

    public void setMm_tel_id(String mm_tel_id) {
        this.mm_tel_id = mm_tel_id;
    }

    public String getMm_tel() {
        return mm_tel;
    }

    public void setMm_tel(String mm_tel) {
        this.mm_tel = mm_tel;
    }

    public String getMm_name() {
        return mm_name;
    }

    public void setMm_name(String mm_name) {
        this.mm_name = mm_name;
    }

    public String getMm_cover() {
        return mm_cover;
    }

    public void setMm_cover(String mm_cover) {
        this.mm_cover = mm_cover;
    }

    public String getMm_tel_type() {
        return mm_tel_type;
    }

    public void setMm_tel_type(String mm_tel_type) {
        this.mm_tel_type = mm_tel_type;
    }
}
