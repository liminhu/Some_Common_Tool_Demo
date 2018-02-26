package com.lmhu.advancelight.book.chapter1.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import demo.utils.MyLog;

/**
 * Created by hulimin on 2018/2/26.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration{
    private static final int[] ATTRS=new int[]{
        android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST= LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST=LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private int mOrientation;


    public DividerItemDecoration(Context context, int mOrientation) {
        final TypedArray a=context.obtainStyledAttributes(ATTRS);
        mDivider=a.getDrawable(0);
        a.recycle();
        setOrientation(mOrientation);

    }

    public void setOrientation(int mOrientation) {
        if(mOrientation != HORIZONTAL_LIST && mOrientation!=VERTICAL_LIST){
            throw  new IllegalArgumentException("invalid orientation");
        }
        this.mOrientation = mOrientation;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
       // MyLog.e(" --- onDraw ");
        if(mOrientation == VERTICAL_LIST){
            drawVertical(c,parent);
        }else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent){
       // MyLog.e("-----  111");

        final int left=parent.getPaddingLeft();
        final  int right=parent.getWidth() - parent.getPaddingRight();
        final int childCount=parent.getChildCount();

        for(int i=0; i<childCount; i++){
            final View child=parent.getChildAt(i);
            RecyclerView v=new RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)child.getLayoutParams();
            final int top=child.getBottom()+params.bottomMargin;
            final int bottom=top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left,top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent){
      //  MyLog.e("-----  111");
        final int top=parent.getPaddingTop();
        final int bottom=parent.getHeight()-parent.getPaddingRight();
        final int childCount=parent.getChildCount();

        for(int i=0; i<childCount; i++){
            final View child=parent.getChildAt(i);
            final RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)child.getLayoutParams();
            final int left=child.getRight() + params.rightMargin;
            final  int right=left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);
        if(mOrientation == VERTICAL_LIST){
            outRect.set(0,0,0,mDivider.getIntrinsicHeight());
        }else {
            outRect.set(0,0,mDivider.getIntrinsicWidth(), 0);
        }
    }
}
