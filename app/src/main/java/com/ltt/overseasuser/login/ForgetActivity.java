package com.ltt.overseasuser.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.http.CustomerCallBack;
import com.ltt.overseasuser.http.RetrofitUtil;
import com.ltt.overseasuser.model.GsonUserBean;
import com.ltt.overseasuser.model.UserBean;
import com.ltt.overseasuser.utils.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/17.
 */
public class ForgetActivity extends BaseActivity {
    ActionBar bar;
    @BindView(R.id.et_email)
    EditText etEmail;
    private String mEmail;
    @Override
    protected int bindLayoutID() {
        return R.layout.activity_foget_password;
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
        bar.setTitle("FORGET PASSWORD");
    }

    @OnClick(R.id.btn_forget)
    public void onClick() {
        if(judgeInput()) {
            forget();
        }
        //finish();
    }
    public void forget(){
        showLoadingView();
        UserBean userParams = new UserBean();
        userParams.setEmail(mEmail);
        Call<GsonUserBean> loginCall = RetrofitUtil.getAPIService().forget(userParams);
        loginCall.enqueue(new CustomerCallBack<GsonUserBean>() {
            @Override
            public void onResponseResult(GsonUserBean response) {
                int code=response.getCode();
                Log.d("code:", ""+code);
                if(code==200) {
                    startActivity(new Intent(ForgetActivity.this, EmailSendActivity.class));
                    finish();
                }
            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                dismissLoadingView();
            }
        });
    }
    private boolean judgeInput(){
        boolean canLogin = true;
        mEmail = etEmail.getText().toString();
        Log.d("judgeInput:",mEmail.contains("@")+":"+mEmail.indexOf(".")+":"+mEmail.indexOf("@"));
        if (mEmail.trim().isEmpty()){
            ToastUtils.showToast("Email Can Not Be Empty");
            canLogin = false;
        }
        if( !mEmail.contains("@") || mEmail.indexOf(".")<mEmail.indexOf("@")){

            ToastUtils.showToast("Email Format Is Not Correct");
            canLogin = false;
        }
        return canLogin;
    }
}
