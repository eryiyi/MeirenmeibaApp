package com.lbins.myapp.data;

import com.lbins.myapp.entity.DxkAd;

import java.util.List;

/**
 * Created by zhl on 2017/1/12.
 */
public class DxkAdData extends Data {
    private List<DxkAd> data;

    public List<DxkAd> getData() {
        return data;
    }

    public void setData(List<DxkAd> data) {
        this.data = data;
    }
}
