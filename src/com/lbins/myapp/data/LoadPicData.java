package com.lbins.myapp.data;

import com.lbins.myapp.entity.LoadPic;

import java.util.List;

/**
 * Created by zhl on 2017/1/6.
 */
public class LoadPicData extends Data {
    private List<LoadPic> data;

    public List<LoadPic> getData() {
        return data;
    }

    public void setData(List<LoadPic> data) {
        this.data = data;
    }
}
