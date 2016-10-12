package com.lbins.myapp.entity;

import java.io.Serializable;

/**
 * Created by zhl on 2016/10/12.
 */
public class PayScanObj implements Serializable{
    private String title;
    private String emp_id;//卖家id
    private String pay_count;//支付金额

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getPay_count() {
        return pay_count;
    }

    public void setPay_count(String pay_count) {
        this.pay_count = pay_count;
    }
}
