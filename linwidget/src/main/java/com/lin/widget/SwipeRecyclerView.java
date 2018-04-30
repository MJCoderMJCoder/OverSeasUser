package com.lin.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lin on 2017/7/17.
 */

public class SwipeRecyclerView extends RecyclerView {

    private Adapter mAdapter;
    private OnLoadMoreListener mOnLoadMoreListener;
    private View mFooterView;
    private boolean isLoadingMoreData = false;
    private boolean isEnable = true;

    public SwipeRecyclerView(Context context) {
        super(context);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mFooterView != null)
            adapter = new SwipeAdapter(mFooterView, adapter);
        super.setAdapter(adapter);
        mAdapter = adapter;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && mOnLoadMoreListener != null && !isLoadingMoreData) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            if (getParent() instanceof SwipeRefreshLayout) {
                SwipeRefreshLayout parent = (SwipeRefreshLayout) getParent();
                if (parent.isRefreshing())
                    return;
            }

            if (isEnable && layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition != 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1) {
                if (mFooterView != null) {
                    mFooterView.setVisibility(VISIBLE);
                    isLoadingMoreData = true;
                    mOnLoadMoreListener.onLoadMore();
                }
            }
        }
    }

    public void setEnableLoadMore(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public void addFootView(View view) {
        final LayoutManager manager = getLayoutManager();
        if (view.getLayoutParams() == null) {
            if (manager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(view.getLayoutParams());
                params.setFullSpan(true);
                view.setLayoutParams(params);
            }
        }
        mFooterView = view;
        mFooterView.setVisibility(GONE);
        if (mAdapter != null) {
            if (!(mAdapter instanceof SwipeAdapter)) {
                mAdapter = new SwipeAdapter(mFooterView, mAdapter);
                super.setAdapter(mAdapter);
            }
        }
    }

    private void loadMore() {
        isLoadingMoreData = true;
        mOnLoadMoreListener.onLoadMore();
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public void loadMoreComplete() {
        isLoadingMoreData = false;
        if (mFooterView != null) {
            mFooterView.setVisibility(GONE);
        }
        getAdapter().notifyDataSetChanged();
    }

    /**
     * set load more listener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {

        void onLoadMore();
    }

}
