package com.ltt.overseasuser.main.tab.fragment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.http.CustomerCallBack;
import com.ltt.overseasuser.http.RetrofitUtil;
import com.ltt.overseasuser.main.tab.fragment.fragment.PortfolloFragment;
import com.ltt.overseasuser.main.tab.fragment.fragment.ReViewFragment;
import com.ltt.overseasuser.model.GsonUserBean;
import com.ltt.overseasuser.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/18.
 */
public class ProfileActivity extends BaseActivity {
    @BindView(R.id.action_bar)
    View actionBar;
    ActionBar bar;
    @BindView(R.id.view_portfollo)
    TextView viewPortfollo;
    @BindView(R.id.view_review)
    TextView viewReview;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.tv_change_email)
    TextView tvChangeEmail;
    @BindView(R.id.iv_change_email)
    ImageView ivChangeEmail;
    @BindView(R.id.tv_change_phone)
    TextView tvChangePhone;
    @BindView(R.id.iv_change_phone)
    ImageView ivChangePhone;
    @BindView(R.id.tv_change_address)
    TextView tvChangeAddress;
    @BindView(R.id.iv_change_address)
    ImageView ivChangeAddress;
    @BindView(R.id.iv_choose_preference)
    ImageView ivChoosePreference;

    @Override
    protected int bindLayoutID() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void prepareActivity() {
        bar = ActionBar.init(actionBar);
        bar.setTitle("My Profile");
        bar.showNotify();
        bar.setLeft(R.mipmap.back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewReview.setSelected(true);
        vp.setAdapter(new GamePager(getSupportFragmentManager()));
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("position", position + "");
                changePos(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        getProfile();
    }

    /**
     * 获取profile信息
     */
    private void getProfile() {
        Call<GsonUserBean> call = RetrofitUtil.getAPIService().getProfile();
        call.enqueue(new CustomerCallBack<GsonUserBean>() {
            @Override
            public void onResponseResult(GsonUserBean response) {
                dismissLoadingView();
                String address = response.getData().getAddress();
                String email = response.getData().getEmail();
                String phone = response.getData().getPhone();
            }

            @Override
            public void onResponseError(BaseBean errorMsg, boolean isNetError) {
                dismissLoadingView();
            }
        });
    }

    private void changePos(int position) {
        switch (position) {
            case 0:
                viewPortfollo.setSelected(true);
                viewReview.setSelected(false);
                break;
            case 1:
                viewPortfollo.setSelected(false);
                viewReview.setSelected(true);
                break;
        }
    }

    @OnClick({R.id.iv_notify, R.id.btn_recharge, R.id.iv_person_info, R.id.iv_content, R.id.view_portfollo, R.id.view_review, R.id.iv_change_email, R.id.iv_change_phone, R.id.iv_change_address,R.id.iv_choose_preference})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_notify:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            case R.id.btn_recharge:
                startActivity(new Intent(this, PaymentActivity.class));
                break;
            case R.id.iv_person_info:
                startActivity(new Intent(this, PersonInfomationActivity.class));
                break;
            case R.id.iv_content:
                break;
            case R.id.view_portfollo:
                changePos(0);
                vp.setCurrentItem(1);
                break;
            case R.id.view_review:
                changePos(1);
                vp.setCurrentItem(0);
                break;
            case R.id.iv_change_email:
                ToastUtils.showToast("change email");
                break;
            case R.id.iv_change_phone:
                ToastUtils.showToast("change phone");
                break;
            case R.id.iv_change_address:
                ToastUtils.showToast("change address");
                break;
            case R.id.iv_choose_preference:
                startActivity(new Intent(this,ChoosePreferenceActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick()
    public void onClick() {
    }



    private static class GamePager extends FragmentPagerAdapter {
        public GamePager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new ReViewFragment();
                    break;
                case 1:
                    fragment = new PortfolloFragment();
                    break;

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
