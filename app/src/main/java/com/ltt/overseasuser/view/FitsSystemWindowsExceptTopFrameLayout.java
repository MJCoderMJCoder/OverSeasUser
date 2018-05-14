package com.ltt.overseasuser.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/**
 * Created by yunwen on 2018/5/10.
 *为解决软键盘与theme主题冲突问题
 */

public class FitsSystemWindowsExceptTopFrameLayout extends FrameLayout {


    public FitsSystemWindowsExceptTopFrameLayout(Context context) {
        super(context);
    }

    public FitsSystemWindowsExceptTopFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitsSystemWindowsExceptTopFrameLayout(Context context, AttributeSet attrs,
                                                 int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public FitsSystemWindowsExceptTopFrameLayout(Context context, AttributeSet attrs,
                                                 int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setPadding(insets.getSystemWindowInsetLeft(), 0, insets.getSystemWindowInsetRight(),
                    insets.getSystemWindowInsetBottom());
            return insets.replaceSystemWindowInsets(0, insets.getSystemWindowInsetTop(), 0, 0);
        } else {
            return super.onApplyWindowInsets(insets);
        }
    }
}
