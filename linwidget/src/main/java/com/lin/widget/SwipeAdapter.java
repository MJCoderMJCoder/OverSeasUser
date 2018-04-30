package com.lin.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lin on 2017/7/17.
 */

public class SwipeAdapter extends RecyclerView.Adapter {

    private RecyclerView.Adapter mAdapter;
    private View mFooterView;

    public SwipeAdapter(View mFooterView, RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
        this.mFooterView = mFooterView;
    }

    @Override
    public int getItemViewType(int position) {
        int adapterCount;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (position < adapterCount) {
                return mAdapter.getItemViewType(position);
            }
        }
        // footer view
        return RecyclerView.INVALID_TYPE;
    }

    @Override
    public long getItemId(int position) {
        if (mAdapter != null) {
            int adapterCount = mAdapter.getItemCount();
            if (position < adapterCount) {
                return mAdapter.getItemId(position);
            }
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RecyclerView.INVALID_TYPE) {
            return new FooterViewHolder(mFooterView);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int adapterCount;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (position < adapterCount) {
                mAdapter.onBindViewHolder(holder, position);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return getFootersCount() + mAdapter.getItemCount();
        } else {
            return getFootersCount();
        }
    }

    public boolean isFooter(int position) {
        return position < getItemCount() && position >= getItemCount() - getFootersCount();
    }

    public int getFootersCount() {
        return mFooterView == null ? 0 : 1;
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
