package com.ltt.overseasuser.model;

public class PWBean {

    /**
     * current_password : test123
     * new_password : test123
     * repeat_password : test123
     */

    private String current_password;
    private String new_password;
    private String repeat_password;

    public String getCurrent_password() {
        return current_password;
    }

    public void setCurrent_password(String current_password) {
        this.current_password = current_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public String getRepeat_password() {
        return repeat_password;
    }

    public void setRepeat_password(String repeat_password) {
        this.repeat_password = repeat_password;
    }
}
