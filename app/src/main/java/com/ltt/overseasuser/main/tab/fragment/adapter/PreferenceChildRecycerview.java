package com.ltt.overseasuser.main.tab.fragment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.model.PreferenceListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunwen on 2018/5/14.
 */

public class PreferenceChildRecycerview extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<PreferenceListBean.DataBean> mLists = new ArrayList<>();
     int parentpositon=0;
    public PreferenceChildRecycerview(Context mContext, List<PreferenceListBean.DataBean> mLists,int parentpositon) {
        this.mContext = mContext;
        this.mLists = mLists;
        this.parentpositon=parentpositon;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        Log.e("sss", "ss" + viewType);
        View view = mLayoutInflater.inflate(R.layout.recycleview_preference_child, parent, false);
        return new ViewHolder_preference_Child(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ( (ViewHolder_preference_Child) holder).Tv_preferencre_child.setText(mLists.get(parentpositon).getChild().get(position).getName());
    }

    @Override
    public int getItemCount() {
      return mLists.get(parentpositon).getChild()==null?0:mLists.get(parentpositon).getChild().size();
    }

    private class ViewHolder_preference_Child extends RecyclerView.ViewHolder {
        TextView Tv_preferencre_child;
        CheckBox Cb_preference_child;
        public ViewHolder_preference_Child(View view) {
            super(view);
            Tv_preferencre_child=view.findViewById(R.id.Tv_preference_child);
            Cb_preference_child=view.findViewById(R.id.cb_preference_child);
        }
    }
}
