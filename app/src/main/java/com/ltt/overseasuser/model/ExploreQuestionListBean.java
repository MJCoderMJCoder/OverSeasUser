package com.ltt.overseasuser.model;

/**
 * Created by Administrator on 2018/5/12 0012.
 */

public class ExploreQuestionListBean {
   private  String   question_id;
     private String  question_title;
     private String  value;
     private String  form_type;
   public String getQuestion_title(){return question_title;}
   public void setQuestion_title(String question_title){this.question_title=question_title;}
   public String getQuestion_id(){return question_id;}
   public void setQuestion_id(String question_id){this.question_id=question_id;}
   public void setValue(String value){this.value=value;}
   public  String getValue(){return value;}
   public void setForm_type(String form_type){this.form_type=form_type;}
   public String getForm_type(){return form_type;}
}
