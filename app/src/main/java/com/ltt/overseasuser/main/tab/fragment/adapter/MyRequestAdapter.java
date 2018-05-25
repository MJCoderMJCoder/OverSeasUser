package com.ltt.overseasuser.main.tab.fragment.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.RecyclerAdapter;
import com.ltt.overseasuser.model.MyRequestBean;
import com.ltt.overseasuser.model.MyResponseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */
public class MyRequestAdapter extends RecyclerAdapter {

    private List data;
    private String request_name;

    public MyRequestAdapter(String request_name, List data) {
        this.data = data;
        this.request_name = request_name;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        if (data != null && data.get(0) instanceof MyRequestBean) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_myrequest, parent, false));
        } else if (data != null && data.get(0) instanceof MyResponseBean) {
            //            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_mytask, parent, false));
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myrequest, parent, false));
        } else {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_myrequest, parent, false));
        }
    }

    @Override
    public int getContentItemCount() {
        return (data != null ? data.size() : 0);
    }

    @Override
    public Object getItem(int position) {
        return (data != null ? data.get(position) : null);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (data != null && data.get(0) instanceof MyRequestBean) {
            ((Holder) holder).tv_hint.setText(((MyRequestBean) data.get(position)).getRequest_name());
            ((Holder) holder).slash.setText(((MyRequestBean) data.get(position)).getTotal_response() + " Response received");
        } else if (data != null && data.get(0) instanceof MyResponseBean) {
            ((Holder) holder).tv_hint.setText(request_name);
            ((Holder) holder).slash.setText(((MyResponseBean) data.get(position)).getService_provider());
        }
    }

    public class Holder extends RecyclerHolder {
        private TextView tv_hint;
        private TextView slash;

        public Holder(View itemView) {
            super(itemView);
            if (data != null && data.get(0) instanceof MyRequestBean) {
                tv_hint = itemView.findViewById(R.id.tv_hint);
                slash = itemView.findViewById(R.id.slash);
            } else if (data != null && data.get(0) instanceof MyResponseBean) {
                tv_hint = itemView.findViewById(R.id.tv_hint);
                slash = itemView.findViewById(R.id.slash);
            }
        }
    }
}
