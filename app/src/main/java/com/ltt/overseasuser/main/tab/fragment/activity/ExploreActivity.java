package com.ltt.overseasuser.main.tab.fragment.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.base.RecyclerAdapter;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.http.CustomerCallBack;
import com.ltt.overseasuser.http.RetrofitUtil;
import com.ltt.overseasuser.main.tab.fragment.adapter.SectionListAdapter;
import com.ltt.overseasuser.main.tab.fragment.adapter.TypeListAdapter;
import com.ltt.overseasuser.model.PhoneListBean;
import com.ltt.overseasuser.model.SectionBean;
import com.ltt.overseasuser.model.SectionListBean;
import com.ltt.overseasuser.model.TypeBean;
import com.ltt.overseasuser.model.TypeListBean;
import com.ltt.overseasuser.model.TypeSectionBean;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/25.
 * Calegories upper left corner menu
 */
public class ExploreActivity extends BaseActivity {
    @BindView(R.id.parent_lv)
    RecyclerView parentLv;
    @BindView(R.id.child_lv)
    RecyclerView childLv;
    ActionBar bar;
    private TypeListAdapter parentAdapter;
    private SectionListAdapter childAdapter;

    private TypeBean mTypeBean;
    private int selected;
    @Override
    protected int bindLayoutID() {
        return R.layout.activity_explore;
    }

    @Override
    protected void prepareActivity() {
        bar=ActionBar.init(this);
        bar.setTitle("Calegories");
        bar.setLeft(R.mipmap.back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bar.showNotify();
        initData();
        selected  = this.getIntent().getExtras().getInt("selected");
        parentAdapter = new TypeListAdapter(this, R.drawable.parent_normal,R.drawable.press2);
        parentAdapter.setPosition(selected);
        parentAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object, View view, int position) {
                //Passing the current click location
                //Click the effect settings.
                parentAdapter.setPosition(position);
                parentAdapter.notifyDataSetChanged();
                if (!mTypeBean.getType_id().equals(((TypeBean) object).getType_id())){
                    mTypeBean = (TypeBean) object;
                    childAdapter.clear();
                    getSectionList();
                }
            }
        });

        childAdapter = new SectionListAdapter(this, R.drawable.normal, R.drawable.press);
        childAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object, View view, int position) {
                Intent intent = new Intent(getContext(), RequestActivity.class);
                intent.putExtra("sectionid",((SectionBean)object).getSection_id());
                startActivity(intent);
            }
        });
        LinearLayoutManager parentManager = new LinearLayoutManager(this );
        parentLv.setLayoutManager(parentManager);
        parentLv.setAdapter(parentAdapter);
        LinearLayoutManager childManager = new LinearLayoutManager(this );
        childLv.setLayoutManager(childManager);
        childLv.setAdapter(childAdapter);


    }

    public void initData(){
        getTypeList();
    }
    /**
     * Created by Administrator on 2018/1/25.
     * getTypeList
     * Menu selection on the left side
     */
    private void getTypeList(){
       showLoadingView();
        Call<TypeListBean> call = RetrofitUtil.getAPIService().getTypeList();
        call.enqueue(new CustomerCallBack<TypeListBean>() {
            @Override
            public void onResponseResult(TypeListBean response) {
                mTypeBean = response.getData().get(selected);
                parentAdapter.addAll(response.getData());
               getSectionList();
               dismissLoadingView();
            }

            @Override
            public void onResponseError(BaseBean errorMsg, boolean isNetError) {
                dismissLoadingView();
            }
        });
    }
    /**
     * Created by Administrator on 2018/1/25.
     * getSectionList
     * Menu selection on the right side
     */
    private void getSectionList(){
         showLoadingView();
        Call<SectionListBean> call = RetrofitUtil.getAPIService().getSectionList(mTypeBean.getType_id());
        call.enqueue(new CustomerCallBack<SectionListBean>() {
            @Override
            public void onResponseResult(SectionListBean response) {
                  dismissLoadingView();
                childAdapter.addAll(response.getData().get(0).getSection_list());
            }

            @Override
            public void onResponseError(BaseBean errorMsg, boolean isNetError) {
                dismissLoadingView();
            }
        });
    }
}
