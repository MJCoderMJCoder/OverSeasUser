package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

/**
 * Created by MJCoder on 2018-05-18.
 */

public class MyRequestListBean extends BaseBean {
    private List<MyRequestBean> data;
    private String total;
    private String current_page;

    public List<MyRequestBean> getData() {
        return data;
    }

    public void setData(List<MyRequestBean> data) {
        this.data = data;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    @Override
    public String toString() {
        return "MyRequestListBean{" +
                "data=" + data +
                ", total='" + total + '\'' +
                ", current_page='" + current_page + '\'' +
                "} " + super.toString();
    }
}
