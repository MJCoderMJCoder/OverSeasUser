package com.ltt.overseasuser.main.tab.fragment.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.http.RetrofitUtil;
import com.ltt.overseasuser.main.tab.fragment.adapter.PreferenceChildRecycerview;
import com.ltt.overseasuser.main.tab.fragment.adapter.PreferenceParentRecycerview;
import com.ltt.overseasuser.model.PreferenceListBean;
import com.ltt.overseasuser.utils.RecyclerDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yunwen on 2018/5/14.
 */

public class ChoosePreferenceActivity extends BaseActivity {
    @BindView(R.id.rv_parent_preference)
    RecyclerView rvParentPreference;
    @BindView(R.id.rv_child_preference)
    RecyclerView rvChildPreference;
    private PreferenceParentRecycerview preferenceParentRecycerviewAdapter;
    private PreferenceChildRecycerview preferenceChildRecycerviewAdapter;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_choose_preference;
    }

    @Override
    protected void prepareActivity() {
        initDate();
        rvParentPreference.setLayoutManager(new LinearLayoutManager(ChoosePreferenceActivity.this, LinearLayoutManager.VERTICAL, false));
        rvParentPreference.addItemDecoration(new RecyclerDivider(this, LinearLayoutManager.HORIZONTAL));
        rvChildPreference.setLayoutManager(new LinearLayoutManager(ChoosePreferenceActivity.this, LinearLayoutManager.VERTICAL, false));
        rvChildPreference.addItemDecoration(new RecyclerDivider(this, LinearLayoutManager.HORIZONTAL));
    }

    private void initDate() {
        Call<PreferenceListBean> preferenceLists = RetrofitUtil.getAPIService().getPreferenceLists();
        preferenceLists.enqueue(new Callback<PreferenceListBean>() {
            @Override
            public void onResponse(Call<PreferenceListBean> call, Response<PreferenceListBean> response) {
                List<PreferenceListBean.DataBean> data =response.body().getData();
                    if (preferenceParentRecycerviewAdapter==null){
                       preferenceParentRecycerviewAdapter = new PreferenceParentRecycerview(ChoosePreferenceActivity.this, data);
                    }
                rvParentPreference.setAdapter(preferenceParentRecycerviewAdapter);
                if (preferenceChildRecycerviewAdapter==null){
                    preferenceChildRecycerviewAdapter = new PreferenceChildRecycerview(ChoosePreferenceActivity.this, data,0);
                }
                  rvChildPreference.setAdapter(preferenceChildRecycerviewAdapter);

            }

            @Override
            public void onFailure(Call<PreferenceListBean> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
