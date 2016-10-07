package com.lbins.myapp.data;

import com.lbins.myapp.entity.LxConsumption;

import java.util.List;

/**
 * Created by zhl on 2016/10/7.
 */
public class LxConsumptionData extends Data {
    private List<LxConsumption> data;

    public List<LxConsumption> getData() {
        return data;
    }

    public void setData(List<LxConsumption> data) {
        this.data = data;
    }
}
