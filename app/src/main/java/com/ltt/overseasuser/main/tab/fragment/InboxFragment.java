package com.ltt.overseasuser.main.tab.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lin.widget.SwipeRecyclerView;
import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseFragment;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.main.tab.fragment.activity.NotificationActivity;
import com.ltt.overseasuser.main.tab.fragment.adapter.InboxAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/18.
 */
public class InboxFragment extends BaseFragment {
    @BindView(R.id.action_bar)
    View actionBar;
    @BindView(R.id.container)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    ActionBar bar;
    private InboxAdapter adapter;

    @Override
    protected int bindLayoutID() {
        return R.layout.fragment_inbox;
    }

    @Override
    protected void prepareFragment() {
        bar = ActionBar.init(actionBar);
        bar.setTitle("Inboxs");
        bar.setLeft(R.mipmap.back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        bar.showNotify();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InboxAdapter();
        recyclerView.setAdapter(adapter);
    }


    @OnClick({R.id.iv_notify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_notify:
                startActivity(new Intent(getActivity(), NotificationActivity.class));
                break;
        }
    }
}
