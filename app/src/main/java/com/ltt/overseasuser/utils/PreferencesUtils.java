package com.ltt.overseasuser.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ltt.overseasuser.XApplication;
import com.ltt.overseasuser.model.UserBean;

public class PreferencesUtils {

    public static final String SHAREDPREFERENCES_MYUSERDATA = "sharedpreferences_myuserdata";
    public static final String SHAREDPREFERENCES_OTHER = "sharedpreferences_other";

    public class USERINFO {
        public static final String TOKEN = "token";
        public static final String USERPASSWORD = "userPassword";
        public static final String MOBLIE = "moblie";
        public static final String EMAIL = "email";
    }

    public static void saveUserInfoPreference(UserBean userData) {
        if (userData == null) {
            return;
        }
        XApplication.globalUserBean = userData;
        SharedPreferences preferences = XApplication.getApplication().getSharedPreferences(SHAREDPREFERENCES_MYUSERDATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERINFO.TOKEN, userData.getAccess_token());
        editor.putString(USERINFO.USERPASSWORD, userData.getPassword());
        editor.putString(USERINFO.MOBLIE, userData.getPhone());
        editor.putString(USERINFO.EMAIL, userData.getEmail());
        editor.commit();
    }

    public static void changeUserPwdPreference(String pwd) {
        if (XApplication.globalUserBean == null) {
            return;
        }
        SharedPreferences preferences = XApplication.getApplication().getSharedPreferences(SHAREDPREFERENCES_MYUSERDATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERINFO.USERPASSWORD, pwd);
        editor.commit();
    }

    public static void clearUserInfo() {
        SharedPreferences preferences = XApplication.getApplication().getSharedPreferences(SHAREDPREFERENCES_MYUSERDATA, Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
        XApplication.globalUserBean = null;
    }

    public static UserBean getUserInfoPreference() {
        SharedPreferences preferences = XApplication.getApplication().getSharedPreferences(SHAREDPREFERENCES_MYUSERDATA, Context.MODE_PRIVATE);
        UserBean userData = new UserBean();
        userData.setAccess_token(preferences.getString(USERINFO.TOKEN, null));
        userData.setPassword(preferences.getString(USERINFO.USERPASSWORD, null));
        userData.setEmail(preferences.getString(USERINFO.EMAIL, null));
        userData.setPhone(preferences.getString(USERINFO.MOBLIE, null));

        return userData;
    }

    public static SharedPreferences getOtherSharedPrefernces(){
        return XApplication.getApplication().getSharedPreferences(SHAREDPREFERENCES_OTHER, Context.MODE_PRIVATE);
    }
}
