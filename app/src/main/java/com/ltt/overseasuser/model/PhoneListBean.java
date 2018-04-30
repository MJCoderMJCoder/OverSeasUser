package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

public class PhoneListBean extends BaseBean {

    private List<PhoneBean> data;

    public List<PhoneBean> getData() {
        return data;
    }

    public void setData(List<PhoneBean> data) {
        this.data = data;
    }
}
