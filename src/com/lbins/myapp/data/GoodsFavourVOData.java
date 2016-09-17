package com.lbins.myapp.data;

import com.lbins.myapp.entity.GoodsFavourVO;

import java.util.List;

/**
 * Created by zhl on 2016/9/17.
 */
public class GoodsFavourVOData extends Data {
    private List<GoodsFavourVO> data;

    public List<GoodsFavourVO> getData() {
        return data;
    }

    public void setData(List<GoodsFavourVO> data) {
        this.data = data;
    }
}
