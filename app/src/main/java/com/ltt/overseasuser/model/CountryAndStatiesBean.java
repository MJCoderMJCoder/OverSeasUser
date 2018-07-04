package com.ltt.overseasuser.model;

import java.util.List;

/**
 * Created by Administrator on 2018/7/2 0002.
 */

public class CountryAndStatiesBean {
    private CountryBean country;
    private List<StateBean> states;
    public void setCountry(CountryBean country){this.country=country;}
    public CountryBean getCountry(){return country;}
    public void setStates(List<StateBean> states){this.states=states;}
    public List<StateBean> getStates(){return states;}
}
