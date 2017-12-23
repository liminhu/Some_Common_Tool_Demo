package com.gameassist.plugin.nativeutils;

import java.lang.reflect.Method;

/**
 * Created by hulimin on 2017/12/23.
 */

public class NativeUtils {
    public static native void nativeDoCheat(int flag, int arg1, int arg2);
    public static native void nativeFindAndBackupAndHook(Class targetClass, String methodName, String methodSig,
                                                         boolean isStatic , Method hook, Method backup);
    public static native void nativeinitHook(int SDK_version);


    public static native void invokeVoidMethod(final int slot,
                                               final Object receiver, final Object... params);

    public static native boolean invokeBooleanMethod(final int slot,
                                                     final Object receiver, final Object... params);

    public static native byte invokeByteMethod(final int slot,
                                               final Object receiver, final Object... params);

    public static native short invokeShortMethod(final int slot,
                                                 final Object receiver, final Object... params);

    public static native char invokeCharMethod(final int slot,
                                               final Object receiver, final Object... params);

    public static native int invokeIntMethod(final int slot,
                                             final Object receiver, final Object... params);

    public static native long invokeLongMethod(final int slot,
                                               final Object receiver, final Object... params);

    public static native float invokeFloatMethod(final int slot,
                                                 final Object receiver, final Object... params);

    public static native double invokeDoubleMethod(final int slot,
                                                   final Object receiver, final Object... params);

    public static native Object invokeObjectMethod(final int slot,
                                                   final Object receiver, final Object... params);

    public static native int dalvikHook(final Method origin, final Method replace);

    public static native void dalvikHookNoBackup(final Method origin,
                                                 final Method replace);
}
