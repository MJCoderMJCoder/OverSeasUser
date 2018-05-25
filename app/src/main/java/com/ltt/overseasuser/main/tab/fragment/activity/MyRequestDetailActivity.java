package com.ltt.overseasuser.main.tab.fragment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.http.RetrofitUtil;
import com.ltt.overseasuser.utils.L;
import com.ltt.overseasuser.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/18.
 */
public class MyRequestDetailActivity extends BaseActivity implements View.OnClickListener {
    ActionBar bar;
    @BindView(R.id.response_nameTV)
    TextView response_nameTV;
    @BindView(R.id.date_createdTV)
    TextView date_createdTV;
    @BindView(R.id.userTV)
    TextView userTV;
    @BindView(R.id.listView)
    ListView listView;

    private final String TAG = "(╯‵□′)╯︵┻━┻ 走你！";
    private String conversation_id;
    private String user;

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

        //        tvTomessage.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            String request_id = intent.getStringExtra("request_id");
            conversation_id = intent.getStringExtra("conversation_id");
            user = intent.getStringExtra("user");
            String response_name = intent.getStringExtra("response_name");
            String date_created = intent.getStringExtra("date_created");
            userTV.setText(user);
            response_nameTV.setText(response_name);
            date_createdTV.setText(date_created);
            Call<MyRequestDetailListBean> myRequestDetailListBeanCall = RetrofitUtil.getAPIService().getRequestDetail(request_id);
            myRequestDetailListBeanCall.enqueue(new CustomerCallBack<MyRequestDetailListBean>() {
                @Override
                public void onResponseResult(MyRequestDetailListBean response) {
                    L.v(TAG, response + "");
                    if (response.isStatus()) {
                        listView.setAdapter(new ReusableAdapter<ExploreQuestionListBean>(response.getData().getQuestions(), R.layout.item_my_request_detail_layout) {
                            @Override
                            public void bindView(ViewHolder holder, ExploreQuestionListBean obj) {
                                holder.setText(R.id.question_title, obj.getQuestion_title());
                                holder.setText(R.id.question_answer, obj.getQuestion_answer());
                            }
                        });
                    } else {
                        ToastUtils.showToast(response.getMsg());
                    }
                }

                @Override
                public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                    if (errorMessage != null) {
                        ToastUtils.showToast(errorMessage.getMsg());
                    } else {
                        ToastUtils.showToast("isNetError：" + isNetError);
                    }
                }
            });
        }
    }

    @OnClick({R.id.tv_profile}) //R.id.tv_tomessage,
    public void onClick(View v) {
        switch (v.getId()) {
            //            case R.id.tv_tomessage:
            //                Intent intent = new Intent(MyRequestDetailActivity.this, ChatsActivity.class);
            //                //                intent.putExtra("username", dataBean.getUser());
            //                //                intent.putExtra("request_category", dataBean.getRequest_category());
            //                //                intent.putExtra("conversation_id", dataBean.getConversation_id());
            //                startActivity(intent);
            //                break;
            case R.id.tv_profile:
                Intent intent = new Intent(MyRequestDetailActivity.this, ChatsActivity.class);
                intent.putExtra("username", user);
                //                intent.putExtra("request_category", dataBean.getRequest_category());
                intent.putExtra("conversation_id", conversation_id);
                startActivity(intent);
                break;
        }

    }
}
