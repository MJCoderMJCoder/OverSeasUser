package com.ltt.overseasuser.main.tab.fragment;

import android.content.Intent;
import android.view.View;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseFragment;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.main.tab.fragment.activity.HelpActivity;
import com.ltt.overseasuser.main.tab.fragment.activity.NotificationActivity;
import com.ltt.overseasuser.main.tab.fragment.activity.ProfileActivity;
import com.ltt.overseasuser.main.tab.fragment.activity.SettingNotificationActivity;
import com.ltt.overseasuser.main.tab.fragment.dialog.ChooseRegionDialog;
import com.ltt.overseasuser.main.tab.fragment.dialog.FeedBackDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/18.
 */
public class MoreFragment extends BaseFragment {
    @BindView(R.id.action_bar)
    View actionBar;
    ActionBar bar;

    @Override
    protected int bindLayoutID() {
        return R.layout.fragment_more;
    }

    @Override
    protected void prepareFragment() {
        bar = ActionBar.init(actionBar);
        bar.setTitle("More");
        bar.setLeft(R.mipmap.back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        bar.showNotify();
    }

    @OnClick({R.id.iv_notify,R.id.tv_my_profile,R.id.tv_notification_set, R.id.tv_set_language, R.id.tv_help, R.id.tv_support, R.id.tv_feedback, R.id.tv_terms, R.id.tv_privacy, R.id.tv_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_notify:
                startActivity(new Intent(getActivity(), NotificationActivity.class));
                break;
            case R.id.tv_my_profile:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;
            case R.id.tv_notification_set:
                startActivity(new Intent(getActivity(), SettingNotificationActivity.class));
                break;
            case R.id.tv_set_language:
                ChooseRegionDialog dialog1=new ChooseRegionDialog(getActivity(),R.style.Prompt_dialog,0.9,0.7);
                dialog1.show();
                break;
            case R.id.tv_help:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;
            case R.id.tv_support:
                break;
            case R.id.tv_feedback:
                FeedBackDialog dialog=new FeedBackDialog(getActivity(),R.style.Prompt_dialog,0.9,0.7);
                dialog.show();
                break;
            case R.id.tv_terms:
                break;
            case R.id.tv_privacy:
                break;
            case R.id.tv_version:
                break;
        }
    }
}
