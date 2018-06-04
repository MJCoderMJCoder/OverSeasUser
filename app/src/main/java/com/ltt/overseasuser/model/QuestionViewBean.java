package com.ltt.overseasuser.model;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class QuestionViewBean {
    private String viewType;
    private View view;
    private String reqeustid;
    private List<String> mValueList;
    public QuestionViewBean(String viewType,View view,String reqeustid){
        this.viewType=viewType;
        this.view = view;
        this.reqeustid=reqeustid;
        mValueList=new ArrayList<>();
    }
    public void setViewType(String viewType){this.viewType=viewType;}
    public String getViewType(){return viewType;}
    public void setView(View view){this.view=view;}
    public View getView(){return view;}
    public void setReqeustid(String reqeustid){this.reqeustid=reqeustid;}
    public String getReqeustid(){return reqeustid;}
    public void addValue(String value){this.mValueList.add(value);}
    public List<String> getValue(){return mValueList;}
}
