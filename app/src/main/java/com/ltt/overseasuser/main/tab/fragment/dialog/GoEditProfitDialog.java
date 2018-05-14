package com.ltt.overseasuser.main.tab.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ltt.overseasuser.R;
import com.ltt.overseasuser.main.tab.fragment.activity.ProfileActivity;

/**
 * Created by Administrator on 2018/1/23.
 */
public class GoEditProfitDialog extends Dialog {
    private Context mContext;
    private View view;
    public GoEditProfitDialog(Context context, int theme) {
        super(context, theme);
    }

    public GoEditProfitDialog(Context context, int theme, double width, double height) {
        super(context, theme);
        mContext=context;
        view= LayoutInflater.from(mContext).inflate(R.layout.dialog_go_edit_profile, null);
        setContentView(view);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p =getWindow().getAttributes();
        p.width = (int) (d.getWidth() * width); // 宽度设置为屏幕的0.65
        if(height > 0){
            p.height = (int) (d.getHeight() * height);
        }
        getWindow().setAttributes(p);

        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView btnGoEdit = (TextView) view.findViewById(R.id.btn_go_edit);
        btnGoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mContext.startActivity(new Intent(mContext, ProfileActivity.class));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
