package com.my.xpoosed.hook.demo;

import android.util.Log;

import com.my.utils.tool.MyLog;
import com.my.xposedhook.hooks.httpHook;
import com.my.xposedhook.hooks.sockhook;

import java.util.HashMap;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import okhttp3.Request;

import static de.robv.android.xposed.XposedHelpers.findClass;

public class HookVivoMarket implements IXposedHookLoadPackage {
    private static final String TAG="my_xp_hook";
    private  static final String PACKAGE_NAME="com.bbk.appstore";
    private static   Class<?> cls = null;
/*
    static {
        try{
            cls=Class.forName("com.vivo.libs.b.f.g");

        }catch (Exception e){

        }
    }*/

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if(lpparam.packageName.equals(PACKAGE_NAME)) {
            Log.d(TAG,"hook_Loaded App:"+lpparam.packageName);

          //  HttpHook.initHooking(lpparam);

         //   sockhook.initHooking(lpparam);
         //   httpHook.initHooking(lpparam);

            OkhttpHook.initHooking(lpparam);


            cls=findClass("com.vivo.b.g",lpparam.classLoader);
                    //XposedHelpers.findClass("com.vivo.b.g", lpparam.classLoader);

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
//            XposedHelpers.findAndHookMethod("com.bbk.appstore.util.LogUtility", lpparam.classLoader, "a", String.class,
//                    String.class,
//
//                    new XC_MethodHook() {
//
//
//
//                        @Override
//                        protected void beforeHookedMethod(MethodHookParam param)
//                                throws Throwable {
//                            super.beforeHookedMethod(param);
//                         //   Log.i(TAG,"0:  --- "+param.args[0]);
//                          //  Log.i(TAG,"1:  ---- "+param.args[1]);
//
//                            /*
//                            String temp=(String)param.args[0];
//                            if(temp.contains("AppStore.SearchAssoicationJsonParser")){
//                                MyLog.printStackLog("SearchAssoicationJsonParser");
//                            }
//
//                                String temp2=(String)param.args[1];
//                                if(temp2.contains("GG大玩家")){
//                                     Log.i(TAG,"0:  --- "+param.args[0]);
//                                     Log.i(TAG,"1:  ---- "+param.args[1]);
//
//                                    FileUtils.writeDataToFile(temp2.getBytes(), "test_gg.txt");
//
//                                    MyLog.printStackLog("SearchAssoicationJsonParser");
//                                    Log.d(TAG,"my_length -- "+temp2.length());
//                                    if(temp2.length()>3000){
//                                        MyLog.e(temp2.substring(3000, temp2.length()-1));
//                                    }else {
//                                        MyLog.e(temp2);
//                                    }
//                                }
//*/
//
//                            try{
//                                Class<?> cls = Class.forName("com.bbk.appstore.util.LogUtility"); // 取得Class对象
//                                boolean a=ReflectionUtils.getBooleanValue(cls, "a");
//                                MyLog.e("msg -- "+a);
//                                if(!a){
//                                    a=true;
//                                    ReflectionUtils.setBooleanValue(cls, "a", a);
//                                    MyLog.e("msg -- 重新设值");
//                                }
//                            }catch (Exception e){
//                                MyLog.e(e.getMessage());
//                            }
//
//                        }
//                    });

/*

            XposedHelpers.findAndHookMethod("com.vivo.b.c", lpparam.classLoader, "a", Response.class,
                    cls,

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
*/


            // TODO: 29/03/18
            XposedHelpers.findAndHookMethod("com.vivo.libs.b.d", lpparam.classLoader, "onPostExecute", Object.class,

                    new XC_MethodHook() {

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.beforeHookedMethod(param);
                            Log.d(TAG, "onPostExecute ... ");
                        }
                    });



            //    private void b(String arg17, HashMap arg18, int arg19, boolean arg20)

            XposedHelpers.findAndHookMethod("com.vivo.libs.b.b", lpparam.classLoader, "b", String.class,
                    HashMap.class, int.class, boolean.class,

                    new XC_MethodHook() {

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.beforeHookedMethod(param);
                            Log.d(TAG, "com.vivo.libs.b.b ... "+(String) param.args[0]);

                        }
                    });


            XposedHelpers.findAndHookMethod("com.bbk.appstore.model.b.ar", lpparam.classLoader, "parseData", String.class,

                    new XC_MethodHook() {

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.beforeHookedMethod(param);
                            Log.d(TAG, "parseData.b ... "+(String) param.args[0]);
                            MyLog.printStackLog("parseData ..");
                        }
                    });


            //hoook 第三方jar,hook 不到
     /*       XposedHelpers.findAndHookMethod("okhttp3.OkHttpClient", lpparam.classLoader, "newCall", Request.class,

                    new XC_MethodHook() {

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.beforeHookedMethod(param);
                            Request request=(Request) param.args[0];
                            MyLog.e(TAG,"newCall ..."+request.toString());
                            MyLog.printStackLog("newCall ..");
                        }
                    });

*/


 /*           XposedHelpers.findAndHookMethod("com.vivo.b.c", lpparam.classLoader, "c", cls.getComponentType(),

                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            MyLog.e("request .. 222");
                        }



                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            MyLog.e("request .. 222");
                            Request request=(Request) param.getResult();
                            MyLog.e(TAG,"request ..."+request.toString());
                            MyLog.printStackLog("request .. 111");
                        }


                    });*/

        }else{
         //   Log.d(TAG,"no hook_Loaded App:"+lpparam.packageName);
        }
    }
}
