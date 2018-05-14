package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

/**
 * Created by yunwen on 2018/5/8.
 */

public class MessageListBean extends BaseBean {

    /**
     * data : [{"request_id":"1","response_id":"1","conversation_id":"-LBtrSmvk93tIA-2SLYi","date_created":"2018-05-07 17:10:31","request_category":"Commercial Vehicles-Trucks","user":"Popmach Asia"}]
     * total_message : 1
     */

    private int total_message;
    private List<DataBean> data;

    public int getTotal_message() {
        return total_message;
    }

    public void setTotal_message(int total_message) {
        this.total_message = total_message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * request_id : 1
         * response_id : 1
         * conversation_id : -LBtrSmvk93tIA-2SLYi
         * date_created : 2018-05-07 17:10:31
         * request_category : Commercial Vehicles-Trucks
         * user : Popmach Asia
         */

        private String request_id;
        private String response_id;
        private String conversation_id;
        private String date_created;
        private String request_category;
        private String user;

        public String getRequest_id() {
            return request_id;
        }

        public void setRequest_id(String request_id) {
            this.request_id = request_id;
        }

        public String getResponse_id() {
            return response_id;
        }

        public void setResponse_id(String response_id) {
            this.response_id = response_id;
        }

        public String getConversation_id() {
            return conversation_id;
        }

        public void setConversation_id(String conversation_id) {
            this.conversation_id = conversation_id;
        }

        public String getDate_created() {
            return date_created;
        }

        public void setDate_created(String date_created) {
            this.date_created = date_created;
        }

        public String getRequest_category() {
            return request_category;
        }

        public void setRequest_category(String request_category) {
            this.request_category = request_category;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }
    }
}
