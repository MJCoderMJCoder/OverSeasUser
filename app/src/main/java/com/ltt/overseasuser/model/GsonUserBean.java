package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

public class GsonUserBean extends BaseBean {

    private UserNewBean data;

    public UserNewBean getData() {
        return data;
    }

    public void setData(UserNewBean data) {
        this.data = data;
    }
}
