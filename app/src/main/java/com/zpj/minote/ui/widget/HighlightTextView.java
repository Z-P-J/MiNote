package com.zpj.minote.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class HighlightTextView extends View {

    private final TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private int mMaxLine;
    private int mTextColor;
    private String mText;

    private StaticLayout mLayout;

    public HighlightTextView(Context context) {
        this(context, null);
    }

    public HighlightTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighlightTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextSize(50);
        mTextPaint.setStyle(Paint.Style.FILL);

//        setText("哈哈哈哈哈哈哈哈哈哈或或或或或或或东方国际，石膏板地方bsdssfdgmgbdfbdjfhgmjd，个得空，gbdkfbgdkfghjbtfjkdffb");
    }


    private String mFirstLine;
    private final Rect mTextBounds = new Rect();

    private int mFirstLineBaseline;

    public void setText(String text) {
        if (TextUtils.equals(text, mText)) {
            return;
        }
        mText = text;
        requestLayout();
//        post(new Runnable() {
//            @Override
//            public void run() {
//                updateText();
//            }
//        });


    }

    private void updateText() {
        mTextPaint.setTextSize(50);
        int width = getWidth() - getPaddingStart() - getPaddingEnd();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mLayout = StaticLayout.Builder.obtain(mText, 0, mText.length(), mTextPaint, width)
                    .setMaxLines(1)
                    .setLineSpacing(0f, 1f)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .build();
        } else {
            mLayout = new StaticLayout(mText, mTextPaint, width,
                    Layout.Alignment.ALIGN_NORMAL,
                    1f, 0f, false);
        }
        int mFirstLineEnd = mLayout.getLineEnd(0);
        mFirstLine = mText.substring(0, mFirstLineEnd);

        mFirstLineBaseline = mLayout.getLineBaseline(0);
        mLayout.getLineBounds(0, mTextBounds);

        mTextPaint.setColor(Color.LTGRAY);
        mTextPaint.setTextSize(40);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mLayout = StaticLayout.Builder.obtain(mText, mFirstLineEnd, mText.length(), mTextPaint, width)
                    .setMaxLines(6)
                    .setLineSpacing(0f, 1f)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .build();
        } else {
            mLayout = new StaticLayout(mText, mFirstLineEnd, mText.length(), mTextPaint, width,
                    Layout.Alignment.ALIGN_NORMAL,
                    1f, 0f, false);
        }
        requestLayout();
//        setMeasuredDimension(getWidth(), getPaddingTop() + getPaddingBottom() + mTextBounds.height() + mLayout.getHeight());
//        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (mLayout != null) {
//            setMeasuredDimension(getWidth(), getPaddingTop() + getPaddingBottom() + mTextBounds.height() + mLayout.getHeight());
//        }


        mTextPaint.setTextSize(50);
        int width = getMeasuredWidth() - getPaddingStart() - getPaddingEnd();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mLayout = StaticLayout.Builder.obtain(mText, 0, mText.length(), mTextPaint, width)
                    .setMaxLines(1)
                    .setLineSpacing(0f, 1f)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .build();
        } else {
            mLayout = new StaticLayout(mText, mTextPaint, width,
                    Layout.Alignment.ALIGN_NORMAL,
                    1f, 0f, false);
        }
        int mFirstLineEnd = mLayout.getLineEnd(0);
        mFirstLine = mText.substring(0, mFirstLineEnd);

        mFirstLineBaseline = mLayout.getLineBaseline(0);
        mLayout.getLineBounds(0, mTextBounds);

        mTextPaint.setColor(Color.LTGRAY);
        mTextPaint.setTextSize(40);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mLayout = StaticLayout.Builder.obtain(mText, mFirstLineEnd, mText.length(), mTextPaint, width)
                    .setMaxLines(6)
                    .setLineSpacing(0f, 1f)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .build();
        } else {
            mLayout = new StaticLayout(mText, mFirstLineEnd, mText.length(), mTextPaint, width,
                    Layout.Alignment.ALIGN_NORMAL,
                    1f, 0f, false);
        }
        setMeasuredDimension(getMeasuredWidth(), getPaddingTop() + getPaddingBottom() + mTextBounds.height() + mLayout.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mFirstLine == null || mLayout == null) {
            return;
        }
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(50);
        canvas.drawText(mFirstLine, getPaddingStart(), mFirstLineBaseline + getPaddingTop(), mTextPaint);

        canvas.save();


        canvas.translate(getPaddingStart(), mTextBounds.height() + getPaddingTop());
        mTextPaint.setTextSize(40);
        mTextPaint.setColor(Color.LTGRAY);
        mLayout.draw(canvas);

        canvas.restore();
    }
}
