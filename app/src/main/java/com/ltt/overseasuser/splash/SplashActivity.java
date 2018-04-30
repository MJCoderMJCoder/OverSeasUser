package com.ltt.overseasuser.splash;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.splash.fragment.Introduce1Fragment;
import com.ltt.overseasuser.splash.fragment.Introduce2Fragment;
import com.ltt.overseasuser.splash.fragment.Introduce3Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/12.
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.vp)
    ViewPager vp;

    private List<Fragment> fragmentList;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void prepareActivity() {
        initFragments();
    }

    private void initFragments() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new Introduce1Fragment());
        fragmentList.add(new Introduce2Fragment());
        fragmentList.add(new Introduce3Fragment());

        vp.setAdapter(new SplashPager(getSupportFragmentManager()));
        vp.setOffscreenPageLimit(3);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class SplashPager extends FragmentPagerAdapter {

        public SplashPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }

    @Override
    public void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        super.setFullScreen();
    }
}
