package com.lbins.myapp.data;

import com.lbins.myapp.entity.KefuTel;

import java.util.List;

/**
 * Created by zhl on 2016/9/30.
 */
public class KefuTelData extends Data {
    private List<KefuTel> data;

    public List<KefuTel> getData() {
        return data;
    }

    public void setData(List<KefuTel> data) {
        this.data = data;
    }
}
