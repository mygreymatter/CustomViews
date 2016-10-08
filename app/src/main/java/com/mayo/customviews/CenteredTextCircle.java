package com.mayo.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * CenteredTextCircle is a custom view which is a circle and text
 * displayed inside the circle.
 *
 * The text is placed in the center of the circle.
 */
public class CenteredTextCircle extends View {
    /**
     * The background color of the circle.
     */
    private Paint mBackgroundColor;
    /**
     * The color of the title.
     */
    private Paint mTitlePaint;

    /**
     * Title displayed in the middle of the circle.
     */
    private CharSequence mTitle;

    public CenteredTextCircle(Context context) {
        super(context);

        init(context, null, 0, 0);
    }

    public CenteredTextCircle(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0, 0);
    }

    public CenteredTextCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, null, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CenteredTextCircle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, null, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CenteredTextCircle, defStyleAttr, defStyleRes);

        try {
            int backgroundColor = a.getColor(R.styleable.CenteredTextCircle_backgroundColor, Color.WHITE);
            float titleSize = a.getDimension(R.styleable.CenteredTextCircle_titleSize,20);
            mTitle = a.getString(R.styleable.CenteredTextCircle_title);

            mBackgroundColor = new Paint();
            mBackgroundColor.setColor(backgroundColor);
            mBackgroundColor.setStyle(Paint.Style.FILL);

            mTitlePaint = new Paint();
            mTitlePaint.setColor(Color.WHITE);
            mTitlePaint.setTextSize(titleSize);
            mTitlePaint.setTextAlign(Paint.Align.CENTER);


        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = 0;
        int measuredWidth = 0;

        switch (mode) {
            //this case sets the exact size specified by the user
            case MeasureSpec.EXACTLY:
                measuredWidth = getContentWidth();
                //check if the given size is greater than the calculated size
                if(measuredWidth < size) {
                    measuredWidth = size;
                }
                break;
            case MeasureSpec.AT_MOST:
                measuredWidth = Math.min(size, getContentWidth());
                break;
            case MeasureSpec.UNSPECIFIED:
                measuredWidth = getContentWidth();
                break;

        }

        mode = MeasureSpec.getMode(heightMeasureSpec);
        size = MeasureSpec.getSize(heightMeasureSpec);

        switch (mode) {
            case MeasureSpec.EXACTLY:
                //height is also measured in width since the view is circular
                measuredHeight = getContentWidth();
                if(measuredHeight < size){
                    measuredHeight = size;
                }
                break;
            case MeasureSpec.AT_MOST:
                measuredHeight = Math.min(size, getContentWidth());
                break;
            case MeasureSpec.UNSPECIFIED:
                measuredHeight = getContentWidth();
                break;

        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    /**
     * Measures the width of the text to displayed inside the circle to be drawn
     *
     * @return width of the text
     */
    private int getContentWidth() {
        int width;

        if(mTitle == null){
            width = 0;
        }else{
            width = (int) (mTitlePaint.getTextSize() * mTitle.length() * 0.6f/*to have gap between border and text*/);
        }

        return width;
    }

    /*
    * Tip: Do not allocate a new object in onDraw
    *
    */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();

        int radius = viewHeight / 2;

        canvas.drawCircle(viewWidth / 2, viewWidth / 2, radius, mBackgroundColor);

        if(mTitle != null) {
            canvas.drawText(mTitle.toString(), viewWidth / 2, viewHeight / 2, mTitlePaint);
        }
    }

    public void setTitle(String title){
        if(!TextUtils.equals(title,mTitle)) {
            mTitle = title;
            //To display the changes made to view like change text, text color, text Size
            invalidate();
            //to compute the view's size and positioning
            requestLayout();
        }
    }
}