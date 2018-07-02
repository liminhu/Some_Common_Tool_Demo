package com.test.common.tencent.mm;

import android.content.ContentValues;
import android.util.Log;

import com.my.utils.tool.MyLog;
import com.my.xposedhook.hooks.httpHook;
import com.my.xposedhook.hooks.sockhook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class TestTencentRedingHook implements IXposedHookLoadPackage {

private  static final String PACKAGE_NAME="com.tencent.reading";
	

public static void test(){
    Exception e = new Exception("this is a log");
    StackTraceElement[]  elements=e.getStackTrace();
    StringBuilder sb=new StringBuilder();
    for(int i=0; i<elements.length; i++){
        sb.append(elements[i]);
        sb.append("\n");
    }
    Log.e( "hook_Exception", sb.toString());
}


	@Override
	public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
		if(lpparam.packageName.equals(PACKAGE_NAME)){
			MyLog.v("hook_Loaded App:  -- %s ", lpparam.packageName);


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
				    	String resultString=(String)param.getResult();
				    	
				    	MyLog.e("hook_JSONObject  -- %s ", resultString);
				    }
				});
			
			XposedHelpers.findAndHookMethod("java.util.Arrays", lpparam.classLoader, "toString",
					new XC_MethodHook() {
				    @Override
				    protected void afterHookedMethod(MethodHookParam param)
				    		throws Throwable {
				    	super.afterHookedMethod(param);
				    	String resultString=(String)param.getResult();
				    	MyLog.v("hook_Arrays  -- %s ", resultString);
				    }
				});
			
			XposedHelpers.findAndHookMethod("android.net.Uri", lpparam.classLoader, "toString",
					new XC_MethodHook() {
				    @Override
				    protected void afterHookedMethod(MethodHookParam param)
				    		throws Throwable {
				    	super.afterHookedMethod(param);
				    	String resultString=(String)param.getResult();
						MyLog.v("hook_Uri  -- %s ", resultString);
				    }
				});
			

			
			XposedHelpers.findAndHookMethod("java.util.ArrayList", lpparam.classLoader, "toString",
					new XC_MethodHook() {
				    @Override
				    protected void afterHookedMethod(MethodHookParam param)
				    		throws Throwable {
				    	super.afterHookedMethod(param);
				    	String resultString=(String)param.getResult();
				    	MyLog.v("hook_ArrayList  --- %s ", resultString);
				    }
				});
	
			
		}
	}

}
