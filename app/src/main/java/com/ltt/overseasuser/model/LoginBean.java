package com.ltt.overseasuser.model;

/**
 * Created by Administrator on 2018/5/12.
 */

public class LoginBean {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String email;
    private String password;

}
