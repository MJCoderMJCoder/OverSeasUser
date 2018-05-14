package com.ltt.overseasuser.model;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class ListQuestionBean {
   private String question_id	;
    private String question_title	;
    private String form_type	;
    private List<String> form_optional_value	;
    private String placeholder	;
    private String required ;
    public void setQuestion_id(String question_id){ this.question_id = question_id;}
    public String getQuestion_id(){return question_id;}
    public void setQuestion_title(String question_title){this.question_title=question_title;}
    public String getQuestion_title(){return question_title;}
    public void setForm_type(String form_type){this.form_type=form_type;}
    public String getForm_type(){return form_type;}
    public void setForm_optional_value(List<String> form_optional_value){this.form_optional_value = form_optional_value;}
    public List<String> getForm_optional_value(){return form_optional_value;}
    public void setPlaceholder(String placeholder){this.placeholder=placeholder;}
    public String getPlaceholder(){return placeholder;}
    public void setRequired(String required){this.required=required;}
    public String getRequired(){return required;}
}
