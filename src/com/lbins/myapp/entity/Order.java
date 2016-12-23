package com.lbins.myapp.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/14.
 */
public class Order implements Serializable{
    private String order_no;//订单no
    private String goods_id;//商品id
    private String emp_id;//会员ID
    private String seller_emp_id;//卖家id
    private String address_id;//收货地址ID
    private String goods_count;//商品数量
    private String payable_amount;//支付金额
    private String create_time;//创建订单时间--毫秒
    private String pay_time;//支付时间--毫秒
    private String send_time;//发货时间--毫秒
    private String accept_time;//收货时间--毫秒
    private String completion_time;//订单完成时间--毫秒
    private String status;//状态---1生成订单,2支付订单,3取消订单,4作废订单,5完成订单',
    private String pay_status;//支付状态 0：未支付，1：已支付，2：退款'
    private String distribution_type;//0配送 1自取 默认0
    private String distribution_status;//配送状态 0：未发送,1：已发送,2：到达
    private String postscript;//用户附言
    private String point;
    private String trade_no;
    private String invoice;
    private String invoice_title;
    private String taxes;//税率
    private String out_trade_no;//支付宝 微信的订单id

    private String provinceId;
    private String cityId;
    private String areaId;
    private String trade_type;//0支付宝  1微信 2零钱
    private String is_return;//是否退货完成 0否 1是（卖家已处理）
    private String is_dxk_order;//是否0元订单； 默认0否  1是  2是充值定向卡会员
    private String is_comment;//是否评价； 默认0否  1是

    private String payable_amount_all;//没打折之前的金额
    private String pv_amount;//商品的pv在订单中的总和 这是返利用到的（商品pv,pv即利润）
    private String order_cont;

    public String getOrder_cont() {
        return order_cont;
    }

    public void setOrder_cont(String order_cont) {
        this.order_cont = order_cont;
    }

    public String getPayable_amount_all() {
        return payable_amount_all;
    }

    public void setPayable_amount_all(String payable_amount_all) {
        this.payable_amount_all = payable_amount_all;
    }

    public String getPv_amount() {
        return pv_amount;
    }

    public void setPv_amount(String pv_amount) {
        this.pv_amount = pv_amount;
    }

    public String getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(String is_comment) {
        this.is_comment = is_comment;
    }

    public String getIs_dxk_order() {
        return is_dxk_order;
    }

    public void setIs_dxk_order(String is_dxk_order) {
        this.is_dxk_order = is_dxk_order;
    }

    public String getIs_return() {
        return is_return;
    }

    public void setIs_return(String is_return) {
        this.is_return = is_return;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getSeller_emp_id() {
        return seller_emp_id;
    }

    public void setSeller_emp_id(String seller_emp_id) {
        this.seller_emp_id = seller_emp_id;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(String goods_count) {
        this.goods_count = goods_count;
    }

    public String getPayable_amount() {
        return payable_amount;
    }

    public void setPayable_amount(String payable_amount) {
        this.payable_amount = payable_amount;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getAccept_time() {
        return accept_time;
    }

    public void setAccept_time(String accept_time) {
        this.accept_time = accept_time;
    }

    public String getCompletion_time() {
        return completion_time;
    }

    public void setCompletion_time(String completion_time) {
        this.completion_time = completion_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getDistribution_type() {
        return distribution_type;
    }

    public void setDistribution_type(String distribution_type) {
        this.distribution_type = distribution_type;
    }

    public String getDistribution_status() {
        return distribution_status;
    }

    public void setDistribution_status(String distribution_status) {
        this.distribution_status = distribution_status;
    }

    public String getPostscript() {
        return postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getInvoice_title() {
        return invoice_title;
    }

    public void setInvoice_title(String invoice_title) {
        this.invoice_title = invoice_title;
    }

    public String getTaxes() {
        return taxes;
    }

    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public Order(String goods_id, String emp_id, String seller_emp_id, String address_id, String goods_count, String payable_amount,
                 String distribution_type, String distribution_status, String postscript, String invoice, String invoice_title, String taxes,
                 String provinceId, String cityId, String areaId,String trade_type,String payable_amount_all,String pv_amount, String is_dxk_order) {
        this.goods_id = goods_id;
        this.emp_id = emp_id;
        this.seller_emp_id = seller_emp_id;
        this.address_id = address_id;
        this.goods_count = goods_count;
        this.distribution_type = distribution_type;
        this.distribution_status = distribution_status;
        this.postscript = postscript;
        this.invoice = invoice;
        this.invoice_title = invoice_title;
        this.taxes = taxes;
        this.payable_amount = payable_amount;
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.areaId = areaId;
        this.trade_type = trade_type;
        this.payable_amount_all = payable_amount_all;
        this.pv_amount = pv_amount;
        this.is_dxk_order = is_dxk_order;
    }

}
