package com.ltt.overseasuser.main.tab.fragment.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.main.tab.fragment.fragment.PortfolloFragment;
import com.ltt.overseasuser.main.tab.fragment.fragment.ReViewFragment;

import butterknife.BindView;
import butterknife.OnClick;

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

    @OnClick({R.id.iv_notify,R.id.btn_recharge,R.id.iv_person_info, R.id.iv_content, R.id.view_portfollo, R.id.view_review})
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
        }
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
