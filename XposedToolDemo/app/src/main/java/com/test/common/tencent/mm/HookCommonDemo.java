package com.test.common.tencent.mm;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.RelativeDateTimeFormatter;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.my.utils.tool.MyLog;
import com.my.xpoosed.hook.demo.OkhttpHook;
import com.my.xposedhook.hooks.httpHook;
import com.my.xposedhook.hooks.sockhook;

import org.json.JSONObject;

import java.io.File;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookCommonDemo implements IXposedHookLoadPackage {
    private static final String TAG="my_xp_hook";
    private  static final String PACKAGE_NAME="com.ss.android.ugc.aweme"; //"com.mx.applicationmarket.vivo";
    private static   Class<?> cls = null;

  //  private     ImageView.setImageUri(Uri.fromFile(new File("/sdcard/test.jpg")));
    private static boolean isHaveAdd=false;

    static boolean  flag=false;


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals(PACKAGE_NAME)) {
            Log.d(TAG, "hook_Loaded App:" + lpparam.packageName);



            //  sockhook.initHooking(lpparam);
            //  httpHook.initHooking(lpparam);
            //  OkhttpHook.initHooking(lpparam);

 /*
            XposedHelpers.findAndHookMethod("java.lang.StringBuilder", lpparam.classLoader, "toString",
                  new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {

                            MyLog.e("1111 ");
                            super.afterHookedMethod(param);
                            String resultString=(String)param.getResult();
                            if(resultString!=null)
                            Log.d(TAG,"my_hook_string_build ---  "+resultString);
                        }
                    });*/



/*

            XposedHelpers.findAndHookMethod("org.json.JSONObject", lpparam.classLoader, "toString",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            String resultString = (String) param.getResult();
                            Log.d(TAG, "hook_JSONObject  ---- " + resultString);
                            if(resultString.startsWith("{\"p1\":\"")){
                                MyLog.printStackLog("--- json ");
                            }
                        }
                    });
*/


/*
            View view;
            view.performClick()

                    */

/*

            XposedHelpers.findAndHookMethod("android.view.View", lpparam.classLoader, "performClick",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            Log.d(TAG, "hook_JSONObject  ---- View ");
                            MyLog.e(param.thisObject.getClass().getName()+" ----- ");
                            if(param.thisObject!=null){
                              //  MyLog.printStackLog("View");

                                if(null!=param.thisObject && param.thisObject.getClass().getName().contains("ImageView")){
                                   // MyLog.e("activity -- "+param.thisObject.getClass().getName());
                                  //  ReflectionUtils.getAllFields(param.thisObject,30);
                                    ImageView iv=(ImageView)param.thisObject;
                                    ViewGroup vg= (ViewGroup)iv.getParent().getParent();
                                    ReflectionUtils.getAllFields((Object) vg,30);

                                   // ReflectionUtils.printfView(vg, 33);
                                }
                            }
                        }
                    });
*/




      XposedHelpers.findAndHookMethod("android.app.Dialog", lpparam.classLoader, "show",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            Log.d(TAG, "hook_JSONObject  ---- dialog ");
                            MyLog.printStackLog("dialog");
                            MyLog.e(param.thisObject.getClass().getName()+" ----- ");

                            if(flag==true){
                                MyLog.e(" flag is true");
                                //ReflectionUtils.getAllFields(param.thisObject, 1);
                                Dialog dialog=(Dialog) param.thisObject;
                                final ViewGroup vg = (ViewGroup) dialog.getWindow().getDecorView();
                                ReflectionUtils.printfView(vg);
                                flag=false;
                            }



                            Dialog dialog=(Dialog) param.thisObject;
                           if (null != dialog && dialog.getClass().getName().contains("android.support.design.widget.c")) {
                                Log.e(TAG, "dialog  class  -- " + dialog.getClass().getName());
                                final ViewGroup vg = (ViewGroup) dialog.getWindow().getDecorView();

                               // ReflectionUtils.getAllFields(dialog, 7);
                            //   ReflectionUtils.printfView(vg, 40);
                               String et="com.ss.android.ugc.aweme.shortvideo.view.MentionEditText";
                               final View edit=getViewByRealTextName(vg, et,"", 3);

                               if(edit!=null) {
                                   MyLog.e("msg ... "+((EditText)edit).getText().toString());
                                   View pid=(View)edit.getParent();
                                   View ppid=(View)edit.getParent().getParent();
                                   MyLog.d("textId:%d, pid:%d, ppid:%d", edit.getId(),pid.getId(), ppid.getId());
                                   MyLog.d("name: %s, pname:%s, ppname:%s", edit.getClass().getName(), pid.getClass().getName(),
                                   ppid.getClass().getName());
                                   ((EditText)edit).setText("1111 ... ");


                                   ReflectionUtils.printfView((ViewGroup)pid, 1);



                                   edit.post(new Runnable() {
                                       @Override
                                       public void run() {
                                           Bitmap tm=ReflectionUtils.loadBitmapFromView(edit);
                                           if(tm!=null){
                                               ReflectionUtils.saveBitmap(tm, "image_"+edit.getId());
                                           }
                                       }
                                   });


                                   flag=true;
                               }else {
                                   MyLog.d(" edit is null ... ");
                               }
                           }else if(null!=param.thisObject && param.thisObject.getClass().getName().equals("com.douyin.share.a.c.c")){
                                MyLog.e("activity -- "+param.thisObject.getClass().getName());
                                ReflectionUtils.getAllFields(param.thisObject,0);
                                MyLog.e("getOwnerActivity .. "+dialog.getOwnerActivity().getClass().getName());
                                final ViewGroup vg= (ViewGroup)dialog.getWindow().getDecorView();
                                //ReflectionUtils.printfView(vg, 33);

                                String type="android.support.v7.widget.AppCompatTextView";
                                View text=getViewByRealTextName(vg,type,"复制链接", 1);
                                if(text!=null) {
                                    View pid=(View)text.getParent();
                                    View ppid=(View)text.getParent().getParent();
                                    MyLog.d("textId:%d, pid:%d, ppid:%d", text.getId(),pid.getId(), ppid.getId());

                                    LinearLayout view=(LinearLayout)ppid;
                                }






                           /*     LinearLayout view=(LinearLayout)getViewById(vg,2131821291);  //图片
                                LinearLayout text_view=(LinearLayout)getViewById(vg,2131821291);  //所有图片加文字 -- 父
                                ReflectionUtils.printfView(text_view, 3);

                                ViewParent p=text_view.getParent();
                                MyLog.e("------ "+p.getClass().getName()+"-- id--"+((View)p).getId());



                                if(isHaveAdd==false){
                                    TextView tv_link=(TextView)getViewById(vg, 2131821294); //复制链接
                                    ImageView download_image=(ImageView)getViewById(vg,2131821285);  //单个下载图
                                    ImageView imageView=new ImageView(vg.getContext());
                                    ImageView imageView2=new ImageView(vg.getContext());
                                    if(download_image!=null){
                                        Bitmap bm = ((BitmapDrawable)download_image.getDrawable()).getBitmap();
                                        imageView.setImageBitmap(bm);
                                        imageView.setBackgroundColor(download_image.getDrawingCacheBackgroundColor());
                                       // imageView.setTextAlignment();
                                    }else{
                                        imageView2.setImageURI(Uri.fromFile(new File("/sdcard/image.png")));
                                    }
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(vg.getContext(), "image  click ... ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    view.addView(imageView);
                                    TextView tv=new TextView(vg.getContext());
                                    tv.setText("无水印下载");
                                    view.addView(tv);
                                    view.addView(imageView2);
                                    view.invalidate();  //重新绘制
                                   // isHaveAdd=true;*/
                               // }
                            }
                        }
                    });


        }
    }







    //1:表示textview, 2:表示button
    private static View getViewByRealTextName(ViewGroup vg, String viewName, String realTextName, int type){
        View result=null;
        try{
            if(vg.getChildCount() == 0){
                return null;
            }else{
                for(int i=0; i<vg.getChildCount(); i++){
                    //  MyLog.e(vg.getChildAt(i).getClass().getName());
                    if (vg.getChildAt(i).getClass().getName().equals(viewName)) {
                        if(type==2){
                            Button button=(Button) vg.getChildAt(i);
                            // MyLog.e("button :\t"+button.getText().toString());
                            if(button.getText().equals(realTextName)) {
                                return button;
                            }
                        }else if(type==1){
                            TextView textView=(TextView) vg.getChildAt(i);
                            MyLog.e("textView :\t"+textView.getText().toString());
                            if(textView.getText().equals(realTextName)) {
                                return textView;
                            }
                        }else if(type == 3){
                            MyLog.e("image -- "+vg.getChildAt(i).getClass().getName());
                            return vg.getChildAt(i);
                        }
                    }
                    if (vg.getChildAt(i) instanceof ViewGroup) {
                        result = getViewByRealTextName((ViewGroup) vg.getChildAt(i), viewName, realTextName,type);
                        if (result == null) {
                            continue;
                        } else {
                            break;
                        }
                    }
                }
                return result;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }



    public static View getViewById(final ViewGroup vg, final int viewId){
        View result=null;
        try{
            if(vg.getChildCount() == 0){
                return null;
            }else{
                for(int i=0; i<vg.getChildCount(); i++){
                    //  MyLog.e(vg.getChildAt(i).getClass().getName());
                    if (vg.getChildAt(i).getId()==viewId) {
                        return vg.getChildAt(i);
                    }
                    if (vg.getChildAt(i) instanceof ViewGroup) {
                        result = getViewById((ViewGroup)vg.getChildAt(i), viewId);
                        if (result == null) {
                            continue;
                        } else {
                            break;
                        }

                    }
                }
                return result;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


}

//2131821291