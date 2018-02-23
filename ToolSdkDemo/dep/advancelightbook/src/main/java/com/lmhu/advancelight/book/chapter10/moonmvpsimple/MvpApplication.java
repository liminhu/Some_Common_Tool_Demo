package com.lmhu.advancelight.book.chapter10.moonmvpsimple;

import android.app.Application;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import demo.utils.MyLog;

/**
 * Created by hulimin on 2018/2/23.
 */

public class MvpApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.e("test ... application ");
        OkHttpFinalConfiguration.Builder builder=new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());
    }
}
