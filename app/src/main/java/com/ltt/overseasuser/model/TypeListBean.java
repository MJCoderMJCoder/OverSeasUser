package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

public class TypeListBean extends BaseBean {

    private List<TypeBean> data;

    public List<TypeBean> getData() {
        return data;
    }

    public void setData(List<TypeBean> data) {
        this.data = data;
    }
}
