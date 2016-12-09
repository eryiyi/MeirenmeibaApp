package com.lbins.myapp.data;


import com.lbins.myapp.entity.Member;

import java.util.List;

/**
 * Created by zhl on 2016/9/13.
 */
public class MembersData extends Data {
    private List<Member> data;

    public List<Member> getData() {
        return data;
    }

    public void setData(List<Member> data) {
        this.data = data;
    }
}
