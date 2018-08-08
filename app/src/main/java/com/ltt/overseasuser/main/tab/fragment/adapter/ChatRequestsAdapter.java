package com.ltt.overseasuser.main.tab.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ltt.overseasuser.R;
import com.ltt.overseasuser.main.tab.fragment.activity.ChatsActivity;
import com.ltt.overseasuser.model.ViewRequestBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunwen on 2018/5/24.
 */

public class ChatRequestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<ViewRequestBean> mLists = new ArrayList<>();

    public ChatRequestsAdapter(ChatsActivity chatsActivity, List<ViewRequestBean> questions) {
        this.mContext = chatsActivity;
        this.mLists = questions;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        View view = mLayoutInflater.inflate(R.layout.recycleview_chat_request, parent, false);
        return new ViewHolder_Chat_Request(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder_Chat_Request) {
//            ((ViewHolder_Chat_Request) holder).tv_typename.setText(mLists.get(position).getQuestion_title());
//            ((ViewHolder_Chat_Request) holder).tv_typedetail.setText(mLists.get(position).getQuestion_answer());
        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }


    public int getTotalCount() {
        return mLists.size();
    }

    /**
     * -----------------------------------------------接收文本的信息-----------------------------------------------------
     */
    private class ViewHolder_Chat_Request extends RecyclerView.ViewHolder {

        TextView tv_typename;
        TextView tv_typedetail;

        private ViewHolder_Chat_Request(View itemView) {
            super(itemView);
            tv_typename = itemView.findViewById(R.id.tv_typename);
            tv_typedetail = itemView.findViewById(R.id.tv_typedetail);
        }
    }




}