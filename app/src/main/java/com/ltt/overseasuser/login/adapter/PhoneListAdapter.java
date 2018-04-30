package com.ltt.overseasuser.login.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.RecyclerAdapter;
import com.ltt.overseasuser.model.PhoneBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */
public class PhoneListAdapter extends RecyclerAdapter {

    private List<Object> list = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone_list, parent, false));
    }

    @Override
    public int getContentItemCount() {
        return list.size();
    }

    public void addAll(Collection<? extends Object> collection) {
        if (collection == null) {
            return;
        }
        list.addAll(collection);
        notifyDataSetChanged();
    }

    public List<Object> getData() {
        return list;
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PhoneBean data = (PhoneBean) getData().get(position);
        ((Holder) holder).tvAreaName.setText(data.getText());
    }

    public class Holder extends RecyclerHolder {
        public Holder(View itemView) {
            super(itemView);
        }
        TextView tvAreaName = (TextView) itemView.findViewById(R.id.tv_area_name);
    }
}
