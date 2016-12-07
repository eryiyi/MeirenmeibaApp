package com.lbins.myapp.entity;

/**
 * Created by zhl on 2016/11/30.
 */
public class LxClass {
    private String lx_class_id;
    private String lx_class_name;//分类名字
    private String lx_class_content;//分类描述
    private String lx_class_cover;//分类图片
    private String top_number;
    private String is_del;//是否可用 0是 1否
    private String f_lx_class_id;//父ID

    public String getF_lx_class_id() {
        return f_lx_class_id;
    }

    public void setF_lx_class_id(String f_lx_class_id) {
        this.f_lx_class_id = f_lx_class_id;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }

    public String getLx_class_id() {
        return lx_class_id;
    }

    public void setLx_class_id(String lx_class_id) {
        this.lx_class_id = lx_class_id;
    }

    public String getLx_class_name() {
        return lx_class_name;
    }

    public void setLx_class_name(String lx_class_name) {
        this.lx_class_name = lx_class_name;
    }

    public String getLx_class_content() {
        return lx_class_content;
    }

    public void setLx_class_content(String lx_class_content) {
        this.lx_class_content = lx_class_content;
    }

    public String getLx_class_cover() {
        return lx_class_cover;
    }

    public void setLx_class_cover(String lx_class_cover) {
        this.lx_class_cover = lx_class_cover;
    }

    public String getTop_number() {
        return top_number;
    }

    public void setTop_number(String top_number) {
        this.top_number = top_number;
    }
}
