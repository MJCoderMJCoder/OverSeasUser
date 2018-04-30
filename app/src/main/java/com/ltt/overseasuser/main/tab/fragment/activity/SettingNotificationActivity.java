package com.ltt.overseasuser.main.tab.fragment.activity;

import android.view.View;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.core.ActionBar;

/**
 * Created by Administrator on 2018/1/23.
 */
public class SettingNotificationActivity extends BaseActivity{
    ActionBar bar;
    @Override
    protected int bindLayoutID() {
        return R.layout.activity_notification;
    }

    @Override
    protected void prepareActivity() {
        bar=ActionBar.init(this);
        bar.setLeft(R.mipmap.back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bar.showNotify();
        bar.setTitle("Notification Setting");
    }
}
