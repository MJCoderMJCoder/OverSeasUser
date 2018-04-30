package com.lin.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lin on 2017/7/17.
 */

public class SwipeFooterFactory {

    public static View createSwipeFooter(Context context, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.footer_view, parent, false);
        return view;
    }
}
