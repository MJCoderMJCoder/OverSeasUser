package com.ltt.overseasuser;

import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.lin.widget.TabLayout;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.main.tab.fragment.ExploreFragment;
import com.ltt.overseasuser.main.tab.fragment.InboxFragment;
import com.ltt.overseasuser.main.tab.fragment.MoreFragment;
import com.ltt.overseasuser.main.tab.fragment.ProfileFragment;
import com.ltt.overseasuser.main.tab.fragment.TaskFragment;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectListener {

    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.tab_menu)
    TabLayout tabLayout;


    private String[] mTitles = {"Inbox", "Explore","Task",  "More"};
    private long exitTime = 0;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private int[] mIconUnselectedIds = {
            R.mipmap.icon_inbox, R.mipmap.icon_explore, R.mipmap.ic_task, R.mipmap.icon_more};
    private int[] mIconSelectedIds = {
            R.mipmap.icon_inbox, R.mipmap.icon_explore, R.mipmap.ic_task,  R.mipmap.icon_more};

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void prepareActivity() {
        initTabMenu();
    }

    private void initTabMenu(){
        mFragments.add(new InboxFragment());
        mFragments.add(new ExploreFragment());
        mFragments.add(new TaskFragment());
//        mFragments.add(new ProfileFragment());
        mFragments.add(new MoreFragment());
        for (int i = 0; i < mTitles.length; i++)
            tabLayout.addTab(mTitles[i], mIconSelectedIds[i], mIconUnselectedIds[i], i);
        tabLayout.setTabData(getSupportFragmentManager(), R.id.container, mFragments);
        tabLayout.setOnTabSelectListener(this);
    }

    @Override
    public void onTabSelect(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
}
