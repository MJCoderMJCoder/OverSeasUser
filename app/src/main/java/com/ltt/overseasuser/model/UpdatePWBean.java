package com.ltt.overseasuser.model;

public class UpdatePWBean {

    /**
     * status : true
     * msg : Successfully update new password
     * code : 200
     */

    private boolean status;
    private String msg;
    private int code;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
