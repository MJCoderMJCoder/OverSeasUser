package com.ltt.overseasuser.base;

import java.io.Serializable;

public class BaseBean implements Serializable {

    private boolean status;
    private int code;
    private String msg;
    private String access_token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "status=" + status +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", access_token='" + access_token + '\'' +
                '}';
    }
}
