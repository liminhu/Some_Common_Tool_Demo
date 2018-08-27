package com.demo;

import android.content.Context;
import android.text.TextUtils;

import com.my.utils.tool.MyLog;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class WeiXinJumpHook implements IXposedHookLoadPackage{
    private static final String WECHAT_PKG_NAME = "com.tencent.mm";
    private static final String APP_PROCESS_NAME="com.tencent.mm:appbrand";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if(WECHAT_PKG_NAME.equals(lpparam.packageName)){
            MyLog.d("apk: begin ... "+lpparam.packageName);
        }

        if ((WECHAT_PKG_NAME.equals(lpparam.packageName)) && lpparam.processName != null && (lpparam.processName.startsWith(APP_PROCESS_NAME))) {
            try {
                MyLog.d("XposedMain %s", lpparam.packageName + "---- lpparam.processName: " + lpparam.processName);
                String versinName="";
                try{
                    Context context=(Context) XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread", new Object[0]), "getSystemContext", new Object[0]);
                    versinName=context.getPackageManager().getPackageInfo(lpparam.packageName, 0).versionName;
                    MyLog.d(" vereion ... "+versinName);
                }catch (Exception e){
                    MyLog.e("异 常 "+e.getMessage());
                }

                if (!TextUtils.equals("6.6.7",versinName)){
                    MyLog.e("no hook --- "+versinName);
                    return;
                }

                HookUtils.initHook(lpparam);
            } catch (Throwable v0) {
                MyLog.d("XposedMain  异常", "" + v0.getMessage());
            }
        }


    }


}