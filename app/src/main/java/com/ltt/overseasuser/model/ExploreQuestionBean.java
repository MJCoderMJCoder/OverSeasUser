package com.ltt.overseasuser.model;

import java.util.List;

/**
 * Created by Administrator on 2018/5/12 0012.
 */

public class ExploreQuestionBean {
    private String request;
    private String request_name;
    private String user;
    private List<ExploreQuestionListBean> questions;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRequest_name() {
        return request_name;
    }

    public void setRequest_name(String request_name) {
        this.request_name = request_name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<ExploreQuestionListBean> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ExploreQuestionListBean> questions) {
        this.questions = questions;
    }
}
