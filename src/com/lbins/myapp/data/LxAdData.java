package com.lbins.myapp.data;

import com.lbins.myapp.entity.LxAd;

import java.util.List;

/**
 * Created by zhl on 2016/9/17.
 */
public class LxAdData extends Data {
    private List<LxAd> data;

    public List<LxAd> getData() {
        return data;
    }

    public void setData(List<LxAd> data) {
        this.data = data;
    }
}
