package com.ltt.overseasuser.login;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.ltt.overseasuser.MainActivity;
import com.ltt.overseasuser.R;
import com.ltt.overseasuser.XApplication;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.http.CustomerCallBack;
import com.ltt.overseasuser.http.RetrofitUtil;
import com.ltt.overseasuser.model.GsonUserBean;
import com.ltt.overseasuser.model.LoginBean;
import com.ltt.overseasuser.model.UserBean;
import com.ltt.overseasuser.utils.PreferencesUtils;
import com.ltt.overseasuser.utils.ToastUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/17.
 */
public class LoginActivity extends BaseActivity {
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    private CallbackManager mCallbackManager;
    ActionBar bar;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_pwd)
    EditText etPwd;

    private String mEmail, mPwd;

    private LoginButton facebookButton;
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
        mCallbackManager = CallbackManager.Factory.create();

        facebookButton=(LoginButton)findViewById(R.id.btn_fblogin) ;
        // Set the initial permissions to request from the user while logging in
        facebookButton.setReadPermissions(Arrays.asList(EMAIL, USER_POSTS));
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                if(isLoggedIn){
                    loginWithFacebook(accessToken.getToken());
                }
            }
        });
        // Register a callback to respond to the user
        facebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                setResult(RESULT_OK);

                finish();
            }
            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                finish();
            }
            @Override
            public void onError(FacebookException e) {
                // Handle exception
                Log.d("onError:",e.toString());
            }
        });
    }

    @OnClick({R.id.tv_forget_pwd, R.id.iv_googlelogin, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forget_pwd:
                startActivity(new Intent(this, ForgetActivity.class));
                break;
            case R.id.iv_googlelogin:
                ToastUtils.showToast("google+ login");
                break;
            case R.id.btn_login:
                //  startActivity(new Intent(this, MainActivity.class));
                if(judgeInput()){
                    login();
                }
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
        LoginBean userParams = new LoginBean();
        userParams.setEmail(mEmail);
        userParams.setPassword(mPwd);
        Call<GsonUserBean> loginCall = RetrofitUtil.getAPIService().login(userParams);
        loginCall.enqueue(new CustomerCallBack<GsonUserBean>() {
            @Override
            public void onResponseResult(GsonUserBean response) {
                Log.d("login success....:",""+response.getCode());
                UserBean userBean=response.getData();
                if(null==userBean)
                    userBean=new UserBean();
                XApplication.globalUserBean = userBean;
                XApplication.globalUserBean.setEmail(mEmail);
                //XApplication.globalUserBean.setPassword(mPwd);
                XApplication.globalUserBean.setAccess_token(response.getAccess_token());
                PreferencesUtils.saveUserInfoPreference(XApplication.globalUserBean);
//                XApplication.globalUserBean.setAccess_token(response.getData().getAccess_token());
                dismissLoadingView();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                //getProfile();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult:",requestCode+""+resultCode);
        super.onActivityResult(requestCode, resultCode, data);

        // Log in with facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Log.d("accessToken:",""+accessToken.getToken());
        String token=accessToken.getToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn)
            loginWithFacebook(token);
    }

    private void loginWithFacebook(String token){
        Call<GsonUserBean> fbcallback = RetrofitUtil.getAPIService().callback(token);
        fbcallback.enqueue(new CustomerCallBack<GsonUserBean>() {
            @Override
            public void onResponseResult(GsonUserBean response) {
                Log.d("login success....:",""+response.getCode());
                UserBean userBean=response.getData();
                if(null==userBean)
                    userBean=new UserBean();
                XApplication.globalUserBean = userBean;
                XApplication.globalUserBean.setAccess_token(response.getAccess_token());
                PreferencesUtils.saveUserInfoPreference(XApplication.globalUserBean);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            }

            @Override
            public void onResponseError(BaseBean errorMsg, boolean isNetError) {
                // dismissLoadingView();
                //Log.d("onResponseError",errorMsg.getMsg());
            }
        });
    }
}
