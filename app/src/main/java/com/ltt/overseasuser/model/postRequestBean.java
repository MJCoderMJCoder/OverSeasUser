package com.ltt.overseasuser.model;

import java.util.List;

/**
 * Created by Administrator on 2018/5/30 0030.
 */

public class postRequestBean {
    private int section_id;
    private List<PostRequestQuestionsBean> questions;
    public void setSection_id(int section_id){this.section_id=section_id;}
    public int getSection_id(){return  section_id;}
    public void setQuestions(List<PostRequestQuestionsBean> questions){this.questions=questions;}
    public List<PostRequestQuestionsBean> getQuestions(){return questions;}

}
