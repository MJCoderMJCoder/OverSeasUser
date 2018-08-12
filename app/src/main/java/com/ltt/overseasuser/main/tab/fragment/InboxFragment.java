package com.ltt.overseasuser.main.tab.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lin.widget.SwipeRecyclerView;
import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.base.BaseFragment;
import com.ltt.overseasuser.base.RecyclerAdapter;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.http.CustomerCallBack;
import com.ltt.overseasuser.http.RetrofitUtil;
import com.ltt.overseasuser.main.tab.fragment.activity.ChatsActivity;
import com.ltt.overseasuser.main.tab.fragment.activity.NotificationActivity;
import com.ltt.overseasuser.main.tab.fragment.adapter.InboxAdapter;
import com.ltt.overseasuser.model.MessageListBean;
import com.ltt.overseasuser.utils.L;
import com.ltt.overseasuser.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/18.
 */
public class InboxFragment extends BaseFragment {
    @BindView(R.id.action_bar)
    View actionBar;
    @BindView(R.id.container)
    SwipeRecyclerView recyclerView;
    ActionBar bar;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    Unbinder unbinder;
    @BindView(R.id.ll_loadmore)
    LinearLayout llLoadmore;
    private InboxAdapter adapter;

    private List<MessageListBean.DataBean> mMessageLists = new ArrayList<>();

    private List<String> chatUserName = new ArrayList<>();

    private int mNextRequestPage = 1;
    private int mTotalMessages = 1;
    private static final int PAGE_SIZE = 10;

    @Override
    protected int bindLayoutID() {
        return R.layout.fragment_inbox;
    }

    @Override
    protected void prepareFragment() {
        bar = ActionBar.init(actionBar);
        bar.setTitle("My Message");
//        bar.setLeft(R.mipmap.back, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        bar.showNotify();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InboxAdapter(R.layout.item_fragment_inbox,mMessageLists);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MessageListBean.DataBean dataBean = mMessageLists.get(position);
                Intent intent = new Intent(getActivity(), ChatsActivity.class);
                intent.putExtra("username", dataBean.getCurrent_user().getName());
                intent.putExtra("request_category", dataBean.getRequest_category());
                intent.putExtra("conversation_id", dataBean.getConversation_id());
//                intent.putExtra("response_id", dataBean.getResponse_id());
                intent.putExtra("date_created", dataBean.getDate_created());
                intent.putExtra("request_id", dataBean.getRequest_id());
                intent.putExtra("opposite_user", dataBean.getOpposite_user().getName());
                startActivity(intent);
            }
        });
//        initData();
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });

        initRefreshLayout();
        refreshLayout.setRefreshing(true);
        setRefresh();
    }

    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefresh();
            }
        });
    }

    // load more
    private void loadMore() {
        Call<MessageListBean> messageLists = RetrofitUtil.getAPIService().getMessageLists(mNextRequestPage);
        messageLists.enqueue(new CustomerCallBack<MessageListBean>() {
            @Override
            public void onResponseResult(MessageListBean messageListBean) {
//                L.e(TAG + "---" + messageListBean.getTotal_message() + "---" + messageListBean.getCode());
                List<MessageListBean.DataBean> data = messageListBean.getData();
//                if (data == null) {
//                    adapter.notifyDataSetChanged();
//                    ToastUtils.showToast("No MessageList");
//                } else {
//                    mMessageLists.addAll(data);
//                    adapter.notifyDataSetChanged();
//                }
                setData(false, data);
            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                adapter.loadMoreFail();
            }
        });
    }

    /**
     * Refresh data
     */
    private void setRefresh() {
        mNextRequestPage = 1;
        Call<MessageListBean> messageLists = RetrofitUtil.getAPIService().getMessageLists(mNextRequestPage);
        messageLists.enqueue(new CustomerCallBack<MessageListBean>() {
            @Override
            public void onResponseResult(MessageListBean messageListBean) {
                L.e(TAG + "---" + messageListBean.getTotal_message() + "---" + messageListBean.getCode());
                int total_message = messageListBean.getTotal_message();
                mTotalMessages = total_message;
                List<MessageListBean.DataBean> data = messageListBean.getData();
//                if (data == null) {
//                    adapter.notifyDataSetChanged();
//                    ToastUtils.showToast("No MessageList");
//                } else {
//                    mMessageLists.addAll(data);
//                    adapter.notifyDataSetChanged();
//                }
                setData(true, data);
                adapter.setEnableLoadMore(true);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                adapter.setEnableLoadMore(true);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void setData(boolean isRefresh, List<MessageListBean.DataBean> data) {
        mNextRequestPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            adapter.setNewData(data);
            mMessageLists.clear();
            mMessageLists.addAll(data);
        } else {
            if (size > 0) {
                adapter.addData(data);
                mMessageLists.addAll(data);
            }
        }
        if (size < PAGE_SIZE) {
            //The first page does not display no more data layout if it is not enough for one page
            adapter.loadMoreEnd(isRefresh);
            Toast.makeText(getContext(), "no more data", Toast.LENGTH_SHORT).show();
        } else {
            adapter.loadMoreComplete();
        }
        adapter.notifyDataSetChanged();
    }

    // TODO: 2018/5/8 Request message list information.
//    protected void initData() {
//        Call<MessageListBean> messageLists = RetrofitUtil.getAPIService().getMessageLists(1);
//        messageLists.enqueue(new CustomerCallBack<MessageListBean>() {
//            @Override
//            public void onResponseResult(MessageListBean messageListBean) {
//                L.e(TAG + "---" + messageListBean.getTotal_message() + "---" + messageListBean.getCode());
//                List<MessageListBean.DataBean> data = messageListBean.getData();
//                if (data == null) {
//                    adapter.notifyDataSetChanged();
//                    ToastUtils.showToast("No MessageList");
//                } else {
//                    mMessageLists.addAll(data);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
//
//            }
//        });
//    }


    @OnClick({R.id.iv_notify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_notify:
                startActivity(new Intent(getActivity(), NotificationActivity.class));
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
