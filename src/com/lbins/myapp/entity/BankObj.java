package com.lbins.myapp.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class BankObj implements Serializable{
    private String bank_id;
    private String emp_id;
    private String bank_mobile;
    private String bank_emp_name;
    private String bank_kaihu_name;
    private String bank_name;
    private String bank_card;
    private String dateline;
    private String is_use;

    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getBank_mobile() {
        return bank_mobile;
    }

    public void setBank_mobile(String bank_mobile) {
        this.bank_mobile = bank_mobile;
    }

    public String getBank_emp_name() {
        return bank_emp_name;
    }

    public void setBank_emp_name(String bank_emp_name) {
        this.bank_emp_name = bank_emp_name;
    }

    public String getBank_kaihu_name() {
        return bank_kaihu_name;
    }

    public void setBank_kaihu_name(String bank_kaihu_name) {
        this.bank_kaihu_name = bank_kaihu_name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_card() {
        return bank_card;
    }

    public void setBank_card(String bank_card) {
        this.bank_card = bank_card;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getIs_use() {
        return is_use;
    }

    public void setIs_use(String is_use) {
        this.is_use = is_use;
    }
}
