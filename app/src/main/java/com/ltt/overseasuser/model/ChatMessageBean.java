package com.ltt.overseasuser.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yunwen on 2018/5/7.
 */

public class ChatMessageBean  {

    private String channel_type;
    private List<MessageBean> list_message;

    private MembersBean members;

    public String getChannel_type() {
        return channel_type;
    }

    public void setChannel_type(String channel_type) {
        this.channel_type = channel_type;
    }

    public List<MessageBean> getList_message() {
        return list_message;
    }

    public void setList_message(List<MessageBean> list_message) {
        this.list_message = list_message;
    }

    public MembersBean getMembers() {
        return members;
    }

    public void setMembers(MembersBean members) {
        this.members = members;
    }
    public ChatMessageBean() {

    }

    public ChatMessageBean(String channel_type, List<MessageBean> list_message, MembersBean members) {
        this.channel_type = channel_type;
        this.list_message = list_message;
        this.members = members;

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("channel_type", channel_type);
        result.put("list_message", list_message);
        result.put("members", members);
        return result;
    }

    public static class MessageBean {

        String createdAt;
        private String message;
        private String senderId;
        private String senderName;
        private String type;

        @Override
        public String toString() {
            return "MessageBean{" +
                    "createdAt=" + createdAt +
                    ", message='" + message + '\'' +
                    ", senderId='" + senderId + '\'' +
                    ", senderName='" + senderName + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }

        public MessageBean() {
        }

        public MessageBean(String message, String senderId, String senderName, String type) {
            this.message = message;
            this.senderId = senderId;
            this.senderName = senderName;
            this.type = type;
            // Initialize to current time
            createdAt = "";
        }

        public  String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt( String createdAt) {
            this.createdAt = createdAt;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("createdAt", createdAt);
            result.put("message", message);
            result.put("senderId", senderId);
            result.put("senderName", senderName);
            result.put("type", type);
            return result;
        }
    }

    public static class MembersBean {
        private String requester;
        private String service_provider;

        public MembersBean() {

        }

        public MembersBean(String requester, String service_provider) {
            this.requester = requester;
            this.service_provider = service_provider;

        }

        public String getRequester() {
            return requester;
        }

        public void setRequester(String requester) {
            this.requester = requester;
        }

        public String getService_provider() {
            return service_provider;
        }

        public void setService_provider(String service_provider) {
            this.service_provider = service_provider;
        }

        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("requester", requester);
            result.put("service_provider", service_provider);
            return result;
        }
    }

}
