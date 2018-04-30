package com.lin.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by lin on 2017/4/13.
 */

public class SwitchLayout extends LinearLayout {

    private int curPos;
    private int childNum;
    private int corner;
    private int color;
    private int offset;
    private Paint paint;
    private ValueAnimator animator;
    private boolean hasAnim = true;

    public SwitchLayout(Context context) {
        this(context, null);
    }

    public SwitchLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CornerTextView, defStyleAttr, 0);
        corner = a.getDimensionPixelSize(R.styleable.CornerTextView_corner, 0);
        color = a.getColor(R.styleable.CornerTextView_borderColor, 0);
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        childNum = getChildCount();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (!hasAnim) {
            offset = getChildAt(curPos).getLeft();
            hasAnim = true;
        }
        if (childNum != 0) {
            RectF rect = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRoundRect(rect, corner, corner, paint);
            RectF rect2 = new RectF(offset, 0, offset + getChildAt(curPos).getMeasuredWidth(), getMeasuredHeight());
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(rect2, corner, corner, paint);
        }
        super.dispatchDraw(canvas);
    }

    public void setPos(int position) {
        if (position >= childNum || position == curPos)
            return;
        animator = ValueAnimator.ofInt(offset, getChildAt(position).getLeft());
        animator.setDuration(250);
        animator.addUpdateListener(listener);
        animator.start();
        curPos = position;
    }

    public void setPosNoAnim(int position) {
        if (curPos < getChildCount()) {
            curPos = position;
            hasAnim = false;
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (animator != null && animator.isRunning())
            animator.cancel();
        super.onDetachedFromWindow();
    }

    private ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            offset = (int) animation.getAnimatedValue();
            invalidate();
        }
    };
}
