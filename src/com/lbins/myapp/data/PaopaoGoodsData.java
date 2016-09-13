package com.lbins.myapp.data;

import com.lbins.myapp.entity.PaopaoGoods;

import java.util.List;

/**
 * Created by zhl on 2016/9/13.
 */
public class PaopaoGoodsData extends Data {
    private List<PaopaoGoods> data;

    public List<PaopaoGoods> getData() {
        return data;
    }

    public void setData(List<PaopaoGoods> data) {
        this.data = data;
    }
}
