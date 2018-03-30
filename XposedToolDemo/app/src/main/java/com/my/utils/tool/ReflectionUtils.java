package com.my.utils.tool;


import android.text.TextUtils;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

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


    // TODO: 30/03/18 有问题
    public static Object callMethod(Object instance, String methodName){
        try{
            Method method = instance.getClass().getMethod(methodName);
           // MyLog.e("method -- " + method.toString());
          //  method.setAccessible(true);
            return method.invoke(instance);
        }catch (Exception e){

        }
        return null;
    }




    public static Object callMethod(Object instance, String methodName, Class[] paramsTypes, Object[] params){
        try{
            Method method = instance.getClass().getMethod(methodName, paramsTypes);
           // MyLog.e("method -- " + method.toString());
          //  method.setAccessible(true);
            return method.invoke(instance, params);
        }catch (Exception e){

        }
        return null;
    }



    // TODO: 2017/10/12  测试得到类中所有的字符串
    public static void   getAllFields(Object model){
        Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        try {
            MyLog.e("leng: "+field.length);
            for (int j = 0; j < field.length; j++) { // 遍历所有属性
                String name = field[j].getName(); // 获取属性的名字
                MyLog.e("name: "+name);
                String type = field[j].getGenericType().toString(); // 获取属性的类型
                MyLog.e("j:%d, name:%s,   ----- type: "+type, j, name);
                if(type.contains("HashMap")){
                    //printHashMap((HashMap) getValue(model,name));
                }else if (type.contains("java.lang.String") && !type.contains("Map")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    String data=(String)getValue(model,name);
                    if(TextUtils.isEmpty(data)){
                        data=" is null";
                    }
                    MyLog.e("data -------- "+data);
                }else if(type.equals("int")){
                    Integer data=(Integer)getValue(model,name);
                    MyLog.e("data int -------- "+data);
                }else  if(type.contains("android.widget.TextView")){
                    TextView data=(TextView)getValue(model,name);
                    MyLog.e("data TextView -------- "+data.getText());
                }
            }
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
    }





    // TODO: 2017/10/12  测试得到类中所有的字符串
    public static void   getAllMethod(Object model){
        Method[] methods = model.getClass().getMethods(); // 获取实体类的所有属性，返回Field数组
        try {
            MyLog.e("leng: "+methods.length);
            for (int j = 0; j < methods.length; j++) { // 遍历所有属性
                String name = methods[j].getName(); // 获取属性的名字
                String a=methods[j].toGenericString();
                MyLog.e("name: "+name);     //获得完整的方法信息（包括修饰符、返回值、路径、名称、参数、抛出值）
                MyLog.e("获得完整的方法信息: "+a);
            }
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
    }


}
