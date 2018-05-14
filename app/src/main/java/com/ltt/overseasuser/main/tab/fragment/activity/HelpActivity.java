package com.ltt.overseasuser.main.tab.fragment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yunwen on 2018/5/14.
 */

public class HelpActivity extends BaseActivity {
    @BindView(R.id.bt_user)
    Button btUser;
    @BindView(R.id.bt_setrvice)
    Button btSetrvice;
    @BindView(R.id.tv_using)
    TextView tvUsing;
    @BindView(R.id.tv_login_password)
    TextView tvLoginPassword;
    @BindView(R.id.profile_setting)
    TextView profileSetting;
    @BindView(R.id.tv_privacy)
    TextView tvPrivacy;
    @BindView(R.id.polices_reporting)
    TextView policesReporting;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_help;
    }

    @Override
    protected void prepareActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_user, R.id.bt_setrvice, R.id.tv_using, R.id.tv_login_password, R.id.profile_setting, R.id.tv_privacy, R.id.polices_reporting})
    public void onClick(View view) {
        Intent intent = new Intent(this,WebviewActivity.class);
        switch (view.getId()) {


            case R.id.bt_user:
                ToastUtils.showToast("user");
                break;
            case R.id.bt_setrvice:
                ToastUtils.showToast("service");
                break;
            case R.id.tv_using:
                intent.putExtra("weburl","https://popmach.com");
                startActivity(intent);
                break;
            case R.id.tv_login_password:
                break;
            case R.id.profile_setting:
                startActivity(new Intent(this,ProfileActivity.class));
                break;
            case R.id.tv_privacy:
                intent.putExtra("weburl","https://popmach.com/info/privacy");
                startActivity(intent);
                break;
            case R.id.polices_reporting:
                intent.putExtra("weburl","https://popmach.com/info/termsofuse");
                startActivity(intent);
                break;
        }
    }
}
