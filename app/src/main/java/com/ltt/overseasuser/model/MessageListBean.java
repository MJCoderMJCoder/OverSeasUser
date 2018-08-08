package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

import java.util.List;

/**
 * Created by yunwen on 2018/5/8.
 */

public class MessageListBean extends BaseBean {

    /**
     * data : [{"request_id":"4","response_id":"26","conversation_id":"-LG3GZzFLOYfN9DyjDv5","date_created":"2018-06-28 10:43:36","request_category":"Machinery-Excavator","opposite_user":{"name":"MUHAMMAD PAISAL ","uid_firebase":"BBR0qPKp6mRg0Cvd5NpCqZzFJcC2"},"current_user":{"name":"TEST POPMACH","uid_firebase":"VIeCBu1x8YVRTkHxTfARJq3C0qB2"}},{"request_id":"3","response_id":"14","conversation_id":"-LG3Dlaob7BeQTrUVcwq","date_created":"2018-06-28 10:31:21","request_category":"Machinery-Excavator","opposite_user":{"name":"LUQMAN PAISAL","uid_firebase":"7364oeqm1IXj03SdI57ir0jKpLd2"},"current_user":{"name":"TEST POPMACH","uid_firebase":"VIeCBu1x8YVRTkHxTfARJq3C0qB2"}},{"request_id":"4","response_id":"12","conversation_id":"-LG3DYccIhMUS5s1wph2","date_created":"2018-06-28 10:30:24","request_category":"Machinery-Excavator","opposite_user":{"name":"LUQMAN PAISAL","uid_firebase":"7364oeqm1IXj03SdI57ir0jKpLd2"},"current_user":{"name":"TEST POPMACH","uid_firebase":"VIeCBu1x8YVRTkHxTfARJq3C0qB2"}},{"request_id":"5","response_id":"11","conversation_id":"-LG3DWp7cLMTbKyq4lRY","date_created":"2018-06-28 10:30:16","request_category":"Machinery-Excavator","opposite_user":{"name":"LUQMAN PAISAL","uid_firebase":"7364oeqm1IXj03SdI57ir0jKpLd2"},"current_user":{"name":"TEST POPMACH","uid_firebase":"VIeCBu1x8YVRTkHxTfARJq3C0qB2"}},{"request_id":"3","response_id":"3","conversation_id":"-LG-68e1eyKTW10zJqKp","date_created":"2018-06-27 15:19:33","request_category":"Machinery-Excavator","opposite_user":{"name":"MUHAMMAD PAISAL ","uid_firebase":"BBR0qPKp6mRg0Cvd5NpCqZzFJcC2"},"current_user":{"name":"TEST POPMACH","uid_firebase":"VIeCBu1x8YVRTkHxTfARJq3C0qB2"}},{"request_id":"5","response_id":"2","conversation_id":"-LFz0Fe3FhPY-C_ff9ht","date_created":"2018-06-27 10:14:12","request_category":"Machinery-Excavator","opposite_user":{"name":"dennis lee","uid_firebase":"SXsLOc8cCvTIIjWxLojKxrHOxhu2"},"current_user":{"name":"TEST POPMACH","uid_firebase":"VIeCBu1x8YVRTkHxTfARJq3C0qB2"}},{"request_id":"5","response_id":"1","conversation_id":"-LFykDjY_usSAFkrmBzT","date_created":"2018-06-27 08:59:48","request_category":"Machinery-Excavator","opposite_user":{"name":"MUHAMMAD PAISAL ","uid_firebase":"BBR0qPKp6mRg0Cvd5NpCqZzFJcC2"},"current_user":{"name":"TEST POPMACH","uid_firebase":"VIeCBu1x8YVRTkHxTfARJq3C0qB2"}}]
     * total_message : 7
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
         * request_id : 4
         * response_id : 26
         * conversation_id : -LG3GZzFLOYfN9DyjDv5
         * date_created : 2018-06-28 10:43:36
         * request_category : Machinery-Excavator
         * opposite_user : {"name":"MUHAMMAD PAISAL ","uid_firebase":"BBR0qPKp6mRg0Cvd5NpCqZzFJcC2"}
         * current_user : {"name":"TEST POPMACH","uid_firebase":"VIeCBu1x8YVRTkHxTfARJq3C0qB2"}
         */

        private String request_id;
        private String response_id;
        private String conversation_id;
        private String date_created;
        private String request_category;
        private OppositeUserBean opposite_user;
        private CurrentUserBean current_user;

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

        public OppositeUserBean getOpposite_user() {
            return opposite_user;
        }

        public void setOpposite_user(OppositeUserBean opposite_user) {
            this.opposite_user = opposite_user;
        }

        public CurrentUserBean getCurrent_user() {
            return current_user;
        }

        public void setCurrent_user(CurrentUserBean current_user) {
            this.current_user = current_user;
        }

        public static class OppositeUserBean {
            /**
             * name : MUHAMMAD PAISAL
             * uid_firebase : BBR0qPKp6mRg0Cvd5NpCqZzFJcC2
             */

            private String name;
            private String uid_firebase;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUid_firebase() {
                return uid_firebase;
            }

            public void setUid_firebase(String uid_firebase) {
                this.uid_firebase = uid_firebase;
            }
        }

        public static class CurrentUserBean {
            /**
             * name : TEST POPMACH
             * uid_firebase : VIeCBu1x8YVRTkHxTfARJq3C0qB2
             */

            private String name;
            private String uid_firebase;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUid_firebase() {
                return uid_firebase;
            }

            public void setUid_firebase(String uid_firebase) {
                this.uid_firebase = uid_firebase;
            }
        }
    }
}
