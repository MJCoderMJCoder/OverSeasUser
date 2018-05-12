package com.ltt.overseasuser.main.tab.fragment.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.RecyclerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */
public class MyRequestAdapter extends RecyclerAdapter {

    private List data;

    public MyRequestAdapter(List data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        if (data == null) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_myrequest, parent, false));
        } else {
            //            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_mytask, parent, false));
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myrequest, parent, false));
        }
    }

    @Override
    public int getContentItemCount() {
        return (data != null ? data.size() : 1);
    }

    @Override
    public Object getItem(int position) {
        return (data != null ? data.get(position) : null);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public class Holder extends RecyclerHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
