package com.lbins.myapp.data;

import com.lbins.myapp.entity.GoodsType;

import java.util.List;

/**
 * Created by zhl on 2016/9/12.
 */
public class GoodsTypeData extends Data {
    private List<GoodsType> data;

    public List<GoodsType> getData() {
        return data;
    }

    public void setData(List<GoodsType> data) {
        this.data = data;
    }
}
