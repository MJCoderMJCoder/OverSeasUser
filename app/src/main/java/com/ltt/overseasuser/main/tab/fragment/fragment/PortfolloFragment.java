package com.ltt.overseasuser.main.tab.fragment.fragment;

import android.support.v7.widget.GridLayoutManager;

import com.lin.widget.SwipeRecyclerView;
import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseFragment;
import com.ltt.overseasuser.main.tab.fragment.adapter.PortFolloAdapter;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/24.
 */
public class PortfolloFragment extends BaseFragment {
    @BindView(R.id.container)
    SwipeRecyclerView recyclerView;
    private PortFolloAdapter adapter;
    @Override
    protected int bindLayoutID() {
        return R.layout.fragment_portfollo;
    }

    @Override
    protected void prepareFragment() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false));
        adapter = new PortFolloAdapter();
        recyclerView.setAdapter(adapter);
    }

}
