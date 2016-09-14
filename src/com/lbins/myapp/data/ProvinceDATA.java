package com.lbins.myapp.data;


import com.lbins.myapp.entity.Province;

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/2/2
 * Time: 14:59
 * 类的功能、说明写在此处.
 */
public class ProvinceDATA extends Data {

    private List<Province> data;

    public List<Province> getData() {
        return data;
    }

    public void setData(List<Province> data) {
        this.data = data;
    }
}
