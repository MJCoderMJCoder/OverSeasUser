package com.ltt.overseasuser.main.tab.fragment.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.RecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/21.
 */
public class ExploreAdapter extends RecyclerAdapter {

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        if(viewType!=3)
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore, parent, false));
        else
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore_another, parent, false));

    }

    @Override
    public int getContentItemCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Holder holder1= (Holder) holder;
    }

    public class Holder extends RecyclerHolder {

        @BindView(R.id.rl_1)
        RelativeLayout rl1;
        @BindView(R.id.rl_2)
        LinearLayout rl2;
        @BindView(R.id.rl_3)
        LinearLayout rl3;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
