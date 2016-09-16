package com.lbins.myapp.data;

import com.lbins.myapp.entity.AdObj;

import java.util.List;

/**
 * Created by zhl on 2016/9/16.
 */
public class AdObjData extends Data {
    private List<AdObj> data;

    public List<AdObj> getData() {
        return data;
    }

    public void setData(List<AdObj> data) {
        this.data = data;
    }
}
