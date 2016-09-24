package com.lbins.myapp.data;

import com.lbins.myapp.entity.BrowsingObj;

import java.util.List;

/**
 * Created by zhl on 2016/9/24.
 */
public class BrowsingObjData extends Data {
    private List<BrowsingObj> data;

    public List<BrowsingObj> getData() {
        return data;
    }

    public void setData(List<BrowsingObj> data) {
        this.data = data;
    }
}
