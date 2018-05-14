package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class QuestionBean extends BaseBean {
    private String total_question;
    private List<ListQuestionBean> list_question;
    public String getTotal_question(){return total_question;}
    public void setTotal_question(String total_question){this.total_question=total_question;}
    public List<ListQuestionBean> getist_question() {
        return list_question;
    }
    public void setList_question(List<ListQuestionBean> list_question) {
        this.list_question = list_question;
    }
}
