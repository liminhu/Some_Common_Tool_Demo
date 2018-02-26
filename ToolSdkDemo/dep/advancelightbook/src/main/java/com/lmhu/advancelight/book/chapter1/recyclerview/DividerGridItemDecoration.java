package com.lmhu.advancelight.book.chapter1.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import demo.utils.MyLog;

/**
 * Created by hulimin on 2018/2/26.
 */

public class DividerGridItemDecoration extends RecyclerView.ItemDecoration{
    private static final int[] ATTRS=new int[]{
        android.R.attr.listDivider
    };


    private Drawable mDivider;

    public DividerGridItemDecoration(Context context) {
        final TypedArray a=context.obtainStyledAttributes(ATTRS);
        mDivider=a.getDrawable(0);
        a.recycle();

    }


    private int getSpanCount(RecyclerView parent){
        int spanCount=-1;
        RecyclerView.LayoutManager layoutManager=parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            spanCount=((GridLayoutManager) layoutManager).getSpanCount();
        }else if(layoutManager instanceof StaggeredGridLayoutManager){
            spanCount=((StaggeredGridLayoutManager)layoutManager).getSpanCount();
        }
        return spanCount;
    }







    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
       // MyLog.e(" --- onDraw ");
        drawVertical(c,parent);
        drawHorizontal(c, parent);
    }

    public void drawVertical(Canvas c, RecyclerView parent){
       // MyLog.e("-----  111");
        final int childCount=parent.getChildCount();

        for(int i=0; i<childCount; i++){
            final View child=parent.getChildAt(i);
            final RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)child.getLayoutParams();

            final int top=child.getTop()-params.topMargin;
            final int bottom=child.getBottom() + params.bottomMargin;
            final int left=child.getRight() +params.rightMargin;
            final int right=left + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left,top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent){
      //  MyLog.e("-----  111");
        final int childCount=parent.getChildCount();

        for(int i=0; i<childCount; i++){
            final View child=parent.getChildAt(i);
            final RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)child.getLayoutParams();

            final int left=child.getLeft()- params.leftMargin;
            final int right=child.getRight()+params.rightMargin+mDivider.getIntrinsicWidth();

            final int top=child.getBottom() + params.bottomMargin;
            final int bottom=top+ mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }


    private boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount){
     //   MyLog.e("isLastColum  -- ");
        RecyclerView.LayoutManager layoutManager=parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            if((pos +1 ) % spanCount == 0){  //如果是最后一列
                return true;
            }
        }else if(layoutManager instanceof  StaggeredGridLayoutManager){
            int orientation=((StaggeredGridLayoutManager)layoutManager).getOrientation();
            if(orientation == StaggeredGridLayoutManager.VERTICAL){
                if((pos +1)% spanCount==0){
                    return true;
                }
            }else {
                childCount=childCount - childCount % spanCount;
                if(pos >= childCount){
                    return  true;
                }
            }
        }
        return false;
    }




    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount){
       // MyLog.e("isLastRaw  -- ");
        RecyclerView.LayoutManager layoutManager=parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            if(pos >= childCount){  //如果是最后一列
                return true;
            }
        }else if(layoutManager instanceof  StaggeredGridLayoutManager){
            int orientation=((StaggeredGridLayoutManager)layoutManager).getOrientation();
            if(orientation == StaggeredGridLayoutManager.VERTICAL){
                childCount=childCount - childCount % spanCount;
                if(pos >= childCount){
                    return true;
                }
            }else {
               if((pos+1) % spanCount ==0){
                   return true;
               }
            }
        }
        return false;
    }




    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);
        int spanCount=getSpanCount(parent);
        int childCount=parent.getAdapter().getItemCount();

        if(isLastRaw(parent, itemPosition, spanCount, childCount)){// 如果是最后一行，则不需要绘制底部
            outRect.set(0,0,mDivider.getIntrinsicWidth(), 0);
        }else if(isLastColum(parent, itemPosition, spanCount, childCount)) {// 如果是最后一列，则不需要绘制右边
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }else{
            outRect.set(0,0,mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
        }
    }
}
