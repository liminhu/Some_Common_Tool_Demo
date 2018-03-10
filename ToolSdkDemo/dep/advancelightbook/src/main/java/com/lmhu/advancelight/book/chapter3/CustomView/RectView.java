package com.lmhu.advancelight.book.chapter3.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.lmhu.advancelight.book.R;

/**
 * Created by hulimin on 2018/2/27.
 */

public class RectView extends View {
/*
    继承View，覆盖构造方法
    自定义属性
    重写onMeasure方法测量宽高
    重写onDraw方法绘制控件
*/

    private Paint mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);  //用于绘制时抗锯齿
    private int mColor= Color.RED;



    public RectView(Context context) {
        super(context);
        initDraw();
    }


    private void initDraw() {
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth((float)1.5);
    }

    public RectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray mTypedArray=context.obtainStyledAttributes(attrs, R.styleable.RectView);

        //提取RectView属性集合的rect_color属性
        mColor=mTypedArray.getColor(R.styleable.RectView_rect_color, Color.RED);
        //获取资源后要及时回收
        mTypedArray.recycle();
        initDraw();
    }

    public RectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDraw();
    }


    /**
     * 从所提供的测量规范中提取尺寸
     * 获取默认的宽高值,AT_MOST:子控件至多达到指定大小的值
     * onMeasure方法是由父控件调用的,所有父控件都是ViewGroup的子类
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);
        int widthSpeceSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSpeceSize=MeasureSpec.getSize(heightMeasureSpec);

        //wrap-content  -- 包裹内容就是父窗体并不知道子控件到底需要多大尺寸
        if(widthSpecMode==MeasureSpec.AT_MOST && heightSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension(600, 600);
        }else if(widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(600, heightMeasureSpec);
        }else if(heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthMeasureSpec, 600);
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paddingLeft=getPaddingLeft();
        int paddingRight=getPaddingRight();
        int paddingTop=getPaddingTop();
        int paddintBottom=getPaddingBottom();
        int width=getWidth() - paddingLeft -paddingRight;  //实际宽度
        int height=getHeight() - paddingTop - paddintBottom;
        canvas.drawRect(0+paddingLeft, paddingTop,width+paddingLeft, height+paddingTop, mPaint);
    }
}
