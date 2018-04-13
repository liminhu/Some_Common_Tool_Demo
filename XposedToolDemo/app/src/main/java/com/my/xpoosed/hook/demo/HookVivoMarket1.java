package com.my.xpoosed.hook.demo;

import android.content.Context;
import android.util.Log;

import com.my.utils.tool.MyLog;
import com.my.utils.tool.ReflectionUtils;
import com.my.xposedhook.hooks.httpHook;
import com.my.xposedhook.hooks.sockhook;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import okhttp3.Request;

import static de.robv.android.xposed.XposedHelpers.findClass;

public class HookVivoMarket1 implements IXposedHookLoadPackage {
    private static final String TAG="my_xp_hook";
    private  static final String PACKAGE_NAME="com.bbk.appstore";
    private static   Class<?> cls = null;


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals(PACKAGE_NAME)) {
            Log.d(TAG, "hook_Loaded App:" + lpparam.packageName);

            cls = findClass("com.vivo.b.g", lpparam.classLoader);
            //XposedHelpers.findClass("com.vivo.b.g", lpparam.classLoader);







            //Context arg3, String arg4
            XposedHelpers.findAndHookMethod("com.vivo.libs.scrolleffect.r", lpparam.classLoader, "a", Context.class, String.class,

                    new XC_MethodHook() {

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.beforeHookedMethod(param);
                            String request=(String) param.args[1];
                            MyLog.e(TAG+"scrolleffect ..."+request.toString());
                        }


                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            String request=(String)  param.getResult();
                            MyLog.e(TAG+"scrolleffect request ..."+request);
                            //  MyLog.printStackLog("request .. 111");
                        }


                    });







            XposedHelpers.findAndHookMethod("org.json.JSONObject", lpparam.classLoader, "toString",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            String resultString = (String) param.getResult();
                            Log.d(TAG, "hook_JSONObject  ---- " + resultString);
                     /*       if(resultString.contains("sourword")){
                                MyLog.printStackLog("hook_JSONObject");
                            }*/


                            try {
                                Class<?> cls = Class.forName("com.vivo.a.a"); // 取得Class对象
                                boolean a = ReflectionUtils.getBooleanValue(cls, "a");
                                MyLog.e("msg -- " + a);
                                if (!a) {
                                    a = true;
                                    ReflectionUtils.setBooleanValue(cls, "a", a);
                                    MyLog.e("msg -- 重新设值");
                                }
                            } catch (Exception e) {
                                MyLog.e(e.getMessage());
                            }

                        }
                    });


            XposedHelpers.findAndHookMethod("com.vivo.libs.scrolleffect.Wave", lpparam.classLoader, "c",
                    String.class,

                    new XC_MethodHook() {

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.beforeHookedMethod(param);
                            String data = (String) param.args[0];
                            Log.d(TAG, "scrolleffect.Wave -- 04.09 ... " + data);

                              if(data.contains("orxby5xwgj")){
                                  MyLog.printStackLog(data);
                              }

                            // MyLog.printStackLog("base64 com.vivo.libs.b.b  04.02   .."+data);
                        }


                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                         //   String request = (String) param.getResult();
                          //  MyLog.e(TAG+"request ...  111 " + request);

                        }
                    });






            XposedHelpers.findAndHookMethod("com.vivo.libs.scrolleffect.Wave", lpparam.classLoader, "b",
                    String.class,

                    new XC_MethodHook() {

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.beforeHookedMethod(param);
                            String data=(String) param.args[0];
                            Log.d(TAG, "scrolleffect.Wave - "+data);
                          // MyLog.printStackLog("base64 com.vivo.libs.b.b  04.02   .."+data);
                        }

                    });







            //    public final long insert(String arg6, String arg7, ContentValues arg8) {
            XposedHelpers.findAndHookMethod("com.bbk.appstore.util.LogUtility", lpparam.classLoader, "a", String.class,
                    String.class,

                    new XC_MethodHook() {


                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.beforeHookedMethod(param);
                            Log.i(TAG, "0:  --- " + param.args[0]);
                            Log.i(TAG, "1:  ---- " + param.args[1]);


                            try {
                                Class<?> cls = Class.forName("com.bbk.appstore.util.LogUtility"); // 取得Class对象
                                boolean a = ReflectionUtils.getBooleanValue(cls, "a");
                                MyLog.e("msg -- " + a);
                                if (!a) {
                                    a = true;
                                    ReflectionUtils.setBooleanValue(cls, "a", a);
                                    MyLog.e("msg -- 重新设值");
                                }
                            } catch (Exception e) {
                                MyLog.e(e.getMessage());
                            }

                        }
                    });







       //     public static void a(HashMap arg5, boolean arg6)
            XposedHelpers.findAndHookMethod("com.bbk.appstore.f.b", lpparam.classLoader, "a",
                    HashMap.class, boolean.class,

                    new XC_MethodHook() {

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.beforeHookedMethod(param);
                            boolean data1=(boolean) param.args[1];
                            Log.d(TAG, " appstore - "+data1);
                            // MyLog.printStackLog("base64 com.vivo.libs.b.b  04.02   .."+data);
                            HashMap data=(HashMap) param.args[0];
                            if(data!=null){
                                Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry<String, String> entry = it.next();
                                    MyLog.e("befault .. key= " + entry.getKey() + " and value= " + entry.getValue());
                                    if(entry.getKey().equals("appexpo")){
                                        MyLog.printStackLog("appexpo ----- ");
                                    }
                                }
                            }
                        }




                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            HashMap data=(HashMap) param.args[0];
                            if(data!=null){
                                Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry<String, String> entry = it.next();
                                    MyLog.e("befault .. key= " + entry.getKey() + " and value= " + entry.getValue());
                                }
                            }
                        }
                    });

        }
    }
}
