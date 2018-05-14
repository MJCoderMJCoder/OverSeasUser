package com.ltt.overseasuser.main.tab.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.RecyclerAdapter;
import com.ltt.overseasuser.model.TypeBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */
public class TypeListAdapter extends RecyclerAdapter {

    private int normalBg;
    private int pressBg;
    private int selection;
    private List<Object> list = new ArrayList<>();

    public TypeListAdapter(Context context, int normalBg, int pressBg) {
        initParams(normalBg, pressBg);
    }

    private void initParams(int normalBg, int pressBg){
        this.normalBg = normalBg;
        this.pressBg = pressBg;
        this.selection = -1;
    }
    public void setPosition(int position) {
        selection = position;
    }
    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_parentitem, parent, false));
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
        final TypeBean data = (TypeBean)(list.get(position));
        ((Holder) holder).tvName.setText(data.getType_name());
        if(position == selection) {
            ((Holder) holder).tvName.setBackgroundResource(pressBg);
        } else {
            ((Holder) holder).tvName.setBackgroundResource(normalBg);
        }
    }

    public class Holder extends RecyclerHolder {
        public Holder(View itemView) {
            super(itemView);
        }
        TextView tvName = (TextView) itemView.findViewById(R.id.parent_tv);
    }

}
