package com.ltt.overseasuser.main.tab.fragment.activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.XApplication;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2018/7/4 0004.
 */

public class RequestFinishActivity {
    private RequestActivity mParentActivity;
    private LayoutInflater mlflater;
    private TextView textTitle;
    private TextView textShow;
    public View mView;
    private String authorization = XApplication.globalUserBean.getAccess_token();


    public RequestFinishActivity(LayoutInflater lflater, RequestActivity requestActivity) {
        mParentActivity = requestActivity;
        mlflater = lflater;
        initUi();
    }

    private void initUi() {
        View finishView = mlflater.inflate(R.layout.requestfinishlayout, null);
        textTitle = (TextView) finishView.findViewById(R.id.tv_title);
        textTitle.setText("Last Request");//("Finish Reuqest");
        textShow = (TextView) finishView.findViewById(R.id.tv_placeholder);
        textShow.setText("Uploading...");//("You request has success post!");
        mView=finishView;
    }
    public void postRequst() {
        textShow.setText("questions is being submitted...");//("You request has success post!");
    }
    public void postFinishSuc() {
        textShow.setText("You request has success post!");
    }
    public void postFinishFail() {
        textShow.setText("You request has Fail post!");
    }
}
