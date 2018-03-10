package com.lmhu.advancelight.book.chapter3.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by hulimin on 2018/2/28.
 */

public class InvalidTextView extends TextView {
    private Paint mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);

    public InvalidTextView(Context context) {
        super(context);
        initDraw();
    }

    public InvalidTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initDraw();
    }

    private void initDraw(){
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth((float)1.5);
    }


    public InvalidTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDraw();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width=getWidth();
        int height=getHeight();
        canvas.drawLine(0, height/2, width, height/2, mPaint);
    }
}
