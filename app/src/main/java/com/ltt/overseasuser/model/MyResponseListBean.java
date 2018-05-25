package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

/**
 * Created by MJCoder on 2018-05-18.
 */

public class MyResponseListBean extends BaseBean {
    private List<MyResponseBean> data;
    private String total;
    private String current_page;

    public List<MyResponseBean> getData() {
        return data;
    }

    public void setData(List<MyResponseBean> data) {
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
        return "MyResponseListBean{" +
                "data=" + data +
                ", total='" + total + '\'' +
                ", current_page='" + current_page + '\'' +
                "} " + super.toString();
    }
}
