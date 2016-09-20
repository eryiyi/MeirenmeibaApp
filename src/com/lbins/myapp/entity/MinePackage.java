package com.lbins.myapp.entity;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class MinePackage {
    private String package_id;//钱包id
    private String package_money;//金额
    private String emp_id;//会员ID

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getPackage_money() {
        return package_money;
    }

    public void setPackage_money(String package_money) {
        this.package_money = package_money;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }
}
