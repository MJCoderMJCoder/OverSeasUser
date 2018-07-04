package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/2 0002.
 */

public class CitiesListBean  extends BaseBean{
    private List<String> data;
    public List<String> getData(){return data;}
    public void setData(List<String> data){this.data=data;}
}
