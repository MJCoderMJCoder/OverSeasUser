package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class QuestionBean extends BaseBean {
    private String create_request_token;
    private boolean required_location;
    private boolean enable_upload;
    private String upload_id;
    private String total_question;
    private List<ListQuestionBean> list_question;
    public String getCreate_request_token(){return create_request_token;}
    public void setCreate_request_token(String create_request_token){this.create_request_token=create_request_token;}
    public boolean isRequired_location(){return required_location;}
    public void setRequired_location(boolean required_location){this.required_location=required_location;}
    public boolean isEnable_upload(){return enable_upload;}
    public void setEnable_upload(boolean enable_upload){this.enable_upload=enable_upload;}
    public String getUpload_id(){return upload_id;}
    public void setUpload_id(String upload_id){this.upload_id=upload_id;}
    public String getTotal_question(){return total_question;}
    public void setTotal_question(String total_question){this.total_question=total_question;}
    public List<ListQuestionBean> getist_question() {
        return list_question;
    }
    public void setList_question(List<ListQuestionBean> list_question) {
        this.list_question = list_question;
    }
}
