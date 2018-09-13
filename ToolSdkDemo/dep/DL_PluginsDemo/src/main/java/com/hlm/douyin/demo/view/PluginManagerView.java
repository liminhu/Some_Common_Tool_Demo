package com.hlm.douyin.demo.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import demo.toolsdk.hlm.com.webviewlib.R;
import demo.utils.MyLog;

public class PluginManagerView extends FrameLayout implements View.OnTouchListener{

    private FrameLayout frame;
    private CheckBox checkBox;
    private Context targetContext;
    private Context selfContext;
    private Activity currentActivity;
    private Handler handler;
    private WindowManager wm;
   // private SensorManager sensorManager;
    private WindowManager.LayoutParams layoutParams;
    private boolean resumed = false;
    private WindowManager sysWindowManager;
    private boolean failAdd;
    private static Integer prevX = null, prevY = null;

    public void onActivityCreate(Activity activity, Bundle bundle) {
        currentActivity = activity;
        MyLog.w("<OnActivityCreate> %s", activity);
        handler.sendEmptyMessage(1);
    }

    public void onActivityResumed(Activity activity) {
        resumed = true;
        currentActivity = activity;
        handler.sendEmptyMessageDelayed(1, 300);
        MyLog.e("<onActivityResumed>%s（%s） %s, %s", activity, activity.getParent(), activity.getWindow(), activity.getWindowManager());
    }

    public void onActivityPaused(Activity activity) {
        resumed = false;
        handler.removeCallbacksAndMessages(null);
        if (layoutParams.type == WindowManager.LayoutParams.TYPE_SYSTEM_ALERT) {
            wm = sysWindowManager;
        }
        try {
            wm.removeView(PluginManagerView.this);
            MyLog.e("<onActivityPaused> remove view... %s, %s", activity, PluginManagerView.this);
        } catch (Exception e) {
            //  MyLog.e(e, "<onActivityPaused> Exception---" + e.toString());
        }
    }


    public void onActivityDestroy(Activity activity) {
        handler.removeCallbacksAndMessages(null);
        MyLog.e("<onActivityDestroy>---" + activity.toString());

    }


    private class PluginManagerViewHandler extends Handler {

        PluginManagerViewHandler(Looper l) {
            super(l);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //开启悬浮框
                case 0:
                    MyLog.e("<MSG_CHECK_FLOATWINDOW> %s ,%s", currentActivity, PluginManagerView.this);
                    if (getWidth() == 0 || getHeight() == 0) {
                        sendEmptyMessageDelayed(1, 100);
                    }
                    break;
                case 1:
                    if (currentActivity == null)
                        return;
                    if (!resumed || currentActivity.getWindow().getDecorView().getWidth() == 0) {
                        sendEmptyMessageDelayed(1, 500);
                        return;
                    }
                    try {
//                        MyLog.w("<MSG_SHOW_PLUGINMANAGER> add to --> %s (%s) %s", currentActivity, getParent(), currentActivity.getWindow().getDecorView());
                        wm = currentActivity.getWindowManager();
                        if (failAdd) {//皇家守卫军
//                            if (!SystemInfo.getAppOps(targetContext)) {
                            if (targetContext.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
                                try {
                                    currentActivity.getPackageManager().getPackageInfo("miui", 0);
                                    Toast.makeText(currentActivity, "请注意：您的MIUI可能需要允许本游戏的悬浮窗权限，才能显示MOD窗口", Toast.LENGTH_LONG).show();
                                } catch (Exception ignored) {
                                    ignored.printStackTrace();
                                }
                            } else {
                                Toast.makeText(currentActivity, "请注意：您可能需要允许GG大玩家的悬浮窗权限，才能显示MOD窗口", Toast.LENGTH_LONG).show();
                            }
                            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                            wm = sysWindowManager;
                        }
                        MyLog.w(" <PluginManagerView.MSG_SHOW_PLUGINMANAGER> +" + layoutParams.type);
                        wm.addView(PluginManagerView.this, layoutParams);
                    } catch (Exception ee) {
                        MyLog.e(" <PluginManagerView.MSG_SHOW_PLUGINMANAGER> addviewException  " + ee.toString());
                        if (!failAdd) {
                            failAdd = true;
                            sendEmptyMessageDelayed(1, 500);
                            return;
                        }
                    }

                    if (prevX != null && prevY != null) {
                        WindowManager.LayoutParams p = (WindowManager.LayoutParams) getLayoutParams();
                        p.x = prevX;
                        p.y = prevY;
                        updateLayout(p);
                    }
                    setVisibility(VISIBLE);
                    MyLog.i("<MSG_SHOW_PLUGINMANAGER> %s %s(%s,%s)", currentActivity, PluginManagerView.this, currentActivity.getWindow(), currentActivity.getWindowManager());
                    removeMessages(1);
                    break;
                default:
                    break;
            }
        }
    }

    // TODO: 2017/8/3 反转隐藏
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        long lastime = 0;

        @Override
        public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                if (z < -8 && x < 5 && x > -5 && y < 5 && y > -5) {
                    if (System.currentTimeMillis() - lastime > 1500) {
                        if (getVisibility() != View.GONE) {
                            setVisibility(View.GONE);
                        } else {
                            if (failAdd) {
                                handler.sendEmptyMessage(1);
                            }
                            setVisibility(View.VISIBLE);
                        }
                        lastime = System.currentTimeMillis();
                    }
                }

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public PluginManagerView(Activity activity) {
        super(activity);

        MyLog.d("dddd -- ");

        this.selfContext = PluginEntry.getInstance().getContext();
        this.targetContext = PluginEntry.getInstance().getTargetApplication();
        sysWindowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        handler = new PluginManagerViewHandler(activity.getMainLooper());

        final View view = inflate(selfContext, R.layout.mainview, this);
        view.setOnTouchListener(this);
        checkBox = (CheckBox) view.findViewById(R.id.check);
        checkBox.setOnTouchListener(new DragListener());
        frame = (FrameLayout) view.findViewById(R.id.frame);
        checkBox.setChecked(false);
        frame.setVisibility(GONE);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //MyLog.e("isChecked -- "+isChecked+"\t"+frame.toString());
                View newView=PluginEntry.getInstance().OnPluginUIShow();
                if(newView!=null){
                    //  MyLog.e(newView.toString());
                    int index=frame.indexOfChild(newView);
                    if(index<0){
                        if(newView.getParent()!=null){
                            ((ViewGroup)newView.getParent()).removeAllViews();
                        }
                        frame.removeAllViews();
                        frame.addView(newView);
                    }
                }
                frame.setVisibility(!isChecked ? GONE : VISIBLE);
            }
        });

        if (layoutParams == null) {
            layoutParams = new WindowManager.LayoutParams();
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            layoutParams.width = LayoutParams.WRAP_CONTENT;
            layoutParams.height = LayoutParams.WRAP_CONTENT;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            layoutParams.format = PixelFormat.TRANSLUCENT;
            layoutParams.type = WindowManager.LayoutParams.LAST_SUB_WINDOW;
        }
      //  sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        View newView=PluginEntry.getInstance().OnPluginUIShow();
        try{
            if(newView.getParent()!=null){
                ((ViewGroup)newView.getParent()).removeView(newView);
            }
            frame.removeAllViews();
            frame.addView(newView);
        }catch (Exception e){
            MyLog.e("exception "+e.getMessage());
        }

    }


    //点游戏隐藏hide
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //新加 hide manager
//        if (currentShow == ShowState.ShowManager) {
//            final int x = (int) event.getX();
//            final int y = (int) event.getY();
//            if ((event.getAction() == MotionEvent.ACTION_DOWN) && ((x < 0) || (x >= v.getWidth()) || (y < 0) || (y >= v.getHeight()))) {
//                switchShowState(ShowState.ShowManager, ShowState.ShowDrag, true);
//                return true;
//            } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                switchShowState(ShowState.ShowManager, ShowState.ShowDrag, true);
//                return true;
//            }
//        }
//        if () {
//            final int x = (int) event.getX();
//            final int y = (int) event.getY();
//            if ((event.getAction() == MotionEvent.ACTION_DOWN) && ((x < 0) || (x >= v.getWidth()) || (y < 0) || (y >= v.getHeight()))) {
//
//                return true;
//            } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//
//                return true;
//            }
//        }
        return false;
    }


    private void updateLayout(int startX, int startY, int offsetX, int offsetY, int width, int height) {
        prevX = layoutParams.x = Math.min(width, Math.max(0, offsetX + startX));
        prevY = layoutParams.y = Math.min(height, Math.max(0, offsetY + startY));
        updateLayout(layoutParams);
    }

    private void updateLayout(WindowManager.LayoutParams lp) {
        setLayoutParams(lp);
        if (currentActivity != null) {
            try {
                WindowManager wm = currentActivity.getWindowManager();
                if (layoutParams.type == WindowManager.LayoutParams.TYPE_SYSTEM_ALERT) {
                    wm = (WindowManager) currentActivity.getSystemService(Context.WINDOW_SERVICE);
                }
                wm.updateViewLayout(PluginManagerView.this, lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            layoutParams = (WindowManager.LayoutParams) getLayoutParams();
        }
    }

    // TODO: 17-5-23 拖拽
    private class DragListener implements OnTouchListener {
        private int touchX, touchY, startX, startY;
        private boolean isDraged = false;
        private int x, y;
        private int fullWidth, fullHeight;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // MyLog.e("touch +");
            touchX = (int) event.getRawX();
            touchY = (int) event.getRawY();
            boolean isHandle = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    fullWidth = currentActivity.getWindow().getDecorView().getRight() - currentActivity.getWindow().getDecorView().getLeft();
                    fullHeight = currentActivity.getWindow().getDecorView().getBottom() - currentActivity.getWindow().getDecorView().getTop();
//                    if (linearLayout.getVisibility() == GONE) {
//                    FrameLayout.LayoutParams ll = (FrameLayout.LayoutParams) cb_toggle.getLayoutParams();
//                    if (layoutParams.x == 0 || layoutParams.y == 0) {
//                        ll.leftMargin = ll.topMargin = ll.bottomMargin = ll.rightMargin = 0;
//                    }
//                    cb_toggle.setLayoutParams(ll);
//                        fullWidth -= cb_toggle.getWidth();
//                        fullHeight -= cb_toggle.getHeight();
//                    } else {
//                    fullWidth -= CountdownView.this.getWidth();
//                    fullHeight -= CountdownView.this.getHeight();
//                    }
                    isDraged = false;
                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
                    x = layoutParams.x;
                    y = layoutParams.y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isDraged) {
                        updateLayout(x, y, touchX-startX, touchY - startY, fullWidth, fullHeight);
//                        updateLayout(x, y, 0, touchY - startY, fullWidth, fullHeight);
                    } else if (Math.abs( touchX-startX) >= v.getWidth() / 4 || Math.abs(touchY - startY) >= v.getHeight() / 4) {
                        isDraged = true;
                    }
                    isHandle = isDraged;
                    break;
                case MotionEvent.ACTION_UP:
//                    if (isDraged && linearLayout.getVisibility() == GONE) {
//                        FrameLayout.LayoutParams ll1 = (FrameLayout.LayoutParams) cb_toggle.getLayoutParams();
//                        if (layoutParams.x == 0) {
//                            ll1.rightMargin = -cb_toggle.getWidth() / 2;
//                        }
//                        cb_toggle.setLayoutParams(ll1);
//                    }
                    isHandle = isDraged;
                    isDraged = false;
                    break;
            }
            return isHandle;
        }
    }
}