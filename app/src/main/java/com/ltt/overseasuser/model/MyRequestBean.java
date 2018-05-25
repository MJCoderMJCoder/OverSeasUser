package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

/**
 * Created by MJCoder on 2018-05-18.
 */

public class MyRequestBean extends BaseBean {
    private String request_id;
    private String request_name;
    private String publish_status;
    private String status_request;
    private String total_response;
    private String date_created;

    public String getRequest_id() {
        return request_id;
    }

    public String getRequest_name() {
        return request_name;
    }

    public String getPublish_status() {
        return publish_status;
    }

    public String getStatus_request() {
        return status_request;
    }

    public String getTotal_response() {
        return total_response;
    }

    public String getDate_created() {
        return date_created;
    }

    @Override
    public String toString() {
        return "MyRequestBean{" +
                "request_id='" + request_id + '\'' +
                ", request_name='" + request_name + '\'' +
                ", publish_status='" + publish_status + '\'' +
                ", status_request='" + status_request + '\'' +
                ", total_response='" + total_response + '\'' +
                ", date_created='" + date_created + '\'' +
                '}';
    }
}
