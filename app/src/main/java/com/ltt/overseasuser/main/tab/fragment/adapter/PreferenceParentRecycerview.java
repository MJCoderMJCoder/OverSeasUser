package com.ltt.overseasuser.main.tab.fragment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ltt.overseasuser.R;

import com.ltt.overseasuser.model.PreferenceListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunwen on 2018/5/14.
 */

public class PreferenceParentRecycerview extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<PreferenceListBean.DataBean> mLists = new ArrayList<>();

    public PreferenceParentRecycerview(Context mContext, List<PreferenceListBean.DataBean> mLists) {
        this.mContext = mContext;
        this.mLists = mLists;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        Log.e("sss", "ss" + viewType);
        View view = mLayoutInflater.inflate(R.layout.recycleview_preference_parent, parent, false);
        return new ViewHolder_preference_parent(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ( (ViewHolder_preference_parent) holder).Tv_preferencre_parent.setText(mLists.get(position).getParent().getMain());
    }

    @Override
    public int getItemCount() {
      return mLists==null?0:mLists.size();
    }

    private class ViewHolder_preference_parent extends RecyclerView.ViewHolder {
        TextView Tv_preferencre_parent;
        public ViewHolder_preference_parent(View view) {
            super(view);
         Tv_preferencre_parent=view.findViewById(R.id.Tv_preference_parent);
        }
    }
}
