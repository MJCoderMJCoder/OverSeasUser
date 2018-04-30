package com.ltt.overseasuser.main.tab.fragment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.core.ActionBar;

import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/18.
 */
public class AddCardDialogActivity extends BaseActivity {
    ActionBar bar;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_add_card_dialog;
    }

    @Override
    protected void prepareActivity() {
        bar = ActionBar.init(this);
        bar.setTitle("Payment");
    }

    @OnClick({R.id.iv_notify, R.id.tv_done})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_notify:
                startActivity(new Intent(this,NotificationActivity.class));
                break;
            case R.id.tv_done:
                startActivity(new Intent(this,NowPaymentActivity.class));
                break;
        }
    }
}
