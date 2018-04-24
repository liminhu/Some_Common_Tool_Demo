package com.test.common.tencent.mm;

import android.app.Dialog;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
                    });*/


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


                            if(null!=param.thisObject && param.thisObject.getClass().getName().equals("com.douyin.share.a.c.c")){
                                MyLog.e("activity -- "+param.thisObject.getClass().getName());
                               // ReflectionUtils.getAllFields(param.thisObject,0);


                                Dialog dialog=(Dialog)param.thisObject;
                                MyLog.e("getOwnerActivity .. "+dialog.getOwnerActivity().getClass().getName());

                                final ViewGroup vg= (ViewGroup)dialog.getWindow().getDecorView();
                               // ReflectionUtils.printfView(vg, 33);
                                LinearLayout view=(LinearLayout)getViewById(vg,2131821291);
                                if(isHaveAdd==false){
                                    ImageView imageView=new ImageView(vg.getContext());
                                    imageView.setImageURI(Uri.fromFile(new File("/sdcard/image.png")));
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(vg.getContext(), "image  click ... ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    view.addView(imageView);
                                    isHaveAdd=true;
                                }
                            }
                        }
                    });


        }
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