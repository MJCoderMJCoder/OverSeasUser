package com.ltt.overseasuser.main.tab.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lin.widget.SwipeRecyclerView;
import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseFragment;
import com.ltt.overseasuser.base.RecyclerAdapter;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.main.tab.fragment.activity.ExploreActivity;
import com.ltt.overseasuser.main.tab.fragment.activity.NotificationActivity;
import com.ltt.overseasuser.main.tab.fragment.adapter.ExploreAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/18.
 */
public class ExploreFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.container)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.action_bar)
    View actionBar;

    private ExploreAdapter adapter;
    ActionBar bar;

    @Override
    protected int bindLayoutID() {
        return R.layout.fragment_explore;
    }

    @Override
    protected void prepareFragment() {
        bar = ActionBar.init(actionBar);
        bar.setBackground(R.mipmap.bg_title_popmach);
        bar.showNotify();
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ExploreAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object, View view, int position) {
                LinearLayout rl2 = (LinearLayout) view.findViewById(R.id.rl_2);
                LinearLayout rl3 = (LinearLayout) view.findViewById(R.id.rl_3);
                rl2.setVisibility(View.GONE);
                rl3.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick({R.id.iv_menu, R.id.iv_notify})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu:
                startActivity(new Intent(getActivity(), ExploreActivity.class));
                break;
            case R.id.iv_notify:
                startActivity(new Intent(getActivity(), NotificationActivity.class));
                break;
        }

    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
    }


}
