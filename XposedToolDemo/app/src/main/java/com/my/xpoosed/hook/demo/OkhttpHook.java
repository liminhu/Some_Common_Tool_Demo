package com.my.xpoosed.hook.demo;

import android.util.Log;

import com.my.utils.tool.MyLog;
import com.my.utils.tool.ReflectionUtils;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import okhttp3.Request;
import okhttp3.Response;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;


/**
 * Created by Athos on 4/21/2016.
 */
public class OkhttpHook {
    private static String TAG="my_OkhttpHook";

    public static void initHooking(XC_LoadPackage.LoadPackageParam lpparam) throws NoSuchMethodException {

        final Class   cls=findClass("com.vivo.b.g",lpparam.classLoader);
        final Class   responseCls=findClass("okhttp3.Response",lpparam.classLoader);



        findAndHookMethod("com.vivo.b.c", lpparam.classLoader, "c", cls, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e(TAG, "111l.... ");
                MyLog.printStackLog("... 1111 ");
                //MyLog.printStackLog("request .. 111");
               // ReflectionUtils.getAllFields(param.args[0]);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
              //  super.afterHookedMethod(param);
               // MyLog.e("request .. 222");
                try{
                    MyLog.e("param: "+param.getResult().getClass().getName());
                   // Request request=(Request) param.getResult().toString();
                    Object o =param.getResult();
                    if( param.getResult()!=null){
                      //  MyLog.e("request .. 333");
                        MyLog.e(TAG+"request ..."+param.getResult().toString());
                                //((Request) param.getResult()).body().toString());
                        if(param.getResult().toString().contains("https://main.appstore.vivo.com.cn/rec/newapps")){
                            MyLog.printStackLog("info.appstore.vivo.com.cn/ ");
                        }
                       // MyLog.printStackLog("request .. 111");
                    }else{
                        MyLog.e("request .. 444");
                    }
                }catch (Exception e){
                    MyLog.e(" exception ... "+e.getMessage());
                }


            }
        });









        findAndHookMethod("com.vivo.b.c", lpparam.classLoader, "a", responseCls, cls, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e(TAG, "Response 111l.... ");
                Log.e(TAG, param.args[0].toString());
                MyLog.e(param.args[0].toString());

             //   ReflectionUtils.getAllMethod(cls);



                try{
                  //  ReflectionUtils.getAllFields(param.args[0]);
                 //   Response
                    Object body=ReflectionUtils.getValue(param.args[0], "body");
//                  //  ReflectionUtils.getAllMethod(body);
//                   // okhttp3.internal.http.RealResponseBody
//                    Log.e(TAG, "body -- "+body.toString());
//                    Method method = body.getClass().getMethod("string");
//                    String data0=(String)method.invoke(body);
//                    // Java 反射机制 - 调用某个类的方法1.
//                    // 调用Person的Speak方法
//                    Log.e(TAG, "body -- "+data0);
//
//


                     String data1=(String)ReflectionUtils.callMethod(body, "toString");
                    Log.e(TAG, "body -- "+data1);
/*

                    String data=(String)ReflectionUtils.callMethod(body, "string");
                    Log.e(TAG, "body -- "+data);
*/


                }catch (Exception e){
                    Log.e(TAG, "Response 111l.... "+e.getMessage());
                }


                //MyLog.printStackLog("request .. 111");
                // ReflectionUtils.getAllFields(param.args[0]);
            }
        });






    }
}
