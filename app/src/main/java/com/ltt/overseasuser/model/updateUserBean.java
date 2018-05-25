package com.ltt.overseasuser.model;

/**
 * Created by yunwen on 2018/5/25.
 */

public class updateUserBean {

    /**
     * email : handsome@luqman.rocks
     * firstname : Luqman
     * lastname : Paisal
     * country_id : 158
     * phone : 199550935
     * state : Kuala Lumpur
     * postcode : 56000
     */

    private String email;
    private String firstname;
    private String lastname;
    private int country_id;
    private String phone;
    private String state;
    private String postcode;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;

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

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
