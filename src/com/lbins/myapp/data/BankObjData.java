package com.lbins.myapp.data;

import com.lbins.myapp.entity.BankObj;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class BankObjData extends Data {
    private List<BankObj> data;

    public List<BankObj> getData() {
        return data;
    }

    public void setData(List<BankObj> data) {
        this.data = data;
    }
}
