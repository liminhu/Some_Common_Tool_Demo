package com.hlm.douyin.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.ryg.dynamicload.DLPlugin;
import com.ryg.dynamicload.internal.DLPluginPackage;

import demo.utils.MyLog;

public class PluginsEntry implements DLPlugin {
    @Override
    public void onCreate(Bundle bundle) {
        MyLog.e("onCreate ... ");
    }

    @Override
    public void onStart() {
        MyLog.e("onStart ... ");
    }

    @Override
    public void onRestart() {
        MyLog.e("onRestart ... ");
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }

    @Override
    public void onResume() {
         MyLog.e("onResume ... ");
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void attach(Activity activity, DLPluginPackage dlPluginPackage) {
            MyLog.e(activity.getClass().getName()+" --- packageName: "+dlPluginPackage.packageName);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams layoutParams) {

    }

    @Override
    public void onWindowFocusChanged(boolean b) {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return false;
    }
}
