package com.lmhu.floatwindow.permission;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.lmhu.floatwindow.R;

import demo.utils.MyLog;
import demo.utils.ResourceUtil;

/**
 * Created by hulimin on 2017/12/17.
 */

public class AVCallFloatView extends FrameLayout {
    private static final String TAG="CallFloatView";

    /**
     * 记录手指按下时在小悬浮窗View上的横坐标
     */
    private float xInView;
    private float yInview;

    /**
     * 记录手指在屏幕上的横坐标的值
     */
    private float xInScreen;
    private float yInScreen;


    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;
    private float yDownInScreen;

    private boolean isAnchoring=false;
    private boolean isShowing =false;
    private WindowManager windowManager=null;
    private WindowManager.LayoutParams mParams=null;


    public AVCallFloatView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        windowManager=(WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater=LayoutInflater.from(getContext());
        int linelayout= ResourceUtil.getLayoutId(getContext(), "activity_float_window");
        View floatView=inflater.inflate(linelayout,null);
        addView(floatView);
    }


    public void setmParams(WindowManager.LayoutParams mParams) {
        this.mParams = mParams;
    }


    public void setShowing(boolean showing) {
        isShowing = showing;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isAnchoring){
            return true;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xInView=event.getX();
                yInview=event.getY();
                xDownInScreen=event.getRawX();
                yDownInScreen=event.getRawY();
                xInScreen=event.getRawX();
                yInScreen=event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                xInScreen=event.getRawX();
                yInScreen=event.getRawY();
                //手指移动的时候更新小悬浮窗的位置
                updateViewPosition();
                break;

            case MotionEvent.ACTION_UP:
                // TODO: 2017/12/17 有待查证这个作用
                if(Math.abs(xDownInScreen-xInScreen) <= ViewConfiguration.get(getContext()).getScaledTouchSlop()
                        && Math.abs(yDownInScreen-yInScreen)<=ViewConfiguration.get(getContext()).getScaledTouchSlop()){
                    //点击效果
                    Toast.makeText(getContext(), "this float window is clicked", Toast.LENGTH_SHORT).show();
                }else {
                    //吸咐效果
                    anchorToSide();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void anchorToSide() {

    }

    private void updateViewPosition() {
        mParams.x=(int)(xInScreen-xInView);
        mParams.y=(int)(yInScreen-yInview);
        MyLog.e("x:"+mParams.x+"---- y:"+mParams.y);
        windowManager.updateViewLayout(this,mParams);
    }
}
