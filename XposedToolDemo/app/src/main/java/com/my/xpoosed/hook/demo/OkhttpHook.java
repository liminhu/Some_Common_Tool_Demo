package com.my.xpoosed.hook.demo;

import android.util.Log;

import com.my.utils.tool.MyLog;
import com.my.utils.tool.ReflectionUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import okhttp3.Request;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;


/**
 * Created by Athos on 4/21/2016.
 */
public class OkhttpHook {
    private static String TAG="my_OkhttpHook";

    public static void initHooking(XC_LoadPackage.LoadPackageParam lpparam) throws NoSuchMethodException {

        final Class   cls=findClass("com.vivo.b.g",lpparam.classLoader);



        findAndHookMethod("com.vivo.b.c", lpparam.classLoader, "c", cls, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e(TAG, "111l.... ");
                MyLog.printStackLog("request .. 111");
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
                       // MyLog.printStackLog("request .. 111");
                    }else{
                        MyLog.e("request .. 444");
                    }
                }catch (Exception e){
                    MyLog.e(" exception ... "+e.getMessage());
                }


            }
        });


    }
}
