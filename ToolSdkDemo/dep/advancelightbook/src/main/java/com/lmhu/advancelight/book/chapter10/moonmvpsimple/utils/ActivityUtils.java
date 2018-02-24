package com.lmhu.advancelight.book.chapter10.moonmvpsimple.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import demo.utils.MyLog;

/**
 * Created by hulimin on 2018/2/23.
 */

public class ActivityUtils {
    public static void addFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int frameId){
        MyLog.e("utils ... frameId ---- "+ frameId);
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }
}
