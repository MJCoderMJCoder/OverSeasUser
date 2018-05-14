package com.ltt.overseasuser.splash.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseFragment;
import com.ltt.overseasuser.login.LoginActivity;
import com.ltt.overseasuser.login.SignUpActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/12.
 */
public class Introduce3Fragment extends BaseFragment {

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_signup)
    Button btnSignup;

    @Override
    protected int bindLayoutID() {
        return R.layout.fragment_introduce3;
    }

    @Override
    protected void prepareFragment() {

    }

    @OnClick({R.id.btn_login, R.id.btn_signup})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Intent in1=new Intent(getActivity(), LoginActivity.class);
                startActivity(in1);
                break;
            case R.id.btn_signup:
                Intent in2=new Intent(getActivity(), SignUpActivity.class);
                startActivity(in2);
                break;

        }
    }
}
