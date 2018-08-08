package com.ltt.overseasuser.model;



import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

/**
 * Created by yunwen on 2018/5/24.
 */

public class ViewRequestBean extends BaseBean {

    private List<ExploreQuestionListBean> data;
    public List<ExploreQuestionListBean> getData(){return data;}
    public void setData(List<ExploreQuestionListBean> data){this.data=data;}
}
