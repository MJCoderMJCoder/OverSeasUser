package com.ltt.overseasuser.login;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.ltt.overseasuser.MainActivity;
import com.ltt.overseasuser.R;
import com.ltt.overseasuser.XApplication;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.http.CustomerCallBack;
import com.ltt.overseasuser.http.RetrofitUtil;
import com.ltt.overseasuser.model.GsonUserBean;
import com.ltt.overseasuser.model.UserBean;
import com.ltt.overseasuser.utils.PreferencesUtils;
import com.ltt.overseasuser.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/17.
 */
public class LoginActivity extends BaseActivity {
    ActionBar bar;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_pwd)
    EditText etPwd;

    private String mEmail, mPwd;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_login;
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
        bar.setTitle("LOG IN");
    }

    @OnClick({R.id.tv_forget_pwd, R.id.iv_fblogin, R.id.iv_googlelogin, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forget_pwd:
                startActivity(new Intent(this, ForgetActivity.class));
                break;
            case R.id.iv_fblogin:
                ToastUtils.showToast("facebook login");
                break;
            case R.id.iv_googlelogin:
                ToastUtils.showToast("google+ login");
                break;
            case R.id.btn_login:
//                startActivity(new Intent(this, MainActivity.class));
//                if(judgeInput()){
//                    login();
//                }
                break;

        }
    }

    private boolean judgeInput(){
        boolean canLogin = true;
        mEmail = etEmail.getText().toString();
        mPwd = etPwd.getText().toString();
        if (mEmail.trim().isEmpty()){
            ToastUtils.showToast("Email Can Not Be Empty");
            canLogin = false;
        }
        if (mPwd.trim().isEmpty()){
            ToastUtils.showToast("Password Can Not Be Empty");
            canLogin = false;
        }
        return canLogin;
    }

    private void login(){
        showLoadingView();
        UserBean userParams = new UserBean();
        userParams.setEmail(mEmail);
        userParams.setPassword(mPwd);
        Call<GsonUserBean> loginCall = RetrofitUtil.getAPIService().login(userParams);
        loginCall.enqueue(new CustomerCallBack<GsonUserBean>() {
            @Override
            public void onResponseResult(GsonUserBean response) {
                XApplication.globalUserBean = response.getData();
                XApplication.globalUserBean.setEmail(mEmail);
                XApplication.globalUserBean.setPassword(mPwd);
                PreferencesUtils.saveUserInfoPreference(XApplication.globalUserBean);
//                XApplication.globalUserBean.setAccess_token(response.getData().getAccess_token());
                getProfile();
            }

            @Override
            public void onResponseError(BaseBean errorMsg, boolean isNetError) {
                dismissLoadingView();
            }
        });
    }

    private void getProfile(){
        showLoadingView();
        Call<GsonUserBean> call = RetrofitUtil.getAPIService().getProfile();
        call.enqueue(new CustomerCallBack<GsonUserBean>() {
            @Override
            public void onResponseResult(GsonUserBean response) {
                dismissLoadingView();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onResponseError(BaseBean errorMsg, boolean isNetError) {
                dismissLoadingView();
            }
        });
    }

}
