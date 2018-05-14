package com.ltt.overseasuser.main.tab.fragment.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.RecyclerAdapter;
import com.ltt.overseasuser.model.TypeBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class ExploreTypeAdapter extends RecyclerAdapter {
    private List<Object> list = new ArrayList<>();


    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new ExploreTypeAdapter.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore_type, parent, false));
    }

    @Override
    public int getContentItemCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        final string data = (TypeBean)(list.get(position));
//        ((Holder) holder).ivType.setImageIcon();


    }

    public void addAll(Collection<? extends Object> collection) {
        if (collection == null) {
            return;
        }
        list.addAll(collection);
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
        ImageView ivType = (ImageView) itemView.findViewById(R.id.iv_type);
    }
}
