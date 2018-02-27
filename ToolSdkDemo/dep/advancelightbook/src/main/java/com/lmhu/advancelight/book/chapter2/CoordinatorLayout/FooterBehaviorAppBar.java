package com.lmhu.advancelight.book.chapter2.CoordinatorLayout;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import java.util.regex.Matcher;

import demo.utils.MyLog;

/**
 * Created by hulimin on 2018/2/27.
 */

public class FooterBehaviorAppBar extends CoordinatorLayout.Behavior<View> {
    public FooterBehaviorAppBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float translationY= Math.abs(dependency.getY());
        MyLog.e("translationY :"+translationY);
        child.setTranslationY(translationY);
        return true;
    }
}


