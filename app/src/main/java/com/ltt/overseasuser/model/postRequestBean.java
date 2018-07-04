package com.ltt.overseasuser.model;

import java.util.List;

/**
 * Created by Administrator on 2018/5/30 0030.
 */

public class postRequestBean {
    private String create_request_token;
    private String country_id;
    private String state_id;
    private String city;
    private List<PostRequestQuestionsBean> questions;

    public void setCreate_request_token(String create_request_token) {
        this.create_request_token = create_request_token;
    }

    public String getCreate_request_token() {
        return create_request_token;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setQuestions(List<PostRequestQuestionsBean> questions) {
        this.questions = questions;
    }

    public List<PostRequestQuestionsBean> getQuestions() {
        return questions;
    }

}
