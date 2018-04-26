package com.mx.sdk.nativeutils;

import android.content.Context;

public class NativeUtils {
    public static native int  nativeInit(Context context);  //初始化相关数据

    public static native String nativeE(int method, String data);  //加密方法
    public static native String nativeD(int method, String data);  //解密方法

}
