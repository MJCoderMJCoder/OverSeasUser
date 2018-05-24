package com.ltt.overseasuser.main.tab.fragment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.core.ActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/18.
 */
public class MyRequestDetailActivity extends BaseActivity implements View.OnClickListener {
    ActionBar bar;
    @BindView(R.id.tv_tomessage)
    TextView tvTomessage;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_myrequest;
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
        bar.setTitle("Enquiry");
        bar.showNotify();

        tvTomessage.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);


    }

    @OnClick({R.id.tv_tomessage, R.id.tv_profile})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tomessage:
                Intent intent = new Intent(MyRequestDetailActivity.this, ChatsActivity.class);
                //                intent.putExtra("username", dataBean.getUser());
                //                intent.putExtra("request_category", dataBean.getRequest_category());
                //                intent.putExtra("conversation_id", dataBean.getConversation_id());
                startActivity(intent);
                break;
            case R.id.tv_profile:
                intent = new Intent(MyRequestDetailActivity.this, ProfileActivity.class);
                //                intent.putExtra("username", dataBean.getUser());
                //                intent.putExtra("request_category", dataBean.getRequest_category());
                //                intent.putExtra("conversation_id", dataBean.getConversation_id());
                startActivity(intent);
                break;
        }

    }

    //    @OnClick({R.id.iv_notify, R.id.btn_contact})
    //    public void onClick(View view) {
    //        switch (view.getId()) {
    //            case R.id.iv_notify:
    //                startActivity(new Intent(this, NotificationActivity.class));
    //                break;
    //            case R.id.btn_contact:
    //                startActivity(new Intent(this, RechareActivity.class));
    //                break;
    //        }
    //    }
}
