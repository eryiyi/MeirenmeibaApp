package com.lbins.myapp.entity;

/**
 * Created by zhl on 2016/10/7.
 * 消费记录
 */
public class LxConsumption {
    private String lx_consumption_id;//消费记录id
    private String lx_consumption_cont;//说明
    private String lx_consumption_count;//金额
    private String dateline; //时间
    private String lx_consumption_type;//消费类型 0购买商品 1后台充值 2手机端充值 3定向卡充值
    private String order_no;//订单号 可以为空
    private String emp_id;//会员ID

    private String emp_mobile;//会员手机号
    private String emp_name;//会员昵称
    private String emp_cover;//会员头像

    private String lx_card_emp_end_time;//定向充值卡用

    public String getLx_card_emp_end_time() {
        return lx_card_emp_end_time;
    }

    public void setLx_card_emp_end_time(String lx_card_emp_end_time) {
        this.lx_card_emp_end_time = lx_card_emp_end_time;
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

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getLx_consumption_id() {
        return lx_consumption_id;
    }

    public void setLx_consumption_id(String lx_consumption_id) {
        this.lx_consumption_id = lx_consumption_id;
    }

    public String getLx_consumption_cont() {
        return lx_consumption_cont;
    }

    public void setLx_consumption_cont(String lx_consumption_cont) {
        this.lx_consumption_cont = lx_consumption_cont;
    }

    public String getLx_consumption_count() {
        return lx_consumption_count;
    }

    public void setLx_consumption_count(String lx_consumption_count) {
        this.lx_consumption_count = lx_consumption_count;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getLx_consumption_type() {
        return lx_consumption_type;
    }

    public void setLx_consumption_type(String lx_consumption_type) {
        this.lx_consumption_type = lx_consumption_type;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

}
