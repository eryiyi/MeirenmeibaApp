package com.lbins.myapp.data;

import com.lbins.myapp.entity.ManagerInfo;

import java.util.List;

/**
 * Created by zhl on 2016/9/15.
 */
public class ManagerInfoData extends Data {
    private List<ManagerInfo> data;

    public List<ManagerInfo> getData() {
        return data;
    }

    public void setData(List<ManagerInfo> data) {
        this.data = data;
    }
}
