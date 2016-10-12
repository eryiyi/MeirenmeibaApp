package com.lbins.myapp.entity;

/**
 * Created by zhl on 2016/10/11.
 */
public class CardEmp {
    private  String emp_id;
    private  String lx_card_emp_year;//第几年了
    private  String lx_card_emp_start_time;//第一次开定向卡的时间
    private  String lx_card_emp_end_time;//结束日期--定向卡到期日
    private  String is_use;//是否可用  0可用 1不可用

    private  String emp_number;//会员账号
    private  String emp_mobile;//会员手机号
    private  String emp_name;//会员昵称
    private  String emp_cover;//会员头像

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getLx_card_emp_year() {
        return lx_card_emp_year;
    }

    public void setLx_card_emp_year(String lx_card_emp_year) {
        this.lx_card_emp_year = lx_card_emp_year;
    }

    public String getLx_card_emp_start_time() {
        return lx_card_emp_start_time;
    }

    public void setLx_card_emp_start_time(String lx_card_emp_start_time) {
        this.lx_card_emp_start_time = lx_card_emp_start_time;
    }

    public String getLx_card_emp_end_time() {
        return lx_card_emp_end_time;
    }

    public void setLx_card_emp_end_time(String lx_card_emp_end_time) {
        this.lx_card_emp_end_time = lx_card_emp_end_time;
    }

    public String getIs_use() {
        return is_use;
    }

    public void setIs_use(String is_use) {
        this.is_use = is_use;
    }

    public String getEmp_number() {
        return emp_number;
    }

    public void setEmp_number(String emp_number) {
        this.emp_number = emp_number;
    }

    public String getEmp_mobile() {
        return emp_mobile;
    }

    public void setEmp_mobile(String emp_mobile) {
        this.emp_mobile = emp_mobile;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_cover() {
        return emp_cover;
    }

    public void setEmp_cover(String emp_cover) {
        this.emp_cover = emp_cover;
    }
}
