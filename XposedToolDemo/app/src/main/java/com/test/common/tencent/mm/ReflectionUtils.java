package com.test.common.tencent.mm;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.utils.tool.MyLog;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

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




    // TODO: 2017/10/12  测试得到类中所有的字符串
    public static void   getAllFields(Object model, int leven){
        if(leven<0){
            return;
        }
        Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        try {
            MyLog.d("leven--- %d, %s ,  leng: "+field.length, leven, model.getClass().getName());
            for (int j = 0; j < field.length; j++) { // 遍历所有属性
                String name = field[j].getName(); // 获取属性的名字
                 MyLog.e("name: "+name);
                String type = field[j].getGenericType().toString(); // 获取属性的类型
                MyLog.e("j:%d, name:%s,   ----- type: "+type, j, name);
                if(type.contains("HashMap")){
                  //  TestUtils.printHashMap((HashMap) getValue(model,name));
                }else if (type.contains("java.lang.String") && !type.contains("Map")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    String data=(String)getValue(model,name);
                    if(TextUtils.isEmpty(data)){
                        data=" is null";
                    }
                    MyLog.d("data -------- leven:%d,  "+data, leven);
                }else if(type.equals("int")){
                    Integer data=(Integer)getValue(model,name);
                    MyLog.e("data int -------- "+data);
                }else if(type.contains("class [Ljava.")){
                    Object data = getValue(model, name);
                    if(data!=null){
                        int jj=Arrays.asList(data).size();
                        MyLog.e("---- size --- "+j);
                        for(int k=0; k<jj; k++){
                          getAllFields(Arrays.asList(data).get(k),2);
                        }
                    }else{
                        MyLog.e("---- size --- "+name+" is null ");
                    }
                }
                else if(type.contains("java.util.List")){
                    Object data = (Object) getValue(model, name);
                    if(data!=null){
                        MyLog.e("---- size --- "+((List)data).size());
                        for(int i=0; i<((List)data).size(); i++) {
                            getAllFields(((List)data).get(i), 2);
                        }
                    }else{
                        MyLog.e("---- size --- "+name+" is null ");
                    }
                }else  if(type.contains("android.widget.TextView")) {
                    TextView data = (TextView) getValue(model, name);
                    MyLog.e("data TextView -------- " + data.getText());
                }else if(type.contains("DisLikeAwemeLayout")){
                    Object data=(Object)getValue(model,name);
                    ViewGroup vg = (ViewGroup) data;
                    // ReflectionUtils.printfView(vg, 43);
                    printfView(vg,3);
                }else  if(!type.contains("java.util.List") && type.contains("com.ss.android.ugc.aweme")){
                    Object data=(Object)getValue(model,name);
                    getAllFields(data, leven-1);
                }else if(type.contains("SparseIntArray") || type.contains("android.app.Activity")){
                    Object data=(Object)getValue(model,name);
                    getAllFields(data, 2);
                }else if(type.contains("android.view.ViewGroup")) {
                    Object data =  getValue(model, name);
                    ViewGroup vg = (ViewGroup) data;
                    // ReflectionUtils.printfView(vg, 43);
                    printfView((ViewGroup)vg.getParent());
                }else {
                    MyLog.e(leven +"----- class j:%d, name:%s,   ----- type: "+type, j, name);
                }
            }
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
    }




    public static ViewGroup getTopViewGroupFromActivity(Activity activity){
        View topView = activity.getWindow().getDecorView();
        final ViewGroup topViewGp = (ViewGroup) topView;
        return  topViewGp;
    }


    static int num=0;
    private static  void printfView(ViewGroup vg){
        try{
            if(vg.getChildCount() == 0){
                return;
            }else{
                for(int i=0; i<vg.getChildCount(); i++){
                    String name=vg.getChildAt(i).getClass().getName();
                    MyLog.e("%x  ----hook_"+num+"\tname:"+name, vg.getChildAt(i).getId());
                    if(name.contains("android.widget.Button")) {
                        Button button=(Button) vg.getChildAt(i);
                        MyLog.e("hook_Button:"+num+":"+"\tid:"+button.getId()+"\t"+button.getText().toString());
                        num++;
                    }else if(name.contains("TextView")){
                        TextView button=(TextView) vg.getChildAt(i);
                        MyLog.e("hook_TextView"+num+":"+button.getText().toString());
                        num++;
                    }
                    if (vg.getChildAt(i) instanceof ViewGroup) {
                        num++;
                        printfView((ViewGroup) vg.getChildAt(i));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }




   static  boolean isFind=false;

    public static  void printfView(ViewGroup vg, int level){
        try{
            if(vg.getChildCount() == 0 || level<0){
                return;
            }else{
                for(int i=0; i<vg.getChildCount(); i++){
                    String name=vg.getChildAt(i).getClass().getName();
                    MyLog.e("id：%d,  level--%d, hook_"+num+"\tname:"+name, vg.getChildAt(i).getId(), level);
                    if(name.contains("android.widget.Button")) {
                        Button button=(Button) vg.getChildAt(i);
                         MyLog.e("hook_Button:"+num+":"+"\tid:"+button.getId()+"\t"+button.getText().toString());
                        num++;
                    }else if(name.contains("TextView")){
                        TextView button=(TextView) vg.getChildAt(i);
                        MyLog.e("hook_TextView"+num+":"+button.getText().toString());
                        if(button.getText().toString().contains("分享")){
                            MyLog.e("id:%d --- 0x%x,parent -- id:%d, TextView --- "+num+":"+button.getText().toString(), button.getId(), button.getId(),((View)button.getParent()).getId());
                            //return;
                        }
                    }else if(name.contains("TenpaySecureEditText")){
                        EditText ed=(EditText) vg.getChildAt(i);
                        MyLog.e("hook_EditText"+level+":"+ed.getText().toString());
                    }else if(name.contains("AppCompatImageView")){
                        final ImageView ed=(ImageView) vg.getChildAt(i);
                        MyLog.e("hook_ImageView ---- "+level+":"+ed.getId());

                    /*    if(Integer.valueOf(ed.getId())== 2131821755){
                            if(isFind==false){
                                isFind=true;
                                ClickEventUtils.touchClick(ed);
                            }

                         //   ViewGroup vg1= ReflectionUtils.getTopViewGroupFromActivity(PluginEntry.currentActivity);
                           // ReflectionUtils.printfView(vg1, 1);
                            return;
                        }*/

                        if(ed!=null && Integer.valueOf(ed.getId())>0){

                            ed.post(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap tm=loadBitmapFromView(ed);
                                    if(tm!=null){
                                        saveBitmap(tm, "image_"+ed.getId());
                                    }
                                }
                            });
                        }

                    }
                    if (vg.getChildAt(i) instanceof ViewGroup && level>0) {
                        printfView((ViewGroup) vg.getChildAt(i), level-1);
                    }else{
                        MyLog.e("111_hook_"+num+"\tname:"+name);
                      /*  if(name.contains("com.ss.android.ugc.aweme")){
                            Object data=(Object)getValue(vg.getChildAt(i).getClass(),name);
                            getAllFields(vg.getChildAt(i).getClass());
                        }*/
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }




    private static  Bitmap loadBitmapFromView(View v) {
        if(v==null){
            return null;
        }
        int w = v.getWidth();
        int h = v.getHeight();

        if(w<1 || h<1){
            return null;
        }

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

       // c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }


    private static  void saveBitmap(Bitmap bm, String name) {
        MyLog.e("view--  saveBitmap:"+name);
        File f = new File(Environment.getExternalStorageDirectory(), "/aaa/"+name+".png");
        //  File f=new File(name);
        MyLog.e("view--  name:"+f.getAbsolutePath());

        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            MyLog.e("view--  saveBitmap: succ  "+name);
        } catch (Exception e) {
            MyLog.e("view--  1111: file "+       e.getMessage());

        }

    }




}
