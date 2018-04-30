package com.ltt.overseasuser.main.tab.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.ltt.overseasuser.R;

/**
 * Created by Administrator on 2018/1/23.
 */
public class FeedBackDialog extends Dialog {
    private Context mContext;
    private View view;
    public FeedBackDialog(Context context, int theme) {
        super(context, theme);
    }

    public FeedBackDialog(Context context, int theme,double width,double height) {
        super(context, theme);
        mContext=context;
        view= LayoutInflater.from(mContext).inflate(R.layout.dialog_feedback, null);
        setContentView(view);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p =getWindow().getAttributes();
        p.width = (int) (d.getWidth() * width); // 宽度设置为屏幕的0.65
        if(height > 0){
            p.height = (int) (d.getHeight() * height);
        }
        getWindow().setAttributes(p);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
