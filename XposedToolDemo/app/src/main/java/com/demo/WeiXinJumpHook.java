package com.demo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hook.wxx5.WeixinVer6_6_7;
import com.hook.wxx5.WeixinVerBase;
import com.my.utils.tool.MyLog;

import org.json.JSONObject;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
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
                try {
                    // hook 微信X5内核
                    weixin = new WeixinVer6_6_7();
                    // hookX5Kernel(loadPackageParam);
                    hookLog(lpparam.classLoader);

                }catch (Throwable e){
                    Log.e(ERROR_TAG, "Hook X5 异常", e);
                }

            } catch (Throwable v0) {
                MyLog.d("XposedMain  异常", "" + v0.getMessage());
            }
        }


    }









    private final static String TAG = "my_wx_x5";
    private final static String ERROR_TAG = "my_wx_x5.Error";
    private final static String INJECT_TAG = "my_wx_x5.Inject";
    private final static String JS_TAG = "my_wx_x5.jsLog";
    private final static String WX_TAG = "my_wx_x5.wxLog";
    private static WeixinVerBase weixin;


    public void hookLog(ClassLoader loader) throws Throwable {

        XC_MethodHook logCallback = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                boolean log = false;
                Throwable ex = new Throwable();
                StackTraceElement[] elements = ex.getStackTrace();
                for (StackTraceElement element : elements) {
                    if (element.getClassName().contains("com.tencent.mm.plugin.appbrand")) {
                        log = true;
                        break;
                    }
                }
                if (!log) {
                    return;
                }
                int level = 0;
                String name = param.method.getName();
                String arg0 = (String) param.args[0];
                String arg1 = (String) param.args[1];
                Object[] arg2 = (Object[]) param.args[2];
                String format = arg2 == null ? arg1 : String.format(arg1, arg2);
                if (TextUtils.isEmpty(format)) {
                    format = "null";
                }
                if (name.equals(weixin.WXLOG_SIMPLE_FUN_LOG_F) || name.equals(weixin.WXLOG_SIMPLE_FUN_LOG_I)) {
                    level = 0;
                } else if (name.equals(weixin.WXLOG_SIMPLE_FUN_LOG_D) || name.equals(weixin.WXLOG_SIMPLE_FUN_LOG_V) || name.equals(weixin.WXLOG_SIMPLE_FUN_LOG_K) || name.equals(weixin.WXLOG_SIMPLE_FUN_LOG_L)) {
                    level = 1;
                } else if (name.equals(weixin.WXLOG_SIMPLE_FUN_LOG_E) || name.equals(weixin.WXLOG_SIMPLE_FUN_LOG_W)) {
                    level = 2;
                }
                switch (level) {
                    case 0:
                        Log.i(WX_TAG + " " + arg0, format);
                        break;
                    case 1:
                        Log.d(WX_TAG + " " + arg0, format);
                        break;
                    case 2:
                        Log.e(WX_TAG + " " + arg0, format);
                        break;
                }
            }
        };

        Class<?> logClass = loader.loadClass(weixin.WXLOG_CLS_PLATFORMTOOLS_LOG);
        XposedHelpers.findAndHookMethod(logClass, weixin.WXLOG_SIMPLE_FUN_LOG_F, String.class, String.class, Object[].class, logCallback);
        XposedHelpers.findAndHookMethod(logClass, weixin.WXLOG_SIMPLE_FUN_LOG_E, String.class, String.class, Object[].class, logCallback);
        XposedHelpers.findAndHookMethod(logClass, weixin.WXLOG_SIMPLE_FUN_LOG_W, String.class, String.class, Object[].class, logCallback);
        XposedHelpers.findAndHookMethod(logClass, weixin.WXLOG_SIMPLE_FUN_LOG_I, String.class, String.class, Object[].class, logCallback);
        XposedHelpers.findAndHookMethod(logClass, weixin.WXLOG_SIMPLE_FUN_LOG_D, String.class, String.class, Object[].class, logCallback);
        XposedHelpers.findAndHookMethod(logClass, weixin.WXLOG_SIMPLE_FUN_LOG_V, String.class, String.class, Object[].class, logCallback);
        XposedHelpers.findAndHookMethod(logClass, weixin.WXLOG_SIMPLE_FUN_LOG_K, String.class, String.class, Object[].class, logCallback);
        XposedHelpers.findAndHookMethod(logClass, weixin.WXLOG_SIMPLE_FUN_LOG_L, String.class, String.class, Object[].class, logCallback);

        // 将小程序日志自定义转发到java
        Class<?> arg0Class = loader.loadClass(weixin.ABLOG_CLS_JSAPI_PARMA0);
        XposedHelpers.findAndHookMethod(weixin.ABLOG_CLS_JSAPI_LOG, loader, weixin.ABLOG_SIMPLE_FUN_JSAPI_LOG, arg0Class, JSONObject.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                JSONObject jsonObjectArg1 = (JSONObject) param.args[1];
                int l = jsonObjectArg1.getInt("level");
                String logs = jsonObjectArg1.getString("logs");
                switch (l) {
                    case 0:
                        Log.d(JS_TAG, logs);
                        break;
                    case 1:
                        Log.i(JS_TAG, logs);
                        break;
                    case 2:
                        Log.w(JS_TAG, logs);
                        break;
                    case 3:
                        Log.e(JS_TAG, logs);
                        break;
                }
            }
        });
    }




}