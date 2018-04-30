package com.lin.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin on 2017/3/13.
 */

public class RollViewPager extends ViewPager {

    private List<ImageView> ivDots;
    private List<String> imageUrl;
    private List<String> titles;
    private TextView tvTitle;
    private int currentItem;
    private int lastPosition;
    private boolean isScrolling;
    private RollPagerAdapter adapter;
    private OnPageChangeListener listener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            if (tvTitle != null)
                tvTitle.setText(titles.get(position));
            if (ivDots == null)
                return;
            position = position - 1;
            if (position == -1)
                position = imageUrl.size() - 3;
            else if (position == imageUrl.size() - 2)
                position = 0;
            ivDots.get(lastPosition).setSelected(false);
            ivDots.get(position).setSelected(true);
            lastPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE)
                if (currentItem == 0)
                    setCurrentItem(imageUrl.size() - 2, false);
                else if (currentItem >= imageUrl.size() - 1)
                    setCurrentItem(1, false);
        }
    };
    private Handler handler = new ScrollHandler(this);

    public RollViewPager(Context context) {
        super(context);
        init();
    }

    public RollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        imageUrl = new ArrayList<String>();
        adapter = new RollPagerAdapter();
        adapter.setListener(listener2);
        this.addOnPageChangeListener(listener);
    }

    public void setWrapHeight(boolean flag) {
        adapter.setWrapHeight(flag);
    }

    public void setIvDots(LinearLayout llDots, int resId, LinearLayout.LayoutParams params) {
        ivDots = new ArrayList<>();
        llDots.removeAllViews();
        for (int i = 0; i < imageUrl.size() - 2; i++) {
            ImageView iv = new ImageView(getContext());
            iv.setImageResource(resId);
            llDots.addView(iv, params);
            ivDots.add(iv);
            if (i == 0)
                iv.setSelected(true);
        }
    }

    public void setTitles(TextView tv, List<String> titles) {
        tvTitle = tv;
        titles.add(titles.get(0));
        titles.add(0, titles.get(titles.size() - 2));
        this.titles = titles;
    }

    public void setImageUrl(List<String> imageUrl) {
        imageUrl.add(imageUrl.get(0));
        imageUrl.add(0, imageUrl.get(imageUrl.size() - 2));
        this.imageUrl = imageUrl;
        adapter.setImageUrl(imageUrl);
        if (getAdapter() == null)
            super.setAdapter(adapter);
        else
            adapter.notifyDataSetChanged();
        reset();
    }

    public void startScroll() {
        isScrolling = true;
        handler.removeMessages(0);
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    public void notifyPartRefresh() {
        adapter.notifyPartRefresh();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.removeMessages(0);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isScrolling)
                    startScroll();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (adapter.getCount() == imageUrl.size() - 2)
            this.adapter.setAdapter(adapter);
    }

    public void setOnPartRefreshListener(OnPartRefreshListener listener) {
        adapter.setOnPartRefreshListener(listener);
    }

    public void reset() {
        currentItem = 1;
        setCurrentItem(1, false);
        if (ivDots != null && ivDots.size() > 0)
            ivDots.get(0).setSelected(true);
        if (tvTitle != null)
            tvTitle.setText(titles.get(1));
    }

    private static class RollPagerAdapter extends PagerAdapter {

        private List<View> recycler = new ArrayList<>();
        private List<View> showingViews = new ArrayList<>();
        private List<String> imageUrl;
        private OnClickListener listener;
        private boolean isWrapHeight;
        private OnPartRefreshListener refreshListener;
        private PagerAdapter adapter;

        public void setImageUrl(List<String> imageUrl) {
            this.imageUrl = imageUrl;
        }

        public void setListener(OnClickListener listener) {
            this.listener = listener;
        }

        public void setWrapHeight(boolean flag) {
            isWrapHeight = flag;
        }

        public void setOnPartRefreshListener(OnPartRefreshListener listener) {
            refreshListener = listener;
        }

        public void setAdapter(PagerAdapter adapter) {
            this.adapter = adapter;
        }

        public void notifyPartRefresh() {
            if (refreshListener == null)
                return;
            for (View view : showingViews) {
                int pos = transform(view.getId());
                if (((ViewGroup) view).getChildCount() > 1)
                    view = ((ViewGroup) view).getChildAt(1);
                refreshListener.onItemPartRefresh(view, pos);
            }
        }

        @Override
        public int getCount() {
            return imageUrl.size() == 0 ? 0 : imageUrl.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % imageUrl.size();
            FrameLayout view;
            ImageView iv;
            if (recycler.size() > 0) {
                view = (FrameLayout) recycler.remove(0);
                iv = (ImageView) view.getChildAt(0);
            } else {
                view = new FrameLayout(container.getContext());
                iv = new ImageView(container.getContext());
                view.addView(iv, -1, -1);
                if (adapter != null) {
                    int pos = transform(position);
                    adapter.instantiateItem(view, pos);
                }
                view.setOnClickListener(listener);
            }
            view.setId(position);
            container.addView(view);
            showingViews.add(view);
            Glide.with(container.getContext()).load(imageUrl.get(position))
                    .dontAnimate()
                    .centerCrop()
                    .into(isWrapHeight ? new WrapTarget(iv) : new GlideDrawableImageViewTarget(iv));
            isWrapHeight = false;
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            showingViews.remove(object);
            recycler.add((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public int transform(int position) {
            int pos = position == 0 ? imageUrl.size() - 3 : position - 1;
            pos = position == imageUrl.size() - 1 ? pos : 0;
            return pos;
        }
    }

    private static class WrapTarget extends GlideDrawableImageViewTarget {

        private ImageView view;

        public WrapTarget(ImageView view) {
            super(view);
            this.view = view;
        }

        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
            float scale = (float) resource.getIntrinsicHeight() / resource.getIntrinsicWidth();
            int height = (int) (view.getResources().getDisplayMetrics().widthPixels * scale);
            getViewPager().getLayoutParams().height = height;
            super.onResourceReady(resource, animation);
        }

        public ViewGroup getViewPager() {
            ViewGroup viewParent = (ViewGroup) view.getParent();
            if (viewParent instanceof ViewPager)
                return viewParent;
            ViewGroup view = null;
            while (viewParent.getParent() instanceof ViewGroup) {
                viewParent = (ViewGroup) viewParent.getParent();
                if (viewParent instanceof ViewGroup) {
                    view = viewParent;
                    break;
                }
            }
            return view;
        }
    }

    private OnClickListener listener2 = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener3 != null) {
                int pos = v.getId();
                listener3.onItemClick(v, pos);
            }
        }
    };

    private OnItemClickListener listener3;

    public void setOnItemClickListener(final OnItemClickListener listener) {
        listener3 = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public interface OnPartRefreshListener {
        void onItemPartRefresh(View v, int pos);
    }

    private static class ScrollHandler extends Handler {
        private WeakReference<RollViewPager> reference;

        public ScrollHandler(RollViewPager pager) {
            reference = new WeakReference<RollViewPager>(pager);
        }

        @Override
        public void handleMessage(Message msg) {
            RollViewPager pager = reference.get();
            if (pager != null) {
                if (pager.imageUrl != null && pager.imageUrl.size() != 0) {
                    pager.currentItem = pager.currentItem + 1;
                    pager.setCurrentItem(pager.currentItem);
                }
                sendEmptyMessageDelayed(0, 3000);
            }
        }
    }
}
