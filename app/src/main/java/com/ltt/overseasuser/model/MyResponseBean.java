package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

/**
 * Created by MJCoder on 2018-05-18.
 */

public class MyResponseBean extends BaseBean {
    private String request_id;
    private String quote_id;
    private List<String> response_msg;
    private String service_provider;
    private String conversation_id;
    private String quote_status_name;
    private String date_created;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(String quote_id) {
        this.quote_id = quote_id;
    }

    public List<String> getResponse_msg() {
        return response_msg;
    }

    public void setResponse_msg(List<String> response_msg) {
        this.response_msg = response_msg;
    }

    public String getService_provider() {
        return service_provider;
    }

    public void setService_provider(String service_provider) {
        this.service_provider = service_provider;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getQuote_status_name() {
        return quote_status_name;
    }

    public void setQuote_status_name(String quote_status_name) {
        this.quote_status_name = quote_status_name;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    @Override
    public String toString() {
        return "MyResponseBean{" +
                "request_id='" + request_id + '\'' +
                ", quote_id='" + quote_id + '\'' +
                ", response_msg=" + response_msg +
                ", service_provider='" + service_provider + '\'' +
                ", conversation_id='" + conversation_id + '\'' +
                ", quote_status_name='" + quote_status_name + '\'' +
                ", date_created='" + date_created + '\'' +
                '}';
    }
}
