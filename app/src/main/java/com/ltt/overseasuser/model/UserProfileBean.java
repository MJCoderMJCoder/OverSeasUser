package com.ltt.overseasuser.model;

import com.ltt.overseasuser.base.BaseBean;

/**
 * Created by yunwen on 2018/5/25.
 */

public class UserProfileBean extends BaseBean {

    /**
     * data : {"uid_firebase":"rP1H2FkUMNN67KyaTGfhDqcMxeC2","firstname":"Luqman","lastname":"Paisal","email":"awesome@luqman.rocks","address":"Ad","postcode":"56000","state":"Kuala Lumpur","country":"Malaysia","contact_number":"+60199550935","company_name":"231321","company_registered_number":"31231","company_address":"","company_contact_number":"+85219955095","company_state":"Central and Western (中西區)","company_country":"Hong Kong","company_business_type":"[\"Food Production\",\"Wholesale & Distribution\"]","company_description":"","company_url":""}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid_firebase : rP1H2FkUMNN67KyaTGfhDqcMxeC2
         * firstname : Luqman
         * lastname : Paisal
         * email : awesome@luqman.rocks
         * address : Ad
         * postcode : 56000
         * state : Kuala Lumpur
         * country : Malaysia
         * contact_number : +60199550935
         * company_name : 231321
         * company_registered_number : 31231
         * company_address :
         * company_contact_number : +85219955095
         * company_state : Central and Western (中西區)
         * company_country : Hong Kong
         * company_business_type : ["Food Production","Wholesale & Distribution"]
         * company_description :
         * company_url :
         */

        private String uid_firebase;
        private String firstname;
        private String lastname;
        private String email;
        private String address;
        private String postcode;
        private String state;
        private String country;
        private String contact_number;
        private String company_name;
        private String company_registered_number;
        private String company_address;
        private String company_contact_number;
        private String company_state;
        private String company_country;
        private String company_business_type;
        private String company_description;
        private String company_url;

        public String getUid_firebase() {
            return uid_firebase;
        }

        public void setUid_firebase(String uid_firebase) {
            this.uid_firebase = uid_firebase;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getContact_number() {
            return contact_number;
        }

        public void setContact_number(String contact_number) {
            this.contact_number = contact_number;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getCompany_registered_number() {
            return company_registered_number;
        }

        public void setCompany_registered_number(String company_registered_number) {
            this.company_registered_number = company_registered_number;
        }

        public String getCompany_address() {
            return company_address;
        }

        public void setCompany_address(String company_address) {
            this.company_address = company_address;
        }

        public String getCompany_contact_number() {
            return company_contact_number;
        }

        public void setCompany_contact_number(String company_contact_number) {
            this.company_contact_number = company_contact_number;
        }

        public String getCompany_state() {
            return company_state;
        }

        public void setCompany_state(String company_state) {
            this.company_state = company_state;
        }

        public String getCompany_country() {
            return company_country;
        }

        public void setCompany_country(String company_country) {
            this.company_country = company_country;
        }

        public String getCompany_business_type() {
            return company_business_type;
        }

        public void setCompany_business_type(String company_business_type) {
            this.company_business_type = company_business_type;
        }

        public String getCompany_description() {
            return company_description;
        }

        public void setCompany_description(String company_description) {
            this.company_description = company_description;
        }

        public String getCompany_url() {
            return company_url;
        }

        public void setCompany_url(String company_url) {
            this.company_url = company_url;
        }
    }
}
