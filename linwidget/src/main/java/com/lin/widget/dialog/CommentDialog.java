package com.lin.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lin.widget.R;

import java.lang.ref.WeakReference;

/**
 * Created by lin on 2017/9/30.
 */

public class CommentDialog extends Dialog implements View.OnClickListener{

    public interface OnSendListener {
        void sendComment(String content);
    }


    private EditText mEditText;
    private TextView mTvSend;
    private FrameLayout mContainer;
    private InputMethodManager imm;
    private boolean isShowEmoji;
    private boolean fixBug;

    public OnSendListener getOnSendListener() {
        return onSendListener;
    }

    public void setOnSendListener(OnSendListener onSendListener) {
        this.onSendListener = onSendListener;
    }

    private OnSendListener onSendListener;

    public CommentDialog(Context context) {
        super(context);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        handler = new LockHandler(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = View.inflate(getContext(),
                R.layout.layout_comment_dialog, null);

        initView(v);
        setLayout();
        setContentView(v);
    }

    private void initView(View v) {
        mEditText = (EditText) v.findViewById(R.id.et_comment);
        mTvSend = (TextView) v.findViewById(R.id.tv_send);
        mContainer = (FrameLayout) v.findViewById(R.id.fl_container);
        mTvSend.setOnClickListener(this);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && mContainer.getVisibility() == View.VISIBLE) {
                    mContainer.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setLayout() {
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        WindowManager m = getWindow().getWindowManager();
        Display display = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = display.getWidth();
        p.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        getWindow().setAttributes(p);
        if (isShowEmoji) {
            mContainer.setVisibility(View.VISIBLE);
        } else
            mEditText.requestFocus();
    }

    public void fixBug() {
        fixBug = true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_send) {
            String content = mEditText.getText().toString().trim();
            if (onSendListener != null) {
                onSendListener.sendComment(content);
            }
        }
    }

    @Override
    public void show() {
        if (mContainer != null) {
            mContainer.setVisibility(View.GONE);
        }
        handler.sendEmptyMessageDelayed(1, 200);
        super.show();
    }

    public void clear() {
        if (mEditText != null)
            mEditText.setText("");
    }

    private LockHandler handler;

    private static class LockHandler extends Handler {

        private WeakReference<CommentDialog> dialog;

        public LockHandler(CommentDialog dialog) {
            this.dialog = new WeakReference(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            CommentDialog dialog = this.dialog.get();
            if (dialog != null && msg.what == 1) {
                dialog.mEditText.requestFocus();
                dialog.imm.showSoftInput(dialog.mEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }
}
