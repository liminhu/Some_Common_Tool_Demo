package com.test.common.tencent.mm;

import android.os.Bundle;

import com.my.utils.tool.MyLog;
import com.my.xposedhook.hooks.httpHook;
import com.my.xposedhook.hooks.sockhook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findClass;

public class TestGGSandboxHook implements IXposedHookLoadPackage {

    private static final String PACKAGE_NAME = "com.example.gg";





    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals(PACKAGE_NAME)) {
            MyLog.v("hook_Loaded App:  -- %s ", lpparam.packageName);

            final Class cls=findClass("com.yyhd.sandbox.s.proxy.InitializeProvider",  lpparam.classLoader);


            sockhook.initHooking(lpparam);
            httpHook.initHooking(lpparam);
			
/*			XposedHelpers.findAndHookMethod("java.lang.StringBuilder", lpparam.classLoader, "toString",
					new XC_MethodHook() {
				    @Override
				    protected void afterHookedMethod(MethodHookParam param)
				    		throws Throwable {
				    	super.afterHookedMethod(param);
				    	String resultString=(String)param.getResult();
				    	if(resultString!=null){
							MyLog.v("hook_string_build -- %s ", resultString);
						}
				    }
				});*/


            XposedHelpers.findAndHookMethod("org.json.JSONObject", lpparam.classLoader, "toString",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            String resultString = (String) param.getResult();
                            MyLog.e("hook_JSONObject  -- %s ", resultString);
                        }
                    });

            XposedHelpers.findAndHookMethod("java.util.Arrays", lpparam.classLoader, "toString",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            String resultString = (String) param.getResult();
                            MyLog.v("hook_Arrays  -- %s ", resultString);
                        }
                    });

            XposedHelpers.findAndHookMethod("android.net.Uri", lpparam.classLoader, "toString",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            String resultString = (String) param.getResult();
                            MyLog.v("hook_Uri  -- %s ", resultString);
                        }
                    });


            XposedHelpers.findAndHookMethod("java.util.ArrayList", lpparam.classLoader, "toString",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            String resultString = (String) param.getResult();
                            MyLog.v("hook_ArrayList  --- %s ", resultString);
                        }
                    });





            XposedHelpers.findAndHookMethod("com.yyhd.sandbox.s.proxy.InitializeProvider", lpparam.classLoader, "call",
                    String.class, String.class, Bundle.class,

                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            Bundle bundle = (Bundle) param.getResult();
                            MyLog.e("InitializeProvider test --- 1111 "+bundle.toString());
                        }

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            MyLog.d("InitializeProvider test ... 0000 ");
                            super.beforeHookedMethod(param);
                        }
                    });


        }
    }

}
