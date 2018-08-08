package com.ltt.overseasuser.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.ltt.overseasuser.model.SignTokenBean;
import com.ltt.overseasuser.model.UserBean;
import com.ltt.overseasuser.model.UserNewBean;
import com.ltt.overseasuser.utils.PreferencesUtils;
import com.ltt.overseasuser.utils.SPUtils;
import com.ltt.overseasuser.utils.ToastUtils;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/17.
 */
public class LoginActivity extends BaseActivity {
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    @BindView(R.id.iv_googlelogin)
    SignInButton ivGooglelogin;
    private CallbackManager mCallbackManager;
    ActionBar bar;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_pwd)
    EditText etPwd;

    private String mEmail, mPwd;

    private LoginButton facebookButton;
    private GoogleSignInClient mGoogleSignInClient;

    private int RC_SIGN_IN = 1001;
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;

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
        mAuth = FirebaseAuth.getInstance();
        facebookButton = (LoginButton) findViewById(R.id.btn_fblogin);
        // Set the initial permissions to request from the user while logging in
//        facebookButton.setReadPermissions(Arrays.asList(EMAIL, USER_POSTS));
//        facebookButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AccessToken accessToken = AccessToken.getCurrentAccessToken();
//                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
//                if (isLoggedIn) {
//                    loginWithFacebook(accessToken.getToken());
//                }
//            }
//        });
//        // Register a callback to respond to the user
//        facebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//                Log.e("返回信息-", loginResult.getAccessToken().getToken());
//                loginWithFacebook(loginResult.getAccessToken().getToken());
////                setResult(RESULT_OK);
////
////                finish();
//
//            }
//
//            @Override
//            public void onCancel() {
//                setResult(RESULT_CANCELED);
//                finish();
//            }
//
//            @Override
//            public void onError(FacebookException e) {
//                // Handle exception
//                Log.d("onError:", e.toString());
//            }
//        });
        callbackManager = CallbackManager.Factory.create();
        facebookButton.setReadPermissions("email");


        // Callback registration
        facebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("sss1", loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                // App code
                Log.e("ssss2", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("ssss3", exception.getMessage());
            }
        });

//        facebookButton.setReadPermissions("email", "public_profile");
//        facebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.e(TAG, "facebook:onSuccess:" + loginResult );
//                Log.e(TAG, "facebook:onSuccess:" + loginResult.getAccessToken().getToken() );
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.e(TAG, "facebook:onCancel");
//                // [START_EXCLUDE]
//                updateFacebookUI(null);
//                // [END_EXCLUDE]
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.e(TAG, "facebook:onError", error);
//                // [START_EXCLUDE]
//                updateFacebookUI(null);
//                // [END_EXCLUDE]
//            }
//        });
        // [END initialize_fblogin]

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ivGooglelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick({R.id.tv_forget_pwd, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forget_pwd:
                startActivity(new Intent(this, ForgetActivity.class));
                break;
            case R.id.btn_login:
                //  startActivity(new Intent(this, MainActivity.class));
                if (judgeInput()) {
                    login();
                }
                break;

        }
    }

    private boolean judgeInput() {
        boolean canLogin = true;
        mEmail = etEmail.getText().toString();
        mPwd = etPwd.getText().toString();
        if (mEmail.trim().isEmpty()) {
            ToastUtils.showToast("Email Can Not Be Empty");
            canLogin = false;
        }
        if (mPwd.trim().isEmpty()) {
            ToastUtils.showToast("Password Can Not Be Empty");
            canLogin = false;
        }
        return canLogin;
    }

    private void login() {
        showLoadingView();
        LoginBean userParams = new LoginBean();
        userParams.setEmail(mEmail);
        userParams.setPassword(mPwd);
        Call<GsonUserBean> loginCall = RetrofitUtil.getAPIService().login(userParams);
        loginCall.enqueue(new CustomerCallBack<GsonUserBean>() {
            @Override
            public void onResponseResult(GsonUserBean response) {
                Log.d("login success....:", "" + response.getCode());
                UserNewBean userBean = response.getData();
                if (null == userBean)
                    userBean = new UserNewBean();
                JSONObject tags = new JSONObject();
                try {
                    tags.put("user_id", userBean.getUser_id() + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OneSignal.sendTags(tags);
                XApplication.globalUserBean = userBean;
//                XApplication.globalUserBean.setEmail(mEmail);
                //XApplication.globalUserBean.setPassword(mPwd);
                XApplication.globalUserBean.setAccess_token(response.getAccess_token());
                PreferencesUtils.saveUserInfoPreference(XApplication.globalUserBean);
//                XApplication.globalUserBean.setAccess_token(response.getData().getAccess_token());
                dismissLoadingView();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                //getProfile();

                setCostomToken();
            }

            @Override
            public void onResponseError(BaseBean errorMsg, boolean isNetError) {
                dismissLoadingView();
            }
        });
    }

    private void setCostomToken() {
        Call<SignTokenBean> signTokenCall = RetrofitUtil.getAPIService().getSignToken();
        signTokenCall.enqueue(new CustomerCallBack<SignTokenBean>() {
            @Override
            public void onResponseResult(SignTokenBean response) {
                Log.e("sss", response + "");
                String sign_in_custom_token = response.getSign_in_custom_token();
                SPUtils.putString("SIGN_IN_CUSTOM_TOKEN",sign_in_custom_token);
            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {

            }
        });
    }

    private void getProfile() {
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
        if (requestCode == RESULT_OK) {

        } else if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
            Log.d("onActivityResult:", requestCode + "" + resultCode);
            super.onActivityResult(requestCode, resultCode, data);

            // Log in with facebook
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            Log.d("accessToken:", "" + accessToken.getToken());
            String token = accessToken.getToken();
            boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
            if (isLoggedIn)
                loginWithFacebook(token);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount acct) {
//        String idToken = account.getIdToken();
//        loginWithGoogle(idToken);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            String idToken = acct.getIdToken();
            if (idToken != null) {
                loginWithGoogle(idToken);
            }
            Log.e(TAG, "---" + personName + "---" + personGivenName + "---" + personFamilyName + "---" + personEmail + "---" + personId + "---" + personPhoto + "---" +idToken);
        }
    }

    private void loginWithGoogle(String idToken) {
        Call<GsonUserBean> fbcallback = RetrofitUtil.getAPIService().googlelogin(idToken);
        fbcallback.enqueue(new CustomerCallBack<GsonUserBean>() {
            @Override
            public void onResponseResult(GsonUserBean response) {
                Log.d("login success....:", "" + response.getCode());
                UserNewBean userBean = response.getData();
                if (null == userBean)
                    userBean = new UserNewBean();
                JSONObject tags = new JSONObject();
                try {
                    tags.put("user_id", userBean.getUser_id() + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                XApplication.globalUserBean = userBean;
                XApplication.globalUserBean.setAccess_token(response.getAccess_token());
                PreferencesUtils.saveUserInfoPreference(XApplication.globalUserBean);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                //sign out
                setCostomToken();
                signOut();
            }

            @Override
            public void onResponseError(BaseBean errorMsg, boolean isNetError) {
                // dismissLoadingView();
                //Log.d("onResponseError",errorMsg.getMsg());
            }
        });
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void loginWithFacebook(String token) {
        Call<GsonUserBean> fbcallback = RetrofitUtil.getAPIService().callback(token);
        fbcallback.enqueue(new CustomerCallBack<GsonUserBean>() {
            @Override
            public void onResponseResult(GsonUserBean response) {
                Log.d("login success....:", "" + response.getCode());
                UserNewBean userBean = response.getData();
                if (null == userBean)
                    userBean = new UserNewBean();
                JSONObject tags = new JSONObject();
                try {
                    tags.put("user_id", userBean.getUser_id() + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                XApplication.globalUserBean = userBean;
                XApplication.globalUserBean.setAccess_token(response.getAccess_token());
                PreferencesUtils.saveUserInfoPreference(XApplication.globalUserBean);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                setCostomToken();
            }

            @Override
            public void onResponseError(BaseBean errorMsg, boolean isNetError) {
                // dismissLoadingView();
                //Log.d("onResponseError",errorMsg.getMsg());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.e(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        showLoadingView();
        // [END_EXCLUDE]
        loginWithFacebook(token.getToken());
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.e(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateFacebookUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.e(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateFacebookUI(null);
//                        }
//
//                        // [START_EXCLUDE]
//                        dismissLoadingView();
//                        // [END_EXCLUDE]
//                    }
//                });
    }

    private void updateFacebookUI(FirebaseUser user) {

    }
    // [END auth_with_facebook]
}
