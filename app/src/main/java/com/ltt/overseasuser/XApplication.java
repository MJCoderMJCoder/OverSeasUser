package com.ltt.overseasuser;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;


import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.login.LoginActivity;
import com.ltt.overseasuser.model.UserBean;
import com.ltt.overseasuser.model.UserNewBean;
import com.ltt.overseasuser.utils.PreferencesUtils;
import com.ltt.overseasuser.utils.ToastUtils;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.ThemeConfig;

public class XApplication extends Application {

    private static XApplication app;

    public static UserNewBean globalUserBean;
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

        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
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

    //这将在收到通知时调用
    private class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            JSONObject data = notification.payload.additionalData;
            String notificationID = notification.payload.notificationID;
            String title = notification.payload.title;
            String body = notification.payload.body;
            String smallIcon = notification.payload.smallIcon;
            String largeIcon = notification.payload.largeIcon;
            String bigPicture = notification.payload.bigPicture;
            String smallIconAccentColor = notification.payload.smallIconAccentColor;
            String sound = notification.payload.sound;
            String ledColor = notification.payload.ledColor;
            int lockScreenVisibility = notification.payload.lockScreenVisibility;
            String groupKey = notification.payload.groupKey;
            String groupMessage = notification.payload.groupMessage;
            String fromProjectNumber = notification.payload.fromProjectNumber;
            String rawPayload = notification.payload.rawPayload;


            String customKey;

            Log.e("OneSignalExample", "NotificationID received: " + notificationID);
            Log.e("OneSignalExample", "NotificationID received: " + data.toString());

            if (data != null) {
                customKey = data.optString("customkey", null);
                if (customKey != null)
                    Log.e("OneSignalExample", "customkey set with value: " + customKey);
            }
        }
    }

    //当一个通知被点击时，将调用它。
    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String launchUrl = result.notification.payload.launchURL; // update docs launchUrl

            String customKey;
            String openURL = null;
            Object activityToLaunch = MainActivity.class;

            if (data != null) {
                customKey = data.optString("customkey", null);
                openURL = data.optString("openURL", null);

                if (customKey != null)
                    Log.e("OneSignalExample", "customkey set with value: " + customKey);

                if (openURL != null)
                    Log.e("OneSignalExample", "openURL to webview with URL value: " + openURL);
            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken) {
                Log.e("OneSignalExample", "Button pressed with id: " + result.action.actionID);

                if (result.action.actionID.equals("id1")) {
                    Log.e("OneSignalExample", "button id called: " + result.action.actionID);
//                    activityToLaunch = GreenActivity.class;
                } else
                    Log.e("OneSignalExample", "button id called: " + result.action.actionID);
            }
            // The following can be used to open an Activity of your choice.
            // Replace - getApplicationContext() - with any Android Context.
            // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
            Intent intent = new Intent(getApplicationContext(), (Class<?>) activityToLaunch);
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("openURL", openURL);
            Log.e("OneSignalExample", "openURL = " + openURL);
            // startActivity(intent);
            startActivity(intent);

            // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
            //   if you are calling startActivity above.
        /*
           <application ...>
             <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
           </application>
        */
        }
    }
}
