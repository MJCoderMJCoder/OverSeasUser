package com.ltt.overseasuser.model;

/**
 * Created by MJCoder on 2018-05-18.
 */

public class MyRequestBean {
    private String request_id;
    private String request_name;
    private String publish_status;
    private String status_request;
    private String total_response;
    private String date_created;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getRequest_name() {
        return request_name;
    }

    public void setRequest_name(String request_name) {
        this.request_name = request_name;
    }

    public String getPublish_status() {
        return publish_status;
    }

    public void setPublish_status(String publish_status) {
        this.publish_status = publish_status;
    }

    public String getStatus_request() {
        return status_request;
    }

    public void setStatus_request(String status_request) {
        this.status_request = status_request;
    }

    public String getTotal_response() {
        return total_response;
    }

    public void setTotal_response(String total_response) {
        this.total_response = total_response;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
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
