package com.lmhu.advancelight.book.chapter3.ViewSlide;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import demo.utils.MyLog;

/**
 * Created by hulimin on 2018/2/28.
 */

public class ViewSlideCustomView extends View{
    private int lastX;
    private int lastY;
    private Scroller mScroller;

    public ViewSlideCustomView(Context context) {
        super(context);
        MyLog.e(" 1111 ");
    }

    public ViewSlideCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller=new Scroller(context);
        MyLog.e(" 22222 ");
    }

    public ViewSlideCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        MyLog.e(" 3333333 ");
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取到手指处的横坐标和纵坐标
        int x=(int)event.getX();
        int y=(int)event.getY();
        MyLog.e("event -- "+event.getAction());
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX=x;
                lastY=y;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算移动距离
                int offsetX=x-lastX;
                int offsetY=y-lastY;
                ((View)getParent()).scrollBy(-offsetX, -offsetY);
                break;
        }
        return true;
    }


    public void smoothScrollTo(int destX, int destY){
        int scrollX=getScrollX();
        int delta=destX-scrollX;
        //1000s内滑向destX
        mScroller.startScroll(scrollX,0, delta,0, 2000);
        invalidate();  //invalidate方法会执行draw过程，重绘View树。
    }


    //父类要求它的子类滚动的时候调用。

    @Override
    public void computeScroll() {
        super.computeScroll();
        MyLog.e("... scroll ");
        if(mScroller.computeScrollOffset()){
            ((View)getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //通过不断的重绘不断的调用computeScroll方法
            invalidate();
        }
    }
}
