package com.ltt.overseasuser.model;

/**
 * Created by yunwen on 2018/7/24.
 */

public class SignTokenBean {

    /**
     * status : true
     * sign_in_custom_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJmaXJlYmFzZS1hZG1pbnNkay1tdnR2ckBwb3BtYWNoLWRldi1hN2ExNS5pYW0uZ3NlcnZpY2VhY2NvdW50LmNvbSIsInN1YiI6ImZpcmViYXNlLWFkbWluc2RrLW12dHZyQHBvcG1hY2gtZGV2LWE3YTE1LmlhbS5nc2VydmljZWFjY291bnQuY29tIiwiYXVkIjoiaHR0cHM6XC9cL2lkZW50aXR5dG9vbGtpdC5nb29nbGVhcGlzLmNvbVwvZ29vZ2xlLmlkZW50aXR5LmlkZW50aXR5dG9vbGtpdC52MS5JZGVudGl0eVRvb2xraXQiLCJ1aWQiOiJyUDFIMkZrVU1OTjY3S3lhVEdmaERxY014ZUMyIiwiaWF0IjoxNTI1Njc0NTg1LCJleHAiOjE1MjU2NzgxODV9.Vpx9NIO3NS1BDKuLrTjT0skuGGgumBTgfXDPl3Nzwq7v05t3ziKVlcXeHqdJWsoaAE1Njdkff6wsxWfzUv7qB34drp1h9rzAncKNoqnrP6KYQH2oqBORgIGt43y2yPYUWgzaJAJ7dGYPEDFvJIZsIB5O6i5UoMhcbazmRMvRVvoxfAvYN_06RPfLSavL2_0qFWlDW00-GJ9QGhbICyP68bRZr2ffmj4u_w4EQwg5yC-dShrgICHgz56ufjPBJ3nFsBfSG4Tb1WZhuhiJATXuI-B_mXQZaVLF1MKFHkjaUxnFjGZ1n4dh2fsRQWI-eXfy2KvNOHnzJjjNPVHOFtqzLw
     * type : new_token
     * current_time : 1525674585
     * expired : 1525676985
     * code : 200
     */

    private boolean status;
    private String sign_in_custom_token;
    private String type;
    private int current_time;
    private int expired;
    private int code;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getSign_in_custom_token() {
        return sign_in_custom_token;
    }

    public void setSign_in_custom_token(String sign_in_custom_token) {
        this.sign_in_custom_token = sign_in_custom_token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(int current_time) {
        this.current_time = current_time;
    }

    public int getExpired() {
        return expired;
    }

    public void setExpired(int expired) {
        this.expired = expired;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
