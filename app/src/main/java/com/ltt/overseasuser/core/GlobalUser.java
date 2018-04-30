package com.ltt.overseasuser.core;

public class GlobalUser {

    private static GlobalUser user;

    private String uid;
    private String token;
    private String nickname;
    private String headUrl;
    private String desc;
    private String sex;

    public String getSex() {
        if (sex.equals("1"))
            return "男";
        else
            return "女";
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private GlobalUser() {
    }

    public static GlobalUser getInstance() {
        if (user == null) {
            synchronized (GlobalUser.class) {
                if (user == null)
                    user = new GlobalUser();
            }
        }
        return user;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getUserId() {
        return uid;
    }

    public String getToken() {
        return token;
    }

    public void setUserId(String userId) {
        uid = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
