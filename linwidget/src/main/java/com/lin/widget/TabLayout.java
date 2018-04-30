package com.lin.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e on 2016/12/30.
 */

public class TabLayout extends LinearLayout implements View.OnClickListener {

    private List<TabItem> items = new ArrayList<>();
    private FragmentManager manager;
    private List<Fragment> fragments;
    private int currentTab;
    private int textSelectColor;
    private int textUnSelectColor;

    public TabLayout(Context context) {
        super(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TabLayout, defStyleAttr, 0);
        textSelectColor = a.getColor(R.styleable.TabLayout_textSelectColor, 0);
        textUnSelectColor = a.getColor(R.styleable.TabLayout_textUnSelectColor, 0);
    }

    public void addTab(String title, int selectedIcon, int unSelectedIcon, int position) {
        TabItem item = new TabItem(title, selectedIcon, unSelectedIcon);
        ViewGroup child = (ViewGroup) getChildAt(position);
        child.setTag(item);
        child.setOnClickListener(this);
        item.iv = (ImageView) child.getChildAt(0);
        item.tv = (TextView) child.getChildAt(1);
        item.iv.setImageResource(unSelectedIcon);
        item.tv.setTextColor(textUnSelectColor);
        item.tv.setText(title);
        items.add(item);
    }

    public void setTabData(FragmentManager manager, int containerViewId, List<Fragment> fragments) {
        this.manager = manager;
        this.fragments = fragments;
        FragmentTransaction transaction = manager.beginTransaction();
        for (Fragment fragment : fragments) {
            if (fragment != null)
                transaction.add(containerViewId, fragment).hide(fragment);
        }
        transaction.commit();
        setCurrentTab(0);
    }

    public void setCurrentTab(int currentTab) {
        if (fragments.get(currentTab) != null) {
            manager.beginTransaction().hide(fragments.get(this.currentTab)).show(fragments.get(currentTab)).commit();
            TabItem item = items.get(this.currentTab);
            item.iv.setImageResource(item.unSelectedIcon);
            item.tv.setTextColor(textUnSelectColor);
            this.currentTab = currentTab;
            item = items.get(this.currentTab);
            item.iv.setImageResource(item.selectedIcon);
            item.tv.setTextColor(textSelectColor);
        }
    }

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.listener = listener;
    }

    public static class TabItem {
        public String title;
        public int selectedIcon;
        public int unSelectedIcon;
        public ImageView iv;
        public TextView tv;

        public TabItem(String title, int selectedIcon, int unSelectedIcon) {
            this.title = title;
            this.selectedIcon = selectedIcon;
            this.unSelectedIcon = unSelectedIcon;
        }
    }

    @Override
    public void onClick(View v) {
        TabItem item = (TabItem) v.getTag();
        int position = items.indexOf(item);
        setCurrentTab(position);
        if (listener != null)
            listener.onTabSelect(position);
    }

    private OnTabSelectListener listener;

    public interface OnTabSelectListener {
        void onTabSelect(int position);
    }
}
