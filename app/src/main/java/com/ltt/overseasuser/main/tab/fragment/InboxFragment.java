package com.ltt.overseasuser.main.tab.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private InboxAdapter adapter;

    private List<MessageListBean.DataBean> mMessageLists = new ArrayList<>();

    @Override
    protected int bindLayoutID() {
        return R.layout.fragment_inbox;
    }

    @Override
    protected void prepareFragment() {
        bar = ActionBar.init(actionBar);
        bar.setTitle("Inbox");
        bar.setLeft(R.mipmap.back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        bar.showNotify();
        setRefresh();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InboxAdapter(mMessageLists);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object, View view, int position) {
                L.e(TAG, "---" + "");
                MessageListBean.DataBean dataBean = mMessageLists.get(position);
                Intent intent = new Intent(getActivity(), ChatsActivity.class);
                intent.putExtra("username", dataBean.getUser());
                intent.putExtra("request_category", dataBean.getRequest_category());
                intent.putExtra("conversation_id", dataBean.getConversation_id());
                startActivity(intent);
            }
        });
        initData();

    }

    /**刷新界面信息*/
    private void setRefresh() {
        refreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        refreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        ToastUtils.showToast("Refresh the data");
                        // 加载完数据设置为不刷新状态，将下拉进度收起来
                        refreshLayout.setRefreshing(false);
                    }
                }, 1200);
            }
        });
    }

    // TODO: 2018/5/8 Request message list information.
    protected void initData() {
        Call<MessageListBean> messageLists = RetrofitUtil.getAPIService().getMessageLists(1);
        messageLists.enqueue(new CustomerCallBack<MessageListBean>() {
            @Override
            public void onResponseResult(MessageListBean messageListBean) {
                L.e(TAG + "---" + messageListBean.getTotal_message() + "---" + messageListBean.getCode());
                List<MessageListBean.DataBean> data = messageListBean.getData();
                if (data == null) {
                    adapter.notifyDataSetChanged();
                    ToastUtils.showToast("No MessageList!");
                } else {
                    mMessageLists.addAll(data);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {

            }
        });
    }


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
