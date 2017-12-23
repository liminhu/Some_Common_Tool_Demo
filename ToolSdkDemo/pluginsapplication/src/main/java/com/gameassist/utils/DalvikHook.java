package com.gameassist.utils;

import com.gameassist.plugin.nativeutils.NativeUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class DalvikHook {
    public static final class DalvikHookHelper {
        private static final ConcurrentHashMap<String , Integer> sBackups = new ConcurrentHashMap<>();

        public static void hook(final Method origin, final Method replace, String key) {
            //原类名+原函数名+方法签名 组成key
            final Integer slot = NativeUtils.dalvikHook(origin, replace);
            sBackups.put(key, slot);
        }

        private static int getBackupSlot(String key) {
            return sBackups.get(key);
        }
        public static Object callOrigin(String key,Method targetMethod,Object thiz,Object... args){
            int slot = getBackupSlot(key);
            String returnType = targetMethod.getReturnType().getName();
            if(returnType.equals("int")){
               return NativeUtils.invokeIntMethod(slot,thiz,args);
            }else if(returnType.equals("String")){
               return NativeUtils.invokeObjectMethod(slot,thiz,args);
            }else if(returnType.equals("float")){
              return   NativeUtils.invokeFloatMethod(slot,thiz,args);
            }else if(returnType.equals("void")){
                NativeUtils.invokeVoidMethod(slot,thiz,args);
                return null;
            }else{
                return   NativeUtils.invokeObjectMethod(slot,thiz,args);
            }

        }

        public static Method findMethod(final Class<?> cls, final String name,
                                        final Class<?>... parameterTypes) {
            try {
                return cls.getDeclaredMethod(name, parameterTypes);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static void invokeVoidMethod(final int slot, final Object receiver,
                                        final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            NativeUtils.invokeVoidMethod(slot, receiver, params);
        } else {
//            AndHook.ArtHook.invokeMethod(slot, receiver, params);
        }
    }

    public static void invokeStaticVoidMethod(final int slot,
                                              final Class<?> clazz, final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            NativeUtils.invokeVoidMethod(slot, clazz, params);
        } else {
//            AndHook.ArtHook.invokeMethod(slot, null, params);
        }
    }

    public static boolean invokeBooleanMethod(final int slot,
                                              final Object receiver, final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeBooleanMethod(slot, receiver, params);
        } else {
//            return (boolean) AndHook.ArtHook.invokeMethod(slot, receiver, params);
            return false;
        }
    }

    public static boolean invokeStaticBooleanMethod(final int slot,
                                                    final Class<?> clazz, final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeBooleanMethod(slot, clazz, params);
        } else {
//            return (boolean) AndHook.ArtHook.invokeMethod(slot, null, params);
            return false;
        }
    }

    public static byte invokeByteMethod(final int slot, final Object receiver,
                                        final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeByteMethod(slot, receiver, params);
        } else {
//            return (byte) AndHook.ArtHook.invokeMethod(slot, receiver, params);
            return 1;
        }
    }

    public static byte invokeStaticByteMethod(final int slot, Class<?> clazz,
                                              final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeByteMethod(slot, clazz, params);
        } else {
//            return (byte) AndHook.ArtHook.invokeMethod(slot, null, params);
            return 1;
        }
    }

    public static short invokeShortMethod(final int slot,
                                          final Object receiver, final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeShortMethod(slot, receiver, params);
        } else {
//            return (short) AndHook.ArtHook.invokeMethod(slot, receiver, params);
            return 1;
        }
    }

    public static short invokeStaticShortMethod(final int slot,
                                                final Class<?> clazz, final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeShortMethod(slot, clazz, params);
        } else {
//            return (short) AndHook.ArtHook.invokeMethod(slot, null, params);
            return 1;
        }
    }

    public static char invokeCharMethod(final int slot, final Object receiver,
                                        final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeCharMethod(slot, receiver, params);
        } else {
//            return (char) AndHook.ArtHook.invokeMethod(slot, receiver, params);
            return 1;
        }
    }

    public static char invokeStaticCharMethod(final int slot,
                                              final Class<?> clazz, final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeCharMethod(slot, clazz, params);
        } else {
//            return (char) AndHook.ArtHook.invokeMethod(slot, null, params);
            return 1;
        }
    }

    public static int invokeIntMethod(final int slot, final Object receiver,
                                      final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeIntMethod(slot, receiver, params);
        } else {
//            return (int) AndHook.ArtHook.invokeMethod(slot, receiver, params);
            return 1;
        }
    }

    public static int invokeStaticIntMethod(final int slot,
                                            final Class<?> clazz, final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeIntMethod(slot, clazz, params);
        } else {
//            return (int) AndHook.ArtHook.invokeMethod(slot, null, params);
            return 1;
        }
    }

    public static long invokeLongMethod(final int slot, final Object receiver,
                                        final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeLongMethod(slot, receiver, params);
        } else {
//            return (long) AndHook.ArtHook.invokeMethod(slot, receiver, params);
            return 1;
        }
    }

    public static long invokeStaticLongMethod(final int slot,
                                              final Class<?> clazz, final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeLongMethod(slot, clazz, params);
        } else {
//            return (long) AndHook.ArtHook.invokeMethod(slot, null, params);
            return 1;
        }
    }

    public static float invokeFloatMethod(final int slot,
                                          final Object receiver, final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeFloatMethod(slot, receiver, params);
        } else {
//            return (float) AndHook.ArtHook.invokeMethod(slot, receiver, params);
            return 1;
        }
    }

    public static float invokeStaticFloatMethod(final int slot,
                                                final Class<?> clazz, final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeFloatMethod(slot, clazz, params);
        } else {
//            return (float) AndHook.ArtHook.invokeMethod(slot, null, params);
            return 1;
        }
    }

    public static double invokeDoubleMethod(final int slot,
                                            final Object receiver, final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeDoubleMethod(slot, receiver, params);
        } else {
//            return (double) AndHook.ArtHook.invokeMethod(slot, receiver, params);
            return 1;
        }
    }

    public static double invokeStaticDoubleMethod(final int slot,
                                                  final Class<?> clazz, final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeDoubleMethod(slot, clazz, params);
        } else {
//            return (double) AndHook.ArtHook.invokeMethod(slot, null, params);
            return 1;
        }
    }

    public static Object invokeObjectMethod(final int slot,
                                            final Object receiver, final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeObjectMethod(slot, receiver, params);
        } else {
//            return AndHook.ArtHook.invokeMethod(slot, receiver, params);
            return 1;
        }
    }

    public static Object invokeStaticObjectMethod(final int slot,
                                                  final Class<?> clazz, final Object... params) {
        if (android.os.Build.VERSION.SDK_INT <= 19) {
            return NativeUtils.invokeObjectMethod(slot, clazz, params);
        } else {
//            return AndHook.ArtHook.invokeMethod(slot, null, params);
            return null;
        }
    }


}
