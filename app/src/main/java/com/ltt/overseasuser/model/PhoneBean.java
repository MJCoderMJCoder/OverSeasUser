package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

import java.io.Serializable;

public class PhoneBean extends BaseBean {

    private String id;
    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
