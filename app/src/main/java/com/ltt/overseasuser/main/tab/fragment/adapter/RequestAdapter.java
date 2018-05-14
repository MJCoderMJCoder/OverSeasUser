package com.ltt.overseasuser.main.tab.fragment.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class RequestAdapter extends PagerAdapter {
    private List<Object> mViewList = new ArrayList<>();
    public RequestAdapter() {  }
    @Override
    public int getCount() { return mViewList.size(); }
    @Override
    public boolean isViewFromObject(View view, Object object) { return view==object; }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView((View)(mViewList.get(position)));
         return (View)(mViewList.get(position));
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) { container.removeView((View)( mViewList.get(position))); }
    public void addAll(Collection<? extends Object> collection) {
        if (collection == null) {
            return;
        }
        mViewList.addAll(collection);
        notifyDataSetChanged();
    }
    public void clear() {
        mViewList.clear();
        notifyDataSetChanged();
    }
}
