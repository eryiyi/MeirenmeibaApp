package com.lbins.myapp.data;


import com.lbins.myapp.entity.DianPuFavour;

import java.util.List;

/**
 * Created by zhl on 2016/6/18.
 */
public class DianPuFavourData extends Data {
    private List<DianPuFavour> data;

    public List<DianPuFavour> getData() {
        return data;
    }

    public void setData(List<DianPuFavour> data) {
        this.data = data;
    }
}
