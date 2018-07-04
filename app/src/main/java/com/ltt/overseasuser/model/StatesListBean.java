package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/2 0002.
 */

public class StatesListBean extends BaseBean {
    private List<StateBean> data;
    public List<StateBean> getData(){return data;}
    public void setData(List<StateBean> data){this.data=data;}
}
