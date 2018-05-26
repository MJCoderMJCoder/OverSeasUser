package com.ltt.overseasuser.main.tab.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lin.widget.SwipeRecyclerView;
import com.ltt.overseasuser.R;
import com.ltt.overseasuser.XApplication;
import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.base.BaseFragment;
import com.ltt.overseasuser.base.RecyclerAdapter;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.http.CustomerCallBack;
import com.ltt.overseasuser.http.RetrofitUtil;
import com.ltt.overseasuser.main.tab.fragment.activity.MyRequestDetailActivity;
import com.ltt.overseasuser.main.tab.fragment.activity.NotificationActivity;
import com.ltt.overseasuser.main.tab.fragment.adapter.MyDealAdapter;
import com.ltt.overseasuser.main.tab.fragment.adapter.MyRequestAdapter;
import com.ltt.overseasuser.model.MyRequestListBean;
import com.ltt.overseasuser.model.MyResponseBean;
import com.ltt.overseasuser.model.MyResponseListBean;
import com.ltt.overseasuser.utils.L;
import com.ltt.overseasuser.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/18.
 * <p>
 * account:awesome@luqman.rocks
 * password:test123（R）
 * or password：luqman123（E）
 * <p>
 * 1. Response is for service provider
 * 2. Request is for user who answer the question and submit the request for service provider to response it
 * 3. Coins we don’t have for now and as our main website currently user can post unlimited request and with manual approval from our backend administrator
 * 4. For avatar and for now we not store any images for user yet , once we ready then we enable to feature
 * 4. The current website do not have avatar function, please us static avatar for every user.
 * 5. For Service provider we only have create response and list of response and message
 * <p>
 * 6.详情界面：For view the request you can refer endpoint Service > MAIN > view request
 * For list of request please refer to Service > User > list of request
 * For list of response from request please refer to Service > User > list of response
 * For view the request you can refer endpoint Service > MAIN > view request
 * <p>
 * For my deal endpoint is not available since our part not finalize the flow of deal to be implement , mean while deal page is not available for temporary.
 * For service provider company profile is not available yet until future i will keep update it inside the same documentation postman.
 */
public class TaskFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.container)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.id_lv)
    ListView listview;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.action_bar)
    View actionBar;
    @BindView(R.id.tv_title_center)
    TextView tvTitle;
    //    private TaskAdapter adapter;
    //    private TaskFinishedAdapter finishedAdapter;
    //    private TaskUnlockedAdapter taskUnlockedadapter;
    //    private AllTaskAdapter allTaskAdapter;
    private MyRequestAdapter myRequestAdapter;
    private MyDealAdapter myDealAdapter;
    ActionBar bar;
    private ArrayList<String> list = new ArrayList<String>();
    private String authorization = XApplication.globalUserBean.getAccess_token();
    private final String TAG = "(╯‵□′)╯︵┻━┻ 走你！";

    @Override
    protected int bindLayoutID() {
        return R.layout.fragment_task;
    }

    @Override
    protected void prepareFragment() {
        bar = ActionBar.init(actionBar);
        bar.showNotify();
        bar.setTitle("My Requests");
        bar.setLeft(R.mipmap.back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_text, getData());
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerLayout.closeDrawers();
                tvTitle.setText(list.get(i));
                showLoadingView();
                changeUi(i);
            }
        });
    }

    private void changeUi(int i) {
        switch (i) {
            case 0:
                Call<MyRequestListBean> requestListBeanCall = RetrofitUtil.getAPIService().getRequestList(1 + "", authorization);
                requestListBeanCall.enqueue(new CustomerCallBack<MyRequestListBean>() {
                    @Override
                    public void onResponseResult(final MyRequestListBean response) {
                        L.v(TAG, response + "");
                        dismissLoadingView();
                        if (response.isStatus()) {
                            myRequestAdapter = new MyRequestAdapter(null, response.getData());
                            recyclerView.setAdapter(myRequestAdapter);
                            myRequestAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(Object object, View view, int position) {
                                    final String request_name = response.getData().get(position).getRequest_name();
                                    showLoadingView();
                                    Call<MyResponseListBean> requestListBeanCall = RetrofitUtil.getAPIService().getResponseList(response.getData().get(position).getRequest_id(), 1 + "", authorization);
                                    requestListBeanCall.enqueue(new CustomerCallBack<MyResponseListBean>() {
                                        @Override
                                        public void onResponseResult(final MyResponseListBean response) {
                                            dismissLoadingView();
                                            if (response.isStatus()) {
                                                myRequestAdapter = new MyRequestAdapter(request_name, response.getData());
                                                recyclerView.setAdapter(myRequestAdapter);
                                                myRequestAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(Object object, View view, int position) {
                                                        Intent intent = new Intent(getActivity(), MyRequestDetailActivity.class);
                                                        MyResponseBean responseBean = response.getData().get(position);
                                                        intent.putExtra("request_id", responseBean.getRequest_id());
                                                        intent.putExtra("conversation_id", responseBean.getConversation_id());
                                                        intent.putExtra("request_name", request_name);
                                                        intent.putExtra("service_provider", responseBean.getService_provider());
                                                        intent.putExtra("date_created", responseBean.getDate_created());
                                                        startActivity(intent);
                                                    }
                                                });
                                            } else {
                                                ToastUtils.showToast(response.getMsg());
                                                myRequestAdapter = new MyRequestAdapter(null, null);
                                                recyclerView.setAdapter(myRequestAdapter);
                                            }
                                        }

                                        @Override
                                        public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                                            dismissLoadingView();
                                            if (errorMessage != null) {
                                                ToastUtils.showToast(errorMessage.getMsg());
                                                myRequestAdapter = new MyRequestAdapter(null, null);
                                                recyclerView.setAdapter(myRequestAdapter);
                                            } else {
                                                ToastUtils.showToast("isNetError：" + isNetError);
                                                myRequestAdapter = new MyRequestAdapter(null, null);
                                                recyclerView.setAdapter(myRequestAdapter);
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            ToastUtils.showToast(response.getMsg());
                            myRequestAdapter = new MyRequestAdapter(null, null);
                            recyclerView.setAdapter(myRequestAdapter);
                        }
                    }

                    @Override
                    public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                        dismissLoadingView();
                        if (errorMessage != null) {
                            ToastUtils.showToast(errorMessage.getMsg());
                            myRequestAdapter = new MyRequestAdapter(null, null);
                            recyclerView.setAdapter(myRequestAdapter);
                        } else {
                            ToastUtils.showToast("isNetError：" + isNetError);
                            myRequestAdapter = new MyRequestAdapter(null, null);
                            recyclerView.setAdapter(myRequestAdapter);
                        }
                    }
                });
                break;
            //            case 1:
            //                dismissLoadingView();
            //                List<String> stringList = new ArrayList<String>();
            //                for (int j = 0; j < 10; j++) {
            //                    stringList.add("");
            //                }
            //                myDealAdapter = new MyDealAdapter(stringList);
            //                recyclerView.setAdapter(myDealAdapter);
            //                myDealAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            //                    @Override
            //                    public void onItemClick(Object object, View view, int position) {
            //                        startActivity(new Intent(getActivity(), MyRequestDetailActivity.class));
            //                    }
            //                });
            //                break;
            //            case 2:
            //                taskUnlockedadapter = new TaskUnlockedAdapter();
            //                recyclerView.setAdapter(taskUnlockedadapter);
            //                break;
            //            case 3:
            //                finishedAdapter = new TaskFinishedAdapter();
            //                recyclerView.setAdapter(finishedAdapter);
            //                break;
        }
    }

    @Override
    public void onRefresh() {
        if ("My Request".equals(tvTitle.getText().toString())) {
            refreshLayout.setRefreshing(false);
            listview.performItemClick(listview, 0, 0);
        }/* else if ("My Deal".equals(tvTitle.getText().toString())) {
            refreshLayout.setRefreshing(false);
            listview.performItemClick(listview, 1, 1);
        }*/
    }


    @OnClick({R.id.iv_menu, R.id.iv_left, R.id.iv_notify})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_notify:
                startActivity(new Intent(getActivity(), NotificationActivity.class));
                break;
            case R.id.iv_menu:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.iv_left:
                drawerLayout.closeDrawers();
                break;
        }

    }

    private ArrayList<String> getData() {
        //        list.add("All My Task");
        //        list.add("Pin");
        //        list.add("Unlocked");
        //        list.add("Finished");
        list.add("My Requests");
        //        list.add("My Deal");
        return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);

        Call<MyRequestListBean> requestListBeanCall = RetrofitUtil.getAPIService().getRequestList(1 + "", authorization);
        requestListBeanCall.enqueue(new CustomerCallBack<MyRequestListBean>() {
            @Override
            public void onResponseResult(final MyRequestListBean response) {
                L.v(TAG, response + "");
                if (response.isStatus()) {
                    myRequestAdapter = new MyRequestAdapter(null, response.getData());
                    recyclerView.setAdapter(myRequestAdapter);
                    myRequestAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Object object, View view, int position) {
                            final String request_name = response.getData().get(position).getRequest_name();
                            showLoadingView();
                            Call<MyResponseListBean> requestListBeanCall = RetrofitUtil.getAPIService().getResponseList(response.getData().get(position).getRequest_id(), 1 + "", authorization);
                            requestListBeanCall.enqueue(new CustomerCallBack<MyResponseListBean>() {
                                @Override
                                public void onResponseResult(final MyResponseListBean response) {
                                    dismissLoadingView();
                                    if (response.isStatus()) {
                                        myRequestAdapter = new MyRequestAdapter(request_name, response.getData());
                                        recyclerView.setAdapter(myRequestAdapter);
                                        myRequestAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(Object object, View view, int position) {
                                                Intent intent = new Intent(getActivity(), MyRequestDetailActivity.class);
                                                MyResponseBean responseBean = response.getData().get(position);
                                                intent.putExtra("request_id", responseBean.getRequest_id());
                                                intent.putExtra("conversation_id", responseBean.getConversation_id());
                                                intent.putExtra("request_name", request_name);
                                                intent.putExtra("service_provider", responseBean.getService_provider());
                                                intent.putExtra("date_created", responseBean.getDate_created());
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        ToastUtils.showToast(response.getMsg());
                                        myRequestAdapter = new MyRequestAdapter(null, null);
                                        recyclerView.setAdapter(myRequestAdapter);
                                    }
                                }

                                @Override
                                public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                                    dismissLoadingView();
                                    if (errorMessage != null) {
                                        ToastUtils.showToast(errorMessage.getMsg());
                                        myRequestAdapter = new MyRequestAdapter(null, null);
                                        recyclerView.setAdapter(myRequestAdapter);
                                    } else {
                                        ToastUtils.showToast("isNetError：" + isNetError);
                                        myRequestAdapter = new MyRequestAdapter(null, null);
                                        recyclerView.setAdapter(myRequestAdapter);
                                    }
                                }
                            });
                        }
                    });
                } else {
                    myRequestAdapter = new MyRequestAdapter(null, null);
                    recyclerView.setAdapter(myRequestAdapter);
                }
            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                if (errorMessage != null) {
                    myRequestAdapter = new MyRequestAdapter(null, null);
                    recyclerView.setAdapter(myRequestAdapter);
                } else {
                    myRequestAdapter = new MyRequestAdapter(null, null);
                    recyclerView.setAdapter(myRequestAdapter);
                }
            }
        });
        return rootView;
    }
}
