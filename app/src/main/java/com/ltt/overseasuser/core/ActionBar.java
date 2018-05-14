package com.ltt.overseasuser.core;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ltt.overseasuser.R;


public class ActionBar {

    View container;
    View statusBar;
    ImageView btnLeft;
    TextView tvTitle;
    ImageView btnRight;
    ImageView btnRight2;
    ImageView iv_right;
    TextView rightBtn;
    ImageView img_center;
    RelativeLayout rl_content;
    ImageView mBtnLeft2;

    RelativeLayout rl_usertitle;
    TextView tv_top;
    TextView tv_bottom;

    private ActionBar() {

    }

    public static ActionBar init(Activity activity) {
        return init(activity.findViewById(R.id.action_bar));
    }

    public static ActionBar init(View view) {
        ActionBar bar = new ActionBar();
        bar.setContentView(view);
        bar.initStatusBar(view);
        return bar;
    }

    private void initStatusBar(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return;
        int result = 0;
        int resourceId = view.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = view.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        statusBar.getLayoutParams().height = result;
    }

    private void setContentView(View view) {
        container = view;
        statusBar = view.findViewById(R.id.status_bar);
        btnLeft = (ImageView) view.findViewById(R.id.btn_left);
        mBtnLeft2 = (ImageView) view.findViewById(R.id.btn_left2);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btnRight = (ImageView) view.findViewById(R.id.btn_right);
        btnRight2 = (ImageView) view.findViewById(R.id.btn_right2);
        iv_right = (ImageView) view.findViewById(R.id.iv_notify);
        rightBtn = (TextView) view.findViewById(R.id.right_btn);
        img_center= (ImageView) view.findViewById(R.id.img_center);
        rl_content= (RelativeLayout) view.findViewById(R.id.rl_content);

        rl_usertitle = (RelativeLayout) view.findViewById(R.id.rl_usertitle);
        tv_top = (TextView) view.findViewById(R.id.tv_top);
        tv_bottom = (TextView) view.findViewById(R.id.tv_bottom);

    }
    public void showcenter(){
        img_center.setVisibility(View.VISIBLE);
    }
    public void showNotify(){
        iv_right.setVisibility(View.VISIBLE);
    }

    public void setBackground(int resId){
        rl_content.setBackgroundResource(resId);
    }

    public void transparent() {
        container.setBackgroundColor(container.getResources().getColor(R.color.transparent));
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitle(int resId) {
        tvTitle.setText(resId);
    }

    public void setLeft(int resId) {
        btnLeft.setImageResource(resId);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                if (context instanceof Activity)
                    ((Activity) context).finish();
            }
        });
    }

    public void setLeft(int resId, View.OnClickListener listener) {
        btnLeft.setImageResource(resId);
        btnLeft.setOnClickListener(listener);
    }

    public void setLeft2(int resId) {
        mBtnLeft2.setVisibility(View.VISIBLE);
        mBtnLeft2.setImageResource(resId);
    }

    public void setLeft2(int resId , View.OnClickListener listener) {
        mBtnLeft2.setVisibility(View.VISIBLE);
        mBtnLeft2.setImageResource(resId);
        mBtnLeft2.setOnClickListener(listener);
    }

    public void setRight(int resId, View.OnClickListener listener) {
        btnRight.setImageResource(resId);
        btnRight.setOnClickListener(listener);
    }

    public void setRight2(int resId, View.OnClickListener listener) {
        btnRight2.setVisibility(View.VISIBLE);
        btnRight2.setImageResource(resId);
        btnRight2.setOnClickListener(listener);
    }

    public void setRightButton(int resId, View.OnClickListener listener) {
        rightBtn.setText(resId);
        rightBtn.setOnClickListener(listener);
        btnRight.setVisibility(View.GONE);
        rightBtn.setVisibility(View.VISIBLE);
    }

    public void setRightButton(String text, View.OnClickListener listener) {
        rightBtn.setText(text);
        rightBtn.setOnClickListener(listener);
        btnRight.setVisibility(View.GONE);
        rightBtn.setVisibility(View.VISIBLE);
    }

    public void showRlTitle() {
        rl_usertitle.setVisibility(View.VISIBLE);
    }

    public void setTop(String top) {
        tv_top.setText(top);
    }

    public void setBottom(String bottm) {
        tv_bottom.setText(bottm);
    }

    public ImageView getLeft() {
        return btnLeft;
    }

    public ImageView getLeft2() {
        return mBtnLeft2;
    }

    public ImageView getRight() {
        return btnRight;
    }

    public ImageView getRight2() {
        return btnRight2;
    }

    public TextView getBtnRight() {
        return rightBtn;
    }
}
