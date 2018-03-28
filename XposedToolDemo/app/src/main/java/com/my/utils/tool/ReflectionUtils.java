package com.my.utils.tool;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by hulimin on 2017/9/14.
 */

public class ReflectionUtils {

    /***
       * 获取私有成员变量的值
     *
     */
    public static Object getValue(Object instance, String fieldName){
        try{
            Field field = instance.getClass().getDeclaredField(fieldName);
           // MyLog.e("field -- " + field.toString());
            field.setAccessible(true); // 参数值为true，禁止访问控制检查
            Object result=field.get(instance);
          //  MyLog.e("getValue --- "+result.toString());
            return  result;
        }catch (Exception e){
            MyLog.e("getValue --- "+e.getMessage());
        }
        return null;
    }





    public static boolean getBooleanValue(Object instance, String fieldName){
        try{
            Field field = instance.getClass().getDeclaredField(fieldName);
            // MyLog.e("field -- " + field.toString());
            field.setAccessible(true); // 参数值为true，禁止访问控制检查
            boolean result=field.getBoolean(instance);
            //  MyLog.e("getValue --- "+result.toString());
            return  result;
        }catch (Exception e){
            MyLog.e("getValue --- "+e.getMessage());
        }
        return false;
    }



    public static void setBooleanValue(Object instance, String fieldName, boolean value){
        try{
            Field field = instance.getClass().getDeclaredField(fieldName);
            // MyLog.e("field -- " + field.toString());
            field.setAccessible(true); // 参数值为true，禁止访问控制检查
            field.setBoolean(instance, value);
            //  MyLog.e("getValue --- "+result.toString());
        }catch (Exception e){
            MyLog.e("setValue --- "+e.getMessage());
        }
    }




    public static void setValue(Object instance, String fieldName, String value){
        try{
            Field field = instance.getClass().getDeclaredField(fieldName);
            // MyLog.e("field -- " + field.toString());
            field.setAccessible(true); // 参数值为true，禁止访问控制检查
            field.set(instance, value);
            //  MyLog.e("getValue --- "+result.toString());
        }catch (Exception e){
            MyLog.e("setValue --- "+e.getMessage());
        }
    }




    public static Object callMethod(Object instance, String methodName){
        try{
            Method method = instance.getClass().getDeclaredMethod(methodName);
           // MyLog.e("method -- " + method.toString());
            method.setAccessible(true);
            return method.invoke(instance);
        }catch (Exception e){

        }
        return null;
    }




    public static Object callMethod(Object instance, String methodName, Class[] paramsTypes, Object[] params){
        try{
            Method method = instance.getClass().getDeclaredMethod(methodName, paramsTypes);
           // MyLog.e("method -- " + method.toString());
            method.setAccessible(true);
            return method.invoke(instance, params);
        }catch (Exception e){

        }
        return null;
    }

}
