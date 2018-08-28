package com.demo;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;

import com.my.utils.tool.FileUtils;
import com.my.utils.tool.MyLog;

import java.io.File;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookUtils {
    private static boolean a;
    private static boolean b;

    public static void initHook(XC_LoadPackage.LoadPackageParam  arg6) {
        try {
            MyLog.d(" is begin hoook .... ");
            XposedHelpers.findAndHookMethod("com.tencent.tinker.loader.app.TinkerApplication", arg6.classLoader, "onCreate", new Object[]{new XC_MethodHook() {


                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    try{
                        Application v0 = (Application)param.thisObject;
                        if(appbrandIsLoad(v0.getClassLoader())) {
                            MyLog.e("JumpHook  -- %s", "TinkerApplication.onCreate " +v0.getClassLoader());
                            a=true;
                            firstMethod(v0.getClassLoader());
                        }
                        else {
                            activityOnCreate(v0.getClassLoader());
                        }

                    }catch (Exception e){
                        MyLog.e(" --- "+e.getMessage());
                    }

                }
            }});
        }
        catch(Throwable v0) {
            MyLog.d("JumpHook %s ", "" + v0.getMessage());
        }
    }





    private static void firstMethod(ClassLoader arg5) {
        try {
            XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.appbrand.appcache.ao", arg5, "a", new Object[]{arg5.loadClass("com.tencent.mm.plugin.appbrand.g"), String.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam  arg6) {
                    String v0_1;
                    MyLog.d("反射中。。。。 00000  -- %s ", "args[0] " + arg6.args[0] + ", args[1]: " + arg6.args[1]);
                    if(g.a() &&  arg6.args[0] != null) {
                        try{
                            MyLog.e("反射中。。。。  "+arg6.args[0]);

                            Object v0 = XposedHelpers.getObjectField(arg6.args[0], "fct");
                            MyLog.d("JumpHook %s ", "a config " + v0);


                            String appId=(String) XposedHelpers.getObjectField(v0, "appId");
                            MyLog.e(" appId:  "+appId);

                            if(v0 != null && ("wx7c8d593b2c3a7703".equals(appId)) && ("game.js".equals(arg6.args[1]))) {
                                MyLog.e(" b is init : "+b);
                                if(b) {
                                    String v1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + arg6.args[1];
                                    MyLog.d("JumpHook", "a0 ------------- filePath " + v1);
                                    h.a(v1, (String) arg6.getResult());
                                    v0_1 = h.a("/sdcard/game662.js");
                                    String v="";
                                    if(!TextUtils.isEmpty(v)) {
                                        MyLog.d("JumpHook", "a0 ------------- changeJs " + v0);
                                        arg6.setResult(v0_1);
                                    }
                                }
                                else {
                                    String result =(String) arg6.getResult();
                                    if(!TextUtils.isEmpty(result)) {
                                        v0_1 = bodyDepth((result));
                                        if(!TextUtils.isEmpty((v0_1))) {

                                            File jump_js=new File("/sdcard/new_jump.js");
                                            if(!jump_js.exists()){
                                                MyLog.e("不存在，现在开始存储--");
                                                FileUtils.newWriteDataToFile(v0_1, jump_js.getAbsolutePath());
                                            }


                                            MyLog.e("v0_1 result :  "+v0_1);
                                            arg6.setResult(v0_1);
                                        }else{
                                            MyLog.e("is null param:  "+result);
                                        }
                                    }else{
                                        MyLog.e("result is null");
                                    }
                                }
                            }
                        }catch (Exception e){
                            MyLog.e("异常 --- "+e.getMessage());
                        }


                    }else {
                        MyLog.d(" is else ... 0000 ");
                    }
                }
            }});
        }
        catch(Throwable v0) {
            MyLog.d("JumpHook %s", "" + v0.getMessage());
        }
    }




    private static void activityOnCreate(ClassLoader arg5) {
        try {
            MyLog.e(" hook  activityOnCreate");
            XposedHelpers.findAndHookMethod("android.app.Activity", arg5, "onCreate", new Object[]{new XC_MethodHook() {


                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                     Object v0 = param.thisObject;
                    MyLog.d("JumpHook  -- %d ", "onCreate " + v0 + ", " + ((Activity)v0).getClassLoader());
                    if(a && (appbrandIsLoad(((Activity)v0).getClassLoader()))) {
                        a=true;
                        firstMethod(((Activity)v0).getClassLoader());
                    }
                }
            }});
        }
        catch(Throwable v0) {
            MyLog.d("JumpHook  -- %s ", "" + v0);
        }
    }







    public static String bodyDepth(String arg20) {
        String v3_1;
        int v3 = arg20.indexOf(".BOTTLE.bodyDepth");
        MyLog.d(" ---------- v3: %d  JumpHook  --- %s  --", v3, "TTLE.bodyDepth ... " + arg20);

        File jump_js=new File("/sdcard/jump.js");
        if(!jump_js.exists()){
            MyLog.e("不存在，现在开始存储--");
            FileUtils.newWriteDataToFile(arg20, jump_js.getAbsolutePath());
        }

        if(v3 >= 1) {
            v3_1 = arg20.substring(v3 - 1, v3);
            MyLog.e(" %s ", "confuseClass ..." + v3_1 + "...");
            if(TextUtils.isEmpty(v3_1)) {
                v3_1 = null;
            }
            else {
                String v4 = "this.combo=new ";
                int v5 = arg20.indexOf(v4);
                String v6 = v3_1 + ".GAME";
                int v7 = arg20.indexOf(v6);
                int v8 = arg20.indexOf("scene.add");
                String v9 = "this.renderer.render(this.scene,this.camera)";
                int v10 = arg20.indexOf(v9);
                int v11 = arg20.indexOf("bottle.status");
                int v12 = arg20.indexOf("mouseDownTime");
                String v13 = v3_1 + ".BOTTLE";
                int v14 = arg20.indexOf(v13);
                String v15 = v3_1 + ".BLOCK";
                int v16 = arg20.indexOf(v15);

                if(v5 != -1) {
                    int v17 = arg20.indexOf(".", v4.length() + v5);
                    if(v17 != -1) {
                        v3_1 = arg20.substring(v4.length() + v5, v17);
                        MyLog.e("JumpHook  --- %s ", "hookConfusedJs className ..." + v3_1 + "...");
                        v3_1.trim();
                    }
                }

                MyLog.d("v3_1: "+v3_1);
                String v3_2 = v3_1;


                int v4_1 = -1;
                if(!TextUtils.isEmpty(v3_2)) {
                    v4_1 = arg20.indexOf(v3_2 + ".Mesh");
                }else {
                    MyLog.d("v3_2 is null ...... "+v3_2);
                }

                MyLog.e("JumpHook  --- %s ", "hookConfusedJs  ---index1 " + v5 + ", " + v7 + ", " + v8 + ", " + v10 + ", " + v11 + ", " + v12 + ", " + v14 + ", " + v4_1 + ", " + v16 + ", ..." +v3_2 + "...");


                if(!TextUtils.isEmpty(v3_2) && v5 != -1 && v7 != -1 && v8 != -1 && v10 != -1 && v11 != -1 && v12 != -1 && v14 != -1 && v4_1 != -1 && v16 != -1) {

                    MyLog.d("last result no null ... ");

                    v3_1 = arg20.substring(0, v5) + ("this.vectorHelperOne=new " + (v3_2) + ".Vector2(0, 0);this.vectorHelperTwo=new " + (v3_2)) + ".Vector2(0, 0);this.helperLine=new " + v3_2 + ".Line();this.helperLine.material.color.setHex(0x0000ff);var pointsOfHelperLine=new Float32Array(6);this.helperLine.geometry.addAttribute(\"position\",new " + (v3_2 + ".BufferAttribute(pointsOfHelperLine,3));this.helperLine.geometry.attributes.position.setDynamic(true);this.helperLine.name=\"helper_line\";this.scene.add(this.helperLine);this.helperArrow=new " + (((String)v3_2)) + ".Mesh(new " + (((String)v3_2)) + ".CircleGeometry(.6, 50),new " + (((String)v3_2)) + ".MeshBasicMaterial({color: 255}));this.helperArrow.name=\"helper_arrow\";this.helperArrow.position.x=-500;this.helperArrow.rotation.x=-Math.PI/2;this.scene.add(this.helperArrow);") + arg20.substring(v5, arg20.length());
                    v4_1 = v3_1.indexOf(v9);
                    return v3_1.substring(0, v4_1) + ("if(\"prepare\" == this.bottle.status) {var i=(Date.now()-this.mouseDownTime)/1e3;var vz=Math.min(i*" + v13 + ".velocityZIncrement,150);vz=+vz.toFixed(2);var vy=Math.min(" + v13 + ".velocityY+i*" + v13 + ".velocityYIncrement,180);vy=+vy.toFixed(2);this.vectorHelperOne.set(this.nextBlock.obj.position.x-this.bottle.obj.position.x,this.nextBlock.obj.position.z-this.bottle.obj.position.z);this.vectorHelperOne.x=+this.vectorHelperOne.x.toFixed(2);this.vectorHelperOne.y=+this.vectorHelperOne.y.toFixed(2);var r=vy/" + v6 + ".gravity*2;var n=this.bottle.obj.position.y.toFixed(2);var a=" + v15 + ".height/2-n;r=+(r-=+((-vy+Math.sqrt(Math.pow(vy,2)-2*" + v6 + ".gravity*a))/-" + v6 + ".gravity).toFixed(2)).toFixed(2);var s=[];this.vectorHelperTwo.set(this.bottle.obj.position.x,this.bottle.obj.position.z);var l=this.vectorHelperOne.setLength(vz * r);this.vectorHelperTwo.add(l);s.push(+this.vectorHelperTwo.x.toFixed(2),+this.vectorHelperTwo.y.toFixed(2));this.helperArrow.position.set(s[0],this.nextBlock.obj.position.y+" + v15 + ".height/2+.15,s[1]);var array=this.helperLine.geometry.attributes.position.array;array[0]=this.currentBlock.obj.position.x;array[1]=this.currentBlock.obj.position.y+" + v15 + ".height/2+.15;array[2]=this.currentBlock.obj.position.z;array[3]=s[0];array[4]=this.nextBlock.obj.position.y+" + v15 + ".height/2+.15;array[5]=s[1];this.helperLine.geometry.computeBoundingSphere();this.helperLine.geometry.attributes.position.needsUpdate=true;}else{this.helperArrow.position.set(-300,0,0);var array=this.helperLine.geometry.attributes.position.array;array[0]=-300;array[1]=0;array[2]=0;array[3]=-500;array[4]=0;array[5]=0;this.helperLine.geometry.computeBoundingSphere();this.helperLine.geometry.attributes.position.needsUpdate = true;}") + v3_1.substring(v4_1, v3_1.length());
                }else {
                    MyLog.d("last  is null ");
                }

                v3_1 = null;
            }
        }
        else {
            v3_1 = null;
        }

        return v3_1;
    }





    private static boolean appbrandIsLoad(ClassLoader arg4) {
        boolean v0_1;
        try {
            arg4.loadClass("com.tencent.mm.plugin.appbrand.appcache.ao");
            arg4.loadClass("com.tencent.mm.plugin.appbrand.g");
            v0_1 = true;
        }
        catch(Exception v0) {
            MyLog.d("JumpHook %s ", "" + v0.getMessage());
            v0_1 = false;
        }
        MyLog.i("is load ... appbrandIsLoad: "+v0_1);
        return v0_1;
    }

}
