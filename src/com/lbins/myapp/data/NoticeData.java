package com.lbins.myapp.data;

import com.lbins.myapp.entity.Notice;

import java.util.List;

/**
 * Created by zhl on 2016/10/30.
 */
public class NoticeData extends Data {
    protected List<Notice> data;

    public List<Notice> getData() {
        return data;
    }

    public void setData(List<Notice> data) {
        this.data = data;
    }
}
