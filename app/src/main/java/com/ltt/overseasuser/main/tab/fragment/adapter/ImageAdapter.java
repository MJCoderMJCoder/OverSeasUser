package com.ltt.overseasuser.main.tab.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.RecyclerAdapter;
import com.ltt.overseasuser.model.SectionImageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class ImageAdapter extends RecyclerAdapter {
     private  List<SectionImageInfo> mList = new ArrayList<SectionImageInfo>();
     private int selected=-1;
    public ImageAdapter(Context context){

    }
     public void add(String sSectionid,int iResouseId,int iResouseIdPress){
        SectionImageInfo secionImageInfo = new SectionImageInfo(sSectionid,iResouseId,iResouseIdPress);
        mList.add(secionImageInfo);
        notifyDataSetChanged();
    }
    public void setSelected(int selected){
        this.selected=selected;
    }
    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new ImageAdapter.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public int getContentItemCount() {
        return mList.size();
    }

    @Override
    public SectionImageInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SectionImageInfo sectionImageObject= mList.get(position);
        if (selected==position)
            ((Holder) holder).lvName.setImageResource(sectionImageObject.getiResouseIdPess());
        else
            ((Holder) holder).lvName.setImageResource(sectionImageObject.getiResouseId());
    }

    public class Holder extends RecyclerHolder {

        public Holder(View view) {super(view);}
        ImageView lvName = (ImageView) itemView.findViewById(R.id.iv_imageitem);
    }
}
