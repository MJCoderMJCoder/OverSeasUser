package com.ltt.overseasuser;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.login.LoginActivity;
import com.ltt.overseasuser.model.UserBean;
import com.ltt.overseasuser.utils.PreferencesUtils;
import com.ltt.overseasuser.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.ThemeConfig;

public class XApplication extends Application {

    private static XApplication app;

    public static UserBean globalUserBean;
    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;

    public static XApplication getApplication() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        globalUserBean = PreferencesUtils.getUserInfoPreference();
        init();
        getScreenSize();
//        initGalleryFinal();
    }

    private void initGalleryFinal() {
        ThemeConfig config = new ThemeConfig.Builder().setTitleBarBgColor(getResources().getColor(R.color.theme))
                .build();

        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(false)
                .setEnableEdit(false)
                .setEnableCrop(false)
                .setEnableRotate(false)
                .setCropSquare(true)
                .setEnablePreview(false)
                .setMutiSelectMaxSize(9)
                .build();
    }

    private void init() {

    }

    //踢出处理
    public Handler authHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//			if(isActivityInForground()){
//			while(!DemoHelper.getInstance().isLoggedIn()){
//
//			}
            switch (msg.what) {
                case 0:
                    ToastUtils.showToast("账号在其他设备登录");
                    break;
                case 2:
                    ToastUtils.showToast("注销成功");
                    break;
                default:
                    ToastUtils.showToast("帐号登录过期,请重新登录");
                    break;
            }
//            MyApplication.userData = null;
//            PreferencesUtils.clearUserInfo();
            Intent intent = new Intent(XApplication.getApplication(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finishActivity();
            XApplication.getApplication().startActivity(intent);
        }
    };

    private void getScreenSize() {
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
//        DIMEN_RATE = dm.density / 1.0f
//        DIMEN_DPI = dm.densityDpi
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if (SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }

    /***************
     * activity 管理开始
     *********************/
    private static List<Activity> list = new ArrayList<Activity>();

    public void addActivity(BaseActivity baseActivity) {
        list.add(baseActivity);
    }

    public void removeActivity(BaseActivity baseActivity) {
        list.remove(baseActivity);
    }

    public void finishActivity() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).finish();
        }
    }

    public void finishOtherActivity(Activity activity) {
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals(activity)) {
                list.get(i).finish();
            }
        }
    }

    public boolean isActivityInForground() {
        for (int i = 0; i < list.size(); i++) {
            if (((BaseActivity) list.get(i)).isForGround) {
                return true;
            }
        }
        return false;
    }

    /***************
     * activity 管理结束
     *********************/
}
