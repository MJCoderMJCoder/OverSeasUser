package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/2 0002.
 */

public class CountriesListBean extends BaseBean {
    private List<CountryAndStatiesBean> data;
    public List<CountryAndStatiesBean> getData(){return data;}
    public void setData(List<CountryAndStatiesBean> data){this.data=data;}
}
