package com.lbins.myapp.data;

import com.lbins.myapp.entity.GoodsComment;

import java.util.List;

/**
 * Created by zhl on 2016/9/16.
 */
public class GoodsCommentData extends Data {
    private List<GoodsComment> data;

    public List<GoodsComment> getData() {
        return data;
    }

    public void setData(List<GoodsComment> data) {
        this.data = data;
    }
}
