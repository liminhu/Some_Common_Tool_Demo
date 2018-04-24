package com.test.common.tencent.mm;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.my.utils.tool.MyLog;
import com.my.xpoosed.hook.demo.OkhttpHook;
import com.my.xposedhook.hooks.httpHook;
import com.my.xposedhook.hooks.sockhook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookCommonDemo implements IXposedHookLoadPackage {
    private static final String TAG="my_xp_hook";
    private  static final String PACKAGE_NAME="com.ss.android.ugc.aweme"; //"com.mx.applicationmarket.vivo";
    private static   Class<?> cls = null;


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals(PACKAGE_NAME)) {
            Log.d(TAG, "hook_Loaded App:" + lpparam.packageName);


            //  sockhook.initHooking(lpparam);
            //  httpHook.initHooking(lpparam);
            //  OkhttpHook.initHooking(lpparam);

 /*
            XposedHelpers.findAndHookMethod("java.lang.StringBuilder", lpparam.classLoader, "toString",
                  new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {

                            MyLog.e("1111 ");
                            super.afterHookedMethod(param);
                            String resultString=(String)param.getResult();
                            if(resultString!=null)
                            Log.d(TAG,"my_hook_string_build ---  "+resultString);
                        }
                    });*/


/*

            XposedHelpers.findAndHookMethod("org.json.JSONObject", lpparam.classLoader, "toString",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            String resultString = (String) param.getResult();
                            Log.d(TAG, "hook_JSONObject  ---- " + resultString);
                            if(resultString.startsWith("{\"p1\":\"")){
                                MyLog.printStackLog("--- json ");
                            }
                        }
                    });*/


/*
            View view;
            view.performClick()

                    */


            XposedHelpers.findAndHookMethod("android.view.View", lpparam.classLoader, "performClick",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            Log.d(TAG, "hook_JSONObject  ---- View ");
                            MyLog.e(param.thisObject.getClass().getName()+" ----- ");
                            if(param.thisObject!=null){
                              //  MyLog.printStackLog("View");


                                if(null!=param.thisObject && param.thisObject.getClass().getName().contains("ImageView")){
                                   // MyLog.e("activity -- "+param.thisObject.getClass().getName());
                                  //  ReflectionUtils.getAllFields(param.thisObject,30);
                                    ImageView iv=(ImageView)param.thisObject;
                                    ViewGroup vg= (ViewGroup)iv.getParent().getParent();
                                    ReflectionUtils.getAllFields((Object) vg,30);

                                   // ReflectionUtils.printfView(vg, 33);
                                }
                            }
                        }
                    });





/*


                            XposedHelpers.findAndHookMethod("android.app.Dialog", lpparam.classLoader, "show",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            Log.d(TAG, "hook_JSONObject  ---- dialog ");
                            MyLog.printStackLog("dialog");


                            MyLog.e(param.thisObject.getClass().getName()+" ----- ");

                            if(null!=param.thisObject){
                                MyLog.e("activity -- "+param.thisObject.getClass().getName());
                                ReflectionUtils.getAllFields(param.thisObject,30);
                           //      ViewGroup vg= (ViewGroup)param.thisObject;
                             //    ReflectionUtils.printfView(vg, 33);
                            }
                        }
                    });
*/


        }
    }
}
