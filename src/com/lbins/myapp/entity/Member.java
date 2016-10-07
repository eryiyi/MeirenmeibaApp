package com.lbins.myapp.entity;

import java.io.Serializable;

/**
 * Created by zhl on 2016/9/13.
 */
public class Member implements Serializable{
    private String empId; //用户ID 唯一标识
    private String emp_number;//用户账号（注册时生成的唯一标识）
    private String empMobile;//手机号
    private String empPass;//密码
    private String empName;//姓名
    private String empCover;//头像
    private String empSex;//性别
    private String isUse;//是否禁用 0否  1是
    private String dateline;//注册日期 毫秒值
    private String emp_birthday;//出生日期 1988-2-11

    private String hxUsername;//暂不用
    private String isInGroup;//暂不用

    private String pushId;//百度推送的用户唯一标志
    private String deviceType;//百度推送的设备标志  3是安卓 4是苹果

    private String lat;//用户经纬度 37.415571
    private String lng;//用户经纬度 117.482235
    private String level_id;//等级id 对应等级表
    private String emp_erweima;//二维码
    private String emp_up;//上级ID（介绍人、推荐人）

    private String emp_up_mobile;//上级手机号
    private String levelName;//等级名称
    private String jfcount;//积分
    private String emp_pay_pass;//支付密码
    private String package_money;//零钱（后台处理，前台只接收值就行）

    public String getPackage_money() {
        return package_money;
    }

    public void setPackage_money(String package_money) {
        this.package_money = package_money;
    }

    public String getEmp_pay_pass() {
        return emp_pay_pass;
    }

    public void setEmp_pay_pass(String emp_pay_pass) {
        this.emp_pay_pass = emp_pay_pass;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getJfcount() {
        return jfcount;
    }

    public void setJfcount(String jfcount) {
        this.jfcount = jfcount;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmp_number() {
        return emp_number;
    }

    public void setEmp_number(String emp_number) {
        this.emp_number = emp_number;
    }

    public String getEmpMobile() {
        return empMobile;
    }

    public void setEmpMobile(String empMobile) {
        this.empMobile = empMobile;
    }

    public String getEmpPass() {
        return empPass;
    }

    public void setEmpPass(String empPass) {
        this.empPass = empPass;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpCover() {
        return empCover;
    }

    public void setEmpCover(String empCover) {
        this.empCover = empCover;
    }

    public String getEmpSex() {
        return empSex;
    }

    public void setEmpSex(String empSex) {
        this.empSex = empSex;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getEmp_birthday() {
        return emp_birthday;
    }

    public void setEmp_birthday(String emp_birthday) {
        this.emp_birthday = emp_birthday;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getHxUsername() {
        return hxUsername;
    }

    public void setHxUsername(String hxUsername) {
        this.hxUsername = hxUsername;
    }

    public String getIsInGroup() {
        return isInGroup;
    }

    public void setIsInGroup(String isInGroup) {
        this.isInGroup = isInGroup;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLevel_id() {
        return level_id;
    }

    public void setLevel_id(String level_id) {
        this.level_id = level_id;
    }

    public String getEmp_erweima() {
        return emp_erweima;
    }

    public void setEmp_erweima(String emp_erweima) {
        this.emp_erweima = emp_erweima;
    }

    public String getEmp_up() {
        return emp_up;
    }

    public void setEmp_up(String emp_up) {
        this.emp_up = emp_up;
    }

    public String getEmp_up_mobile() {
        return emp_up_mobile;
    }

    public void setEmp_up_mobile(String emp_up_mobile) {
        this.emp_up_mobile = emp_up_mobile;
    }
}
