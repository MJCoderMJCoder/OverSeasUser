package com.ltt.overseasuser.main.tab.fragment.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lin.widget.SwipeRecyclerView;
import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.main.tab.fragment.adapter.NotificationAdapter;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/21.
 */
public class NotificationActivity extends BaseActivity {
    @BindView(R.id.status_bar)
    View statusBar;
    @BindView(R.id.iv_notify)
    ImageView ivNotify;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    @BindView(R.id.action_bar)
    LinearLayout actionBar;
    @BindView(R.id.toprecycle)
    SwipeRecyclerView toprecycle;
    @BindView(R.id.bottomrecycle)
    SwipeRecyclerView bottomrecycle;
    private NotificationAdapter adapter;
    ActionBar bar;
    @Override
    protected int bindLayoutID() {
        return R.layout.activity_notification_center;
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
        bar.showcenter();

        toprecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        bottomrecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter();
        toprecycle.setAdapter(adapter);
        bottomrecycle.setAdapter(adapter);
    }


}
