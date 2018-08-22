package com.sdk.hlm.tool.hook;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;


import com.sdk.hlm.tool.utils.MyLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ServiceManagerWraper {
    public static void hookPMS(Context context, String signed, String appPkgName, int hashCode){
        try{
            // 获取全局的ActivityThread对象
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod =
                    activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);
            // 获取ActivityThread里面原始的sPackageManager
            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            Object sPackageManager = sPackageManagerField.get(currentActivityThread);
            // 准备好代理对象, 用来替换原始的对象
            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            Object proxy = Proxy.newProxyInstance(
                    iPackageManagerInterface.getClassLoader(),
                    new Class<?>[] { iPackageManagerInterface },
                    new PmsHookBinderInvocationHandler(sPackageManager, signed, appPkgName, 0));
            // 1. 替换掉ActivityThread里面的 sPackageManager 字段
            sPackageManagerField.set(currentActivityThread, proxy);
            // 2. 替换 ApplicationPackageManager里面的 mPM对象
            PackageManager pm = context.getPackageManager();
            Field mPmField = pm.getClass().getDeclaredField("mPM");
            mPmField.setAccessible(true);
            mPmField.set(pm, proxy);
            MyLog.e("hook pms finish ...");

        }catch (Exception e){
            Log.d("jw", "hook pms error:"+ Log.getStackTraceString(e));
        }
    }




    public static void hookPMS(Context context){
        String qqSign = "308203833082026ba00302010202042ccf4795300d06092a864886f70d01010b050030723111300f0603550406130874657374746573743111300f0603550408130874657374746573743111300f0603550407130874657374746573743111300f060355040a130874657374746573743111300f060355040b130874657374746573743111300f060355040313087465737474657374301e170d3136303830323034333532335a170d3431303732373034333532335a30723111300f0603550406130874657374746573743111300f0603550408130874657374746573743111300f0603550407130874657374746573743111300f060355040a130874657374746573743111300f060355040b130874657374746573743111300f06035504031308746573747465737430820122300d06092a864886f70d01010105000382010f003082010a02820101008c2b993579677d0059dce3d8a5ed9a76a54f8b4920bc37216489102bb549610a2fe23a115d516a575d9fdb0d67c5fc31f4e202155e7d8005cfcbe94d1358e27fe895a8502c8cbe14227aaa714c7b985d3ab9800c7b3de600fb915a01671dcb78dc4b1ea41393a4b295169ee2b08704db72ed6422f0d096ec78507fcdad863a087d0afea4018b2d3127cb0f8690087a95c7264c00976cafdd25936dc160925969df120ef226fb71808f643634b8febd363d8baafd537d1eca594fb7220c7957691ce52f279601c47d5283b5895c30a45bb2d8cc1e5a2a7307dcf44532e650d739a390820947ed7cab43b33155649abdfa0e301f7852a34ae8210603c54d1947fb0203010001a321301f301d0603551d0e04160414650fd4d094f644db46d31ac6b1576f58b7c50af0300d06092a864886f70d01010b05000382010100293c8b1d3c24af248f8e288b3a8724367bafb54761a21525853025d9f1eb9b99f6724dadb4334fe7ec38dcc334e86acb9e83f71f80a2b2206aa0ac6038542f902a697dc18c21fb4e086e547e878bbe366d1183b49b8e0574628016371dc4e87f6be5d6f9558003898249e93f980ed72367702400e99413bf088dfca76f5866a4c97818441651a5322eed10c1ad093031ce0016c5ef93dcfb5d89cfa90c4d6f48637a70c0e0e8ca325ef1a31176fac7fb800144bba64680b1fe8bc57a720e7769f9c4c95026f90b9239f3b32e949094c85f641a4b6a91efe71be73238f630fdca070464d7d6360e5eef35610052adc59cc5abde00d3c64e11af1583de682f0d21";
        hookPMS(context, qqSign, "com.test.hlm.tool", 0);
    }
}
