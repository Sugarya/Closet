package com.sugarya.closet.ui.widgets.enhancerecycler.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.sugarya.closet.R;


/**
 * Modify by Ethan on 17/01/16
 * 上拉加载更多 动画View
 */
public class LoadingIndicatorView extends View {

    //Sizes (with defaults in DP)
    public static final int DEFAULT_SIZE = 60;

    private Paint mPaint;

    private BaseIndicatorController mIndicatorController;

    private boolean mHasAnimation;


    public LoadingIndicatorView(Context context) {
        this(context, null);
    }

    public LoadingIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingIndicatorView);
        int indicatorColor = a.getColor(R.styleable.LoadingIndicatorView_indicator_color, Color.GRAY);
        a.recycle();

        mPaint = new Paint();
        mPaint.setColor(indicatorColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mIndicatorController = new BallPulseIndicator();
        mIndicatorController.setTarget(this);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(dp2px(DEFAULT_SIZE), widthMeasureSpec);
        int height = measureDimension(dp2px(DEFAULT_SIZE), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mHasAnimation) {
            mHasAnimation = true;
            mIndicatorController.initAnimation();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
    }





    void drawIndicator(Canvas canvas) {
        mIndicatorController.draw(canvas, mPaint);
    }


    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }





    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (v == GONE || v == INVISIBLE) {
                mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.END);
            } else {
                mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mHasAnimation) {
            mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.CANCEL);
    }

}
