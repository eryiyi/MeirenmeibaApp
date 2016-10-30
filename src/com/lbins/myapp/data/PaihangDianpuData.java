package com.lbins.myapp.data;

import com.lbins.myapp.entity.PaihangDianpu;

import java.util.List;

/**
 * Created by zhl on 2016/10/30.
 */
public class PaihangDianpuData extends Data {
    private List<PaihangDianpu> data;

    public List<PaihangDianpu> getData() {
        return data;
    }

    public void setData(List<PaihangDianpu> data) {
        this.data = data;
    }
}
