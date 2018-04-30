package com.lin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.lin.widget.utils.DensityUtil;

/**
 * Created by lin on 2017/8/15.
 */

public class CornerTextView extends AppCompatTextView {

    private int mCornerSize;
    private int mBorderColor;
    private int mBorderWidth;

    public CornerTextView(Context context) {
        this(context, null);
    }

    public CornerTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CornerTextView, defStyleAttr, 0);
        mCornerSize = a.getDimensionPixelSize(R.styleable.CornerTextView_corner, 0);
        mBorderColor = a.getColor(R.styleable.CornerTextView_borderColor, 0);
        mBorderWidth = a.getColor(R.styleable.CornerTextView_borderWidth, DensityUtil.dp2px(getContext(), 1));
    }

    public void setBorderColor(int color) {
        mBorderColor = color;
        setTextColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        int strokeWidth = mBorderWidth;
        paint.setAntiAlias(true);
        paint.setColor(mBorderColor);
        Path path = new Path();
        path.moveTo(0, mCornerSize);
        path.quadTo(0, 0, mCornerSize, 0);
        path.lineTo(getMeasuredWidth() - mCornerSize, 0);
        path.quadTo(getMeasuredWidth(), 0, getMeasuredWidth(), mCornerSize);
        path.lineTo(getMeasuredWidth(), getMeasuredHeight() - mCornerSize);
        path.quadTo(getMeasuredWidth(), getMeasuredHeight(), getMeasuredWidth() - mCornerSize, getMeasuredHeight());
        path.lineTo(mCornerSize, getMeasuredHeight());
        path.quadTo(0, getMeasuredHeight(), 0, getMeasuredHeight() - mCornerSize);
        path.close();
        Path path2 = new Path();
        path2.moveTo(strokeWidth, mCornerSize);
        path2.quadTo(strokeWidth, strokeWidth, mCornerSize, strokeWidth);
        path2.lineTo(getMeasuredWidth() - mCornerSize, strokeWidth);
        path2.quadTo(getMeasuredWidth() - strokeWidth, strokeWidth, getMeasuredWidth() - strokeWidth, mCornerSize);
        path2.lineTo(getMeasuredWidth() - strokeWidth, getMeasuredHeight() - mCornerSize);
        path2.quadTo(getMeasuredWidth() - strokeWidth, getMeasuredHeight() - strokeWidth, getMeasuredWidth() - mCornerSize, getMeasuredHeight() - strokeWidth);
        path2.lineTo(mCornerSize, getMeasuredHeight() - strokeWidth);
        path2.quadTo(strokeWidth, getMeasuredHeight() - strokeWidth, strokeWidth, getMeasuredHeight() - mCornerSize);
        path2.close();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            path.op(path2, Path.Op.DIFFERENCE);
            canvas.drawPath(path, paint);
        } else {
            canvas.save();
            canvas.clipPath(path2, Region.Op.DIFFERENCE);
            canvas.drawPath(path, paint);
            canvas.restore();
        }
        super.onDraw(canvas);
    }
}
