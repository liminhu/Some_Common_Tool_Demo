package com.gameassist.utils;

import com.gameassist.plugin.nativeutils.NativeUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import demo.utils.MyLog;


public class HookHelp {
    public static ArrayList<String> hookList = new ArrayList<>();
    public static void doHookInit(ClassLoader originClassLoader) {
        try {
            for (String hook_name : hookList) {
                doHookItemInit(hook_name, originClassLoader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doHookItemInit(String hookClassName, ClassLoader originClassLoader) {
        try {
            MyLog.e("is hook doHookItemInit ... "+hookClassName);
            Class<?> hookItem = Class.forName("com.gameassist.plugin.hook." + hookClassName);

            String className = (String) hookItem.getField("className").get(null);
            String methodName = (String) hookItem.getField("methodName").get(null);
            String methodSig = (String) hookItem.getField("methodSig").get(null);
            String key = className+methodName+methodSig;
            Method target_method = (Method) hookItem.getField("targetMethod").get(null);
            String mod =  Modifier.toString(target_method.getModifiers());
            if(mod!=null){
                MyLog.e(mod+"--- is not null ");
            }else{
                MyLog.e("Modifier is  null ");
            }
            boolean isStatic = false;
            if(mod.contains("static")) {
                isStatic = true;
            }

            if (className == null || className.equals("")) {
                MyLog.e("doHookItemInit   is not null  ... ");
                return;
            }
            Class<?> clazz = Class.forName(className, true, originClassLoader);
            if (Modifier.isAbstract(clazz.getModifiers())) {
            }
            MyLog.e("is hook java ... ");
            Method hook = null;
            Method backup = null;
            for (Method method : hookItem.getDeclaredMethods()) {
                if (method.getName().equals("hook")) {
                    hook = method;
                } else if (method.getName().equals("origin")) {
                    backup = method;
                }
            }
            if (hook == null) {
                return;
            }

            if (android.os.Build.VERSION.SDK_INT <= 19) {
                DalvikHook.DalvikHookHelper.hook(target_method,hook,key);
            }else {
                MyLog.e("is hook java ... 1111");
                NativeUtils.nativeFindAndBackupAndHook(clazz, methodName, methodSig, isStatic, hook, backup);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  static class HookMethod{
        public static String className = null;
        public static String methodName = null;
        public static String methodSig = null;
        public static Method targetMethod = null;

        public static Method getMethod(String name, Class<?>... parameterTypes){
            try {
                targetMethod = Class.forName(className).getDeclaredMethod(name,parameterTypes);
                return targetMethod;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            MyLog.e(name+"异常了。。。");
            return null;
        }
        public static Object originInvoke(Object _this, Object... args) {
            if (android.os.Build.VERSION.SDK_INT <= 19) {
                Object ret = DalvikHook.DalvikHookHelper.callOrigin(className + methodName + methodSig, targetMethod, _this, args);
                return ret;
            }else{
                return null;
            }

        }
    }

}
