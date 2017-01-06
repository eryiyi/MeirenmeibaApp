package com.lbins.myapp.entity;

import java.io.Serializable;

/**
 * Created by zhl on 2017/1/6.
 */
public class LoadPic implements Serializable{
    private String load_pic_id;
    private String load_pic_title;
    private String load_pic;
    private String top_number;
    private String is_use;

    public String getLoad_pic_id() {
        return load_pic_id;
    }

    public void setLoad_pic_id(String load_pic_id) {
        this.load_pic_id = load_pic_id;
    }

    public String getLoad_pic_title() {
        return load_pic_title;
    }

    public void setLoad_pic_title(String load_pic_title) {
        this.load_pic_title = load_pic_title;
    }

    public String getLoad_pic() {
        return load_pic;
    }

    public void setLoad_pic(String load_pic) {
        this.load_pic = load_pic;
    }

    public String getTop_number() {
        return top_number;
    }

    public void setTop_number(String top_number) {
        this.top_number = top_number;
    }

    public String getIs_use() {
        return is_use;
    }

    public void setIs_use(String is_use) {
        this.is_use = is_use;
    }
}
