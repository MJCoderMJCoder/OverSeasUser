package com.ltt.overseasuser.model;

import android.view.View;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class QuestionViewBean {
    private String viewType;
    private View view;
    public QuestionViewBean(String viewType,View view){
        this.viewType=viewType;
        this.view = view;
    }
    public void setViewType(String viewType){this.viewType=viewType;}
    public String getViewType(){return viewType;}
    public void setView(View view){this.view=view;}
    public View getView(){return view;}
}
