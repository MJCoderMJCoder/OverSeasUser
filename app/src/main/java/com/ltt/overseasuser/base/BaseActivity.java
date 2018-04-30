package com.ltt.overseasuser.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;


import com.ltt.overseasuser.R;
import com.ltt.overseasuser.XApplication;

import butterknife.ButterKnife;
import io.reactivex.ObservableTransformer;

/**
 * Created by lin on 2017/7/16.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG = getClass().getSimpleName();
    public boolean isForGround = false;

    private ProgressDialog loadingView;

    private TagOperator operator = new TagOperator();
    private ObservableTransformer transformer = new RxTransformer(operator);

    protected abstract int bindLayoutID();

    protected abstract void prepareActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreen();
        setContentView(bindLayoutID());
        ButterKnife.bind(this);
        prepareActivity();
        XApplication.getApplication().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForGround = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForGround = false;
    }

    @Override
    protected void onDestroy() {
        operator.onDestroy();
        super.onDestroy();
    }

    public void setFullScreen(){
    }

    public ObservableTransformer transform() {
        return transformer;
    }

    protected Context getContext() {
        return this;
    }

    protected void showLoadingView() {
        if (loadingView == null) {
            loadingView = new ProgressDialog(this);
            loadingView.setMessage(getString(R.string.loading_view_msg));
            loadingView.setCanceledOnTouchOutside(false);
            loadingView.setCancelable(false);
        }
        if (!loadingView.isShowing())
            loadingView.show();
    }

    protected void dismissLoadingView() {
        if (loadingView != null && loadingView.isShowing())
            loadingView.dismiss();
    }

    @Override
    public void finish() {
        dismissLoadingView();
        super.finish();
    }
}
