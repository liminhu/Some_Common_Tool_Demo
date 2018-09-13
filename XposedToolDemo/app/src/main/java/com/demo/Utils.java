package com.demo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.my.utils.tool.MyLog;

public class Utils {
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        if(ctx==null){
            MyLog.e(" ctx is null ");
            return "";
        }
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            if(packageInfo!=null) {
                localVersion = packageInfo.versionName;
            }else {
                MyLog.d("packageInfo is null ");
            }
            MyLog.d("TAG ----  %s", "本软件的版本号。。" + localVersion);
        } catch (Exception  e) {
            MyLog.d(" 异常 ： "+e.getMessage());
        }
        return localVersion;
    }



    private static PackageInfo getPackageInfo(Context context){
        try{
            MyLog.e("--- getPackageInfo begin "+context.getPackageName());
            PackageManager pManager = context.getPackageManager();
            PackageInfo pInfo = pManager.getPackageInfo(context.getPackageName(), 0);
            return pInfo;
        }catch (Exception e){
            MyLog.e("log --- "+e.getMessage());
        }
        return null;
    }
}
