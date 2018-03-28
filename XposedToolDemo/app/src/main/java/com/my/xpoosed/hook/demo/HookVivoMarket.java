package com.my.xpoosed.hook.demo;

import android.content.ContentValues;
import android.util.Log;

import com.my.utils.tool.MyLog;
import com.my.utils.tool.ReflectionUtils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import okhttp3.Response;

public class HookVivoMarket implements IXposedHookLoadPackage {
    private static final String TAG="my_xp_hook";
    private  static final String PACKAGE_NAME="com.bbk.appstore";
    private static   Class<?> cls = null;

    static {
        try{
            cls=Class.forName("com.vivo.libs.b.f.g");
        }catch (Exception e){

        }

    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if(lpparam.packageName.equals(PACKAGE_NAME)) {
            Log.d(TAG,"hook_Loaded App:"+lpparam.packageName);


   /*         XposedHelpers.findAndHookMethod("java.lang.StringBuilder", lpparam.classLoader, "toString",
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


            XposedHelpers.findAndHookMethod("org.json.JSONObject", lpparam.classLoader, "toString",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            String resultString=(String)param.getResult();
                            Log.d(TAG,"hook_JSONObject  ---- "+resultString);
                            if(resultString.contains("sourword")){
                                MyLog.printStackLog("hook_JSONObject");
                            }
                        }
                    });




            //
            XposedHelpers.findAndHookMethod("com.vivo.push.util.i", lpparam.classLoader, "d", String.class,
                    String.class,

                    new XC_MethodHook() {



                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.beforeHookedMethod(param);
                            Log.i(TAG,"push 0:  --- "+param.args[0]);
                            Log.i(TAG,"push 1:  ---- "+param.args[1]);

                            String temp=(String)param.args[0];
                            if(temp.contains("AppStore.AutoFinishHandler")){
                                MyLog.printStackLog("AutoFinishHandler");
                            }


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
                            Log.i(TAG,"0:  --- "+param.args[0]);
                            Log.i(TAG,"1:  ---- "+param.args[1]);

                            String temp=(String)param.args[0];
                            if(temp.contains("AppStore.AutoFinishHandler")){
                                MyLog.printStackLog("AutoFinishHandler");
                            }

                            try{
                                Class<?> cls = Class.forName("com.bbk.appstore.util.LogUtility"); // 取得Class对象
                                boolean a=ReflectionUtils.getBooleanValue(cls, "a");
                                MyLog.e("msg -- "+a);
                                if(!a){
                                    a=true;
                                    ReflectionUtils.setBooleanValue(cls, "a", a);
                                    MyLog.e("msg -- 重新设值");
                                }
                            }catch (Exception e){
                                MyLog.e(e.getMessage());
                            }

                        }
                    });



            XposedHelpers.findAndHookMethod("com.vivo.b.c", lpparam.classLoader, "a", Response.class,
                    cls.getClass(),

                    new XC_MethodHook() {

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.beforeHookedMethod(param);
                            Log.d(TAG, "response ... ");
                            Response response=(Response)param.args[0];
                            Log.e(TAG,"response --- 0:  --- "+response.toString());
                        }
                    });



        }else{
         //   Log.d(TAG,"no hook_Loaded App:"+lpparam.packageName);
        }
    }
}
