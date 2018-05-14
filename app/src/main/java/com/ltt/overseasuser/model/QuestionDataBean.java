package com.ltt.overseasuser.model;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9 0009.
 */

public class QuestionDataBean {
    private List<QuestionBean> data;
    public void setData(List<QuestionBean> data){this.data= data;}
    public List<QuestionBean> getData(){return data;}
}
