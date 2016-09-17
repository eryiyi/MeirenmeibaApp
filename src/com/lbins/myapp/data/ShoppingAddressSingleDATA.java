package com.lbins.myapp.data;


import com.lbins.myapp.entity.ShoppingAddress;

/**
 * 收货地址默认的
 */
public class ShoppingAddressSingleDATA extends Data {
    private ShoppingAddress data;

    public ShoppingAddress getData() {
        return data;
    }

    public void setData(ShoppingAddress data) {
        this.data = data;
    }
}
