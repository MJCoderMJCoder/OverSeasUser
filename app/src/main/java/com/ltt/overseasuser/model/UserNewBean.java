package com.ltt.overseasuser.model;

/**
 * Created by yunwen on 2018/7/19.
 */

public class UserNewBean {

    /**
     * status : true
     * code : 200
     * access_token : eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJjb25zdW1lcktleSI6Im1CMHlPajVZZW9nZ3hXTGtEdGtJbnl1YTE4d3N2ZmdnIiwidXNlcl9pZCI6IjE4OSIsImlzc3VlZEF0IjoiMjAxOC0wNy0xOVQxODoxNToxMyswODAwIiwidHRsIjoyNjc4NDAwfQ.s2tAfam4Dj1jDyJ7YBWeXOBOWgd3O5hn8syx3LAlfNg
     * user_id : 189
     * expired : 1534608000
     * user_info : {"email":"test@popmach.com","firstname":"TEST","lastname":"POPMACH"}
     */

    private boolean status;
    private int code;
    private String access_token;
    private int user_id;
    private int expired;
    private UserInfoBean user_info;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getExpired() {
        return expired;
    }

    public void setExpired(int expired) {
        this.expired = expired;
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public static class UserInfoBean {
        /**
         * email : test@popmach.com
         * firstname : TEST
         * lastname : POPMACH
         */

        private String email;
        private String firstname;
        private String lastname;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }
    }
}
