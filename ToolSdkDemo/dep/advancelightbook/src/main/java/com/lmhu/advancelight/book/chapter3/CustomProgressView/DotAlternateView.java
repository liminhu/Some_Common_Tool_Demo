package com.lmhu.advancelight.book.chapter3.CustomProgressView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lmhu.advancelight.book.R;

import demo.utils.MyLog;

/**
 * Created by hulimin on 2018/3/12.
 */

public class DotAlternateView extends View{
    private final String TAG="my_log"+this.getClass().getSimpleName();
    private Paint mPaint=new Paint();

    private int mLeftColor; //可视左边圆点的颜色
    private int mRightColor;  //可视右边圆点的颜色
    private int mMiddleColor;
    private int mDotRadius;   //半径

    private int mDotSpacing; //间距
    private float mMoveDistance;   //圆点位移量

    private  float mMoveRate;  //圆点位移动率
    private  final int DOT_STATUS_RIGHT=0x101; // 以刚开始左边圆点为准，向右移
    private final int DOT_STATUS_LEFT=0x102; // 以刚开始左边圆点为准，圆点移动方向向左移

    private int mDotChangeStatus=DOT_STATUS_RIGHT;
    private int mAlphaChangeTotal=130; // 圓点透明度变化最大

    private float mAlphachangeRate;  //透明度变化率
    private float mAlphaChange; //透明度改变量



    public DotAlternateView(Context context) {
       // super(context);
        this(context,null);
    }

    public DotAlternateView(Context context, @Nullable AttributeSet attrs) {
       // super(context, attrs);
        this(context, attrs, 0);
    }

    public DotAlternateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.DotAlternateView, defStyleAttr, 0);
        initAttributes(typedArray);
        typedArray.recycle();
        init();
    }


    private void initAttributes(TypedArray Attributes){
        mLeftColor= Attributes.getColor(R.styleable.DotAlternateView_dot_dark_color, ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mRightColor=Attributes.getColor(R.styleable.DotAlternateView_dot_light_color, ContextCompat.getColor(getContext(), R.color.colorAccent));
        mDotRadius=Attributes.getDimensionPixelSize(R.styleable.DotAlternateView_dot_radius, DensityUtils.dp2px(getContext(), 3));
        mDotSpacing=Attributes.getDimensionPixelSize(R.styleable.DotAlternateView_dot_spacing, DensityUtils.dp2px(getContext(), 6));
        mMoveRate=Attributes.getFloat(R.styleable.DotAlternateView_dot_move_rate, 1.2f);
       // mMiddleColor=Attributes.getColor(R.styleable.DotAlternateView_dot_middle_color, ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    private void init(){
        //移动总距离/移动率 = alpha总变化/x
        //x = 移动率 * alpha总变化 / 移动总距离
        mAlphachangeRate=mMoveRate * mAlphaChangeTotal / (mDotRadius*2 + mDotSpacing);
        mPaint.setColor(mLeftColor);
        mPaint.setAntiAlias(true);  //设置了抗锯齿的，边界明显变模糊了
        mPaint.setStyle(Paint.Style.FILL);
        Log.e(TAG,"mAlphaChangeTotal_"+mAlphaChangeTotal);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MyLog.e(TAG+"测量宽度 ... ");
        //测量宽度
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        if(widthMode == MeasureSpec.EXACTLY){
            width=widthSize;
            MyLog.e(TAG+"onMeasure Exactly...  widthSize="+width);
        }else {
            //指定最小宽度所有圆点加上间距的宽度，以最小半径加上间距算总和加上最左边各最右边变大后的距离
            width=(mDotRadius * 2) *2 +mDotSpacing;
            //最大模式
            if(widthMode == MeasureSpec.AT_MOST){
                width=Math.min(width, widthSize);
            }
        }


        if(heightMode== MeasureSpec.EXACTLY){
            height=heightSize;
        }else {
            height=mDotRadius *2 ;
            if(heightMode == MeasureSpec.AT_MOST){
                height=Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width,height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //左边圆点起点x轴
        int startPointX=getWidth() /2 - (2 * mDotRadius * 2+mDotSpacing)/2 +mDotRadius;
        int startPointY=getHeight() /2;

        //向右移 位移要增加对应透明度变化量也需要增加 反之都需要减小
        if(mDotChangeStatus == DOT_STATUS_RIGHT){
            mMoveDistance += mMoveRate;
            mAlphaChange += mAlphachangeRate;
        }else {
            mAlphaChange -= mAlphachangeRate;
            mMoveDistance -= mMoveRate;
        }

        //当移动到最右，需要改变方向
        if(mMoveDistance >= mDotRadius *2 + mDotSpacing && mDotChangeStatus == DOT_STATUS_RIGHT){
            mDotChangeStatus = DOT_STATUS_LEFT;
            mMoveDistance=mDotRadius *2 + mDotSpacing;
            mAlphaChange=mAlphaChangeTotal;
        }else  if(mMoveDistance <= 0 && mDotChangeStatus == DOT_STATUS_LEFT){
            mDotChangeStatus=DOT_STATUS_RIGHT;
            mMoveDistance=0f;
            mAlphaChange=0f;
        }


        //提供两种颜色设置mLeftColor和mRightColor
        mPaint.setColor(mLeftColor);
        mPaint.setAlpha((int)(255-mAlphaChange));
        canvas.drawCircle(startPointX+mMoveDistance, startPointY, mDotRadius, mPaint);



        mPaint.setColor(mRightColor);
        mPaint.setAlpha((int)(255-mAlphaChange));
        canvas.drawCircle(startPointX+ mDotRadius *2 - mMoveDistance + mDotSpacing, startPointY, mDotRadius, mPaint);

        //Log.e(TAG,"onDraw invalidate ...");

        invalidate();
    }










}
