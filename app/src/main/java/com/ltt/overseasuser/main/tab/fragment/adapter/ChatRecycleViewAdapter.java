package com.ltt.overseasuser.main.tab.fragment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.Constants;
import com.ltt.overseasuser.main.tab.fragment.activity.ChatsActivity;
import com.ltt.overseasuser.model.ChatMessageBean;
import com.ltt.overseasuser.utils.DateUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunwen on 2018/5/9.
 */

public class ChatRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    Context mContext;
    List<ChatMessageBean.MessageBean> mLists = new ArrayList<>();
    ChatMessageBean.MembersBean mMembersBean = new ChatMessageBean.MembersBean();

    public ChatRecycleViewAdapter(ChatsActivity chatsActivity, List<ChatMessageBean.MessageBean> dataLists, ChatMessageBean.MembersBean membersBean) {
        this.mContext = chatsActivity;
        this.mLists = dataLists;
        this.mMembersBean = membersBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
        Log.e("sss", "ss" + viewType);
        if (Constants.FROMLEFTTXT == viewType) {
            View view = mLayoutInflater.inflate(R.layout.recycleview_chat_lefttxt, parent, false);
            return new ViewHolder_Chat_LeftTxt(view);
        } else if (Constants.FROMRIGHTTXT == viewType) {
            View view = mLayoutInflater.inflate(R.layout.recycleview_chat_righttxt, parent, false);
            return new ViewHolder_Chat_RightTxt(view);
        } else if (Constants.FROMLEFTPIC == viewType) {
            View view = mLayoutInflater.inflate(R.layout.recycleview_chat_leftpic, parent, false);
            return new ViewHolder_Chat_LeftPic(view);
        }else if (Constants.FROMRIGHTPIC == viewType) {
            View view = mLayoutInflater.inflate(R.layout.recycleview_chat_rightpic, parent, false);
            return new ViewHolder_Chat_RightPic(view);
        } else if (Constants.FROMLEFTFILE == viewType) {
            View view = mLayoutInflater.inflate(R.layout.recycleview_chat_leftfile, parent, false);
            return new ViewHolder_Chat_LeftFile(view);
        } else if (Constants.FROMRIGHTFILE == viewType) {
            View view = mLayoutInflater.inflate(R.layout.recycleview_chat_rightfile, parent, false);
            return new ViewHolder_Chat_RightFile(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder_Chat_LeftTxt) {
            if (position > 0) {
                long time = mLists.get(position).getCreatedAt() - mLists.get(position - 1).getCreatedAt();
                long l = time / 1000;
                if (l < 30) {
                    ((ViewHolder_Chat_LeftTxt) holder).mTv_time.setVisibility(View.GONE);
                } else {
                    ((ViewHolder_Chat_LeftTxt) holder).mTv_time.setVisibility(View.VISIBLE);
                    ((ViewHolder_Chat_LeftTxt) holder).mTv_time.setText(DateUtils.timeslash(mLists.get(position).getCreatedAt() +""));
                }
            }

            ((ViewHolder_Chat_LeftTxt) holder).mTv_message.setText(mLists.get(position).getMessage());
        } else if (holder instanceof ViewHolder_Chat_RightTxt) {
            if (position > 0) {
                long time = mLists.get(position).getCreatedAt() - mLists.get(position - 1).getCreatedAt();
                long l = time / 1000;
                if (l < 30) {
                    ((ViewHolder_Chat_RightTxt) holder).mTv_time.setVisibility(View.GONE);
                } else {
                    ((ViewHolder_Chat_RightTxt) holder).mTv_time.setVisibility(View.VISIBLE);
                    ((ViewHolder_Chat_RightTxt) holder).mTv_time.setText(DateUtils.timeslash(mLists.get(position).getCreatedAt() +""));
                }
            }
            ((ViewHolder_Chat_RightTxt) holder).mTv_message.setText(mLists.get(position).getMessage());
        } else if (holder instanceof ViewHolder_Chat_LeftPic) {
            if (position > 0) {
                long time = mLists.get(position).getCreatedAt() - mLists.get(position - 1).getCreatedAt();
                long l = time / 1000;
                if (l < 30) {
                    ((ViewHolder_Chat_LeftPic) holder).mTv_time.setVisibility(View.GONE);
                } else {
                    ((ViewHolder_Chat_LeftPic) holder).mTv_time.setVisibility(View.VISIBLE);
                    ((ViewHolder_Chat_LeftPic) holder).mTv_time.setText(DateUtils.timeslash(mLists.get(position).getCreatedAt() +""));
                }
            }

            String message = mLists.get(position).getMessage();
            if (message.contains("https")) {
                Glide.with(mContext).load(message).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(((ViewHolder_Chat_LeftPic) holder).mIv_message);
            } else {
                Bitmap loacalBitmap = getLoacalBitmap(message);
                ((ViewHolder_Chat_LeftPic) holder).mIv_message.setImageBitmap(loacalBitmap);
            }

        } else if (holder instanceof ViewHolder_Chat_RightPic) {
            if (position > 0) {
                long time = mLists.get(position).getCreatedAt() - mLists.get(position - 1).getCreatedAt();
                long l = time / 1000;
                if (l < 30) {
                    ((ViewHolder_Chat_RightPic) holder).mTv_time.setVisibility(View.GONE);
                } else {
                    ((ViewHolder_Chat_RightPic) holder).mTv_time.setVisibility(View.VISIBLE);
                    ((ViewHolder_Chat_RightPic) holder).mTv_time.setText(DateUtils.timeslash(mLists.get(position).getCreatedAt() +""));
                }
            }
            String message = mLists.get(position).getMessage();
            if (message.contains("https")) {
                Glide.with(mContext).load(message).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(((ViewHolder_Chat_RightPic) holder).mIv_message);
            } else {
                Bitmap loacalBitmap = getLoacalBitmap(message);
                ((ViewHolder_Chat_RightPic) holder).mIv_message.setImageBitmap(loacalBitmap);
            }
        }else if (holder instanceof ViewHolder_Chat_LeftFile) {
            if (position > 0) {
                long time = mLists.get(position).getCreatedAt() - mLists.get(position - 1).getCreatedAt();
                long l = time / 1000;
                if (l < 30) {
                    ((ViewHolder_Chat_LeftFile) holder).mTv_time.setVisibility(View.GONE);
                } else {
                    ((ViewHolder_Chat_LeftFile) holder).mTv_time.setVisibility(View.VISIBLE);
                    ((ViewHolder_Chat_LeftFile) holder).mTv_time.setText(DateUtils.timeslash(mLists.get(position).getCreatedAt() +""));
                }
            }
            ((ViewHolder_Chat_LeftFile) holder).mTv_message.setText(mLists.get(position).getMessage());
        } else if (holder instanceof ViewHolder_Chat_RightFile) {
            if (position > 0) {
                long time = mLists.get(position).getCreatedAt() - mLists.get(position - 1).getCreatedAt();
                long l = time / 1000;
                if (l < 30) {
                    ((ViewHolder_Chat_RightFile) holder).mTv_time.setVisibility(View.GONE);
                } else {
                    ((ViewHolder_Chat_RightFile) holder).mTv_time.setVisibility(View.VISIBLE);
                    ((ViewHolder_Chat_RightFile) holder).mTv_time.setText(DateUtils.timeslash(mLists.get(position).getCreatedAt() +""));
                }
            }
            ((ViewHolder_Chat_RightFile) holder).mTv_message.setText(mLists.get(position).getMessage());
        }
    }

    /**
     * 加载本地图片
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        String requester = mMembersBean.getRequester();
        String service_provider = mMembersBean.getService_provider();
        String senderId = mLists.get(position).getSenderId();
        if (senderId.equals(service_provider)) {
            String type = mLists.get(position).getType();
            if (type.equals(Constants.TYPETXT)) {
                return Constants.FROMLEFTTXT;
            } else if (type.equals(Constants.TYPEPIC)) {
                return Constants.FROMLEFTPIC;
            } else if (type.equals(Constants.TYPEFILE)) {
                return Constants.FROMLEFTFILE;
            }
        } else if (senderId.equals(requester)){
            String type = mLists.get(position).getType();
            if (type.equals(Constants.TYPETXT)) {
                return Constants.FROMRIGHTTXT;
            } else if (type.equals(Constants.TYPEPIC)) {
                return Constants.FROMRIGHTPIC;
            } else if (type.equals(Constants.TYPEFILE)) {
                return Constants.FROMRIGHTFILE;
            }
        }

        return super.getItemViewType(position);
    }

    public int getTotalCount() {
        return mLists.size();
    }

    /**-----------------------------------------------接收文本的信息-----------------------------------------------------*/
    private class ViewHolder_Chat_LeftTxt extends RecyclerView.ViewHolder {

        TextView mTv_time;
        TextView mTv_message;

        private ViewHolder_Chat_LeftTxt(View itemView) {
            super(itemView);
            mTv_time = itemView.findViewById(R.id.tv_time);
            mTv_message = itemView.findViewById(R.id.tv_message);
        }
    }

    /**-----------------------------------------------接收图片的信息-----------------------------------------------------*/
    private class ViewHolder_Chat_LeftPic extends RecyclerView.ViewHolder {

        TextView mTv_time;
        ImageView mIv_message;

        private ViewHolder_Chat_LeftPic(View itemView) {
            super(itemView);
            mTv_time = itemView.findViewById(R.id.tv_time);
            mIv_message = itemView.findViewById(R.id.iv_message);
        }
    }

    /**-----------------------------------------------接收文件的信息-----------------------------------------------------*/
    private class ViewHolder_Chat_LeftFile extends RecyclerView.ViewHolder {

        TextView mTv_time;
        TextView mTv_message;

        private ViewHolder_Chat_LeftFile(View itemView) {
            super(itemView);
            mTv_time = itemView.findViewById(R.id.tv_time);
            mTv_message = itemView.findViewById(R.id.tv_message);
        }
    }

    /**-----------------------------------------------发送文本的信息-----------------------------------------------------*/
    private class ViewHolder_Chat_RightTxt extends RecyclerView.ViewHolder {

        TextView mTv_time;
        TextView mTv_message;

        private ViewHolder_Chat_RightTxt(View itemView) {
            super(itemView);
            mTv_time = itemView.findViewById(R.id.tv_time);
            mTv_message = itemView.findViewById(R.id.tv_message);
        }
    }

    /**-----------------------------------------------发送图片的信息-----------------------------------------------------*/
    private class ViewHolder_Chat_RightPic extends RecyclerView.ViewHolder {
        TextView mTv_time;
        ImageView mIv_message;
        private ViewHolder_Chat_RightPic(View itemView) {
            super(itemView);
            mTv_time = itemView.findViewById(R.id.tv_time);
            mIv_message = itemView.findViewById(R.id.iv_message);
        }
    }

    /**-----------------------------------------------发送文件的信息-----------------------------------------------------*/
    private class ViewHolder_Chat_RightFile extends RecyclerView.ViewHolder {

        TextView mTv_time;
        TextView mTv_message;

        private ViewHolder_Chat_RightFile(View itemView) {
            super(itemView);
            mTv_time = itemView.findViewById(R.id.tv_time);
            mTv_message = itemView.findViewById(R.id.tv_message);
        }
    }
}
