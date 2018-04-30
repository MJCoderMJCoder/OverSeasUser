package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

public class GsonUserBean extends BaseBean {

    private UserBean data;

    public UserBean getData() {
        return data;
    }

    public void setData(UserBean data) {
        this.data = data;
    }
}
