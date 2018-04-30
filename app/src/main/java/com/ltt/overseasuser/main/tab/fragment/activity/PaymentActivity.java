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
public class PaymentActivity extends BaseActivity {
    ActionBar bar;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_paymennt;
    }

    @Override
    protected void prepareActivity() {
        bar = ActionBar.init(this);
        bar.setLeft(R.mipmap.back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bar.setTitle("Payment");
        bar.showNotify();
    }


    @OnClick({R.id.iv_notify,R.id.btn_click1, R.id.btn_click2, R.id.btn_click3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_notify:
                startActivity(new Intent(this,NotificationActivity.class));
                break;
            case R.id.btn_click1:
            case R.id.btn_click2:
            case R.id.btn_click3:
                startActivity(new Intent(this,AddCardActivity.class));
                break;
        }
    }
}
