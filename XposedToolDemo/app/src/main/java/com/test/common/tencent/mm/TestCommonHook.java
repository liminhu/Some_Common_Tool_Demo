package com.test.common.tencent.mm;

import android.content.ContentValues;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class TestCommonHook implements IXposedHookLoadPackage {

private  static final String PACKAGE_NAME="com.tencent.mm";	
	

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
			Log.v("hook_Loaded App:", lpparam.packageName);
			
			
			XposedHelpers.findAndHookMethod("java.lang.StringBuilder", lpparam.classLoader, "toString",
					new XC_MethodHook() {
				    @Override
				    protected void afterHookedMethod(MethodHookParam param)
				    		throws Throwable {
				    	super.afterHookedMethod(param);
				    	String resultString=(String)param.getResult();
				    	Log.v("hook_string_build", resultString);
				    	if(resultString.contains("5509122859@chatroom") || resultString.contains("0.01") || resultString.contains("huli") || resultString.contains("INSERT INTO")){
				    		test();
				    	}
				    }
				});
			
			
			
			
			//    public final long insert(String arg6, String arg7, ContentValues arg8) {
			XposedHelpers.findAndHookMethod("com.tencent.mm.bj.e", lpparam.classLoader, "insert", String.class,
					String.class, ContentValues.class,
					
					new XC_MethodHook() {
				    @Override
				    protected void afterHookedMethod(MethodHookParam param)
				    		throws Throwable {
				    	super.afterHookedMethod(param);
				    	int resultString=(Integer)param.getResult();
				    	Log.e("hook_insert_af", ""+resultString);
				    }
				    
				    
				    @Override
				    	protected void beforeHookedMethod(MethodHookParam param)
				    			throws Throwable {
				    		super.beforeHookedMethod(param);
							Log.e("hook_insert_mm", ""+param.args[0]);
							Log.e("hook_insert_mm", ""+param.args[1]);
							Log.e("hook_insert_mm", ""+param.args[2].toString());
				    	}
				});
			
			
			
			
			XposedHelpers.findAndHookMethod("org.json.JSONObject", lpparam.classLoader, "toString",
					new XC_MethodHook() {
				    @Override
				    protected void afterHookedMethod(MethodHookParam param)
				    		throws Throwable {
				    	super.afterHookedMethod(param);
				    	String resultString=(String)param.getResult();
				    	
				    	Log.v("hook_JSONObject", resultString);
				    }
				});
			
			XposedHelpers.findAndHookMethod("java.util.Arrays", lpparam.classLoader, "toString",
					new XC_MethodHook() {
				    @Override
				    protected void afterHookedMethod(MethodHookParam param)
				    		throws Throwable {
				    	super.afterHookedMethod(param);
				    	String resultString=(String)param.getResult();
				    	Log.v("hook_Arrays", resultString);
				    }
				});
			
			XposedHelpers.findAndHookMethod("android.net.Uri", lpparam.classLoader, "toString",
					new XC_MethodHook() {
				    @Override
				    protected void afterHookedMethod(MethodHookParam param)
				    		throws Throwable {
				    	super.afterHookedMethod(param);
				    	String resultString=(String)param.getResult();
				    	Log.v("hook_Uri", resultString);
				    }
				});
			
			XposedHelpers.findAndHookMethod("org.apache.http.util.EntityUtils", lpparam.classLoader, "toString",
					new XC_MethodHook() {
				    @Override
				    protected void afterHookedMethod(MethodHookParam param)
				    		throws Throwable {
				    	super.afterHookedMethod(param);
				    	String resultString=(String)param.getResult();
				    	Log.v("hook_EntityUtils", resultString);
				    }
				});

			
			XposedHelpers.findAndHookMethod("java.util.ArrayList", lpparam.classLoader, "toString",
					new XC_MethodHook() {
				    @Override
				    protected void afterHookedMethod(MethodHookParam param)
				    		throws Throwable {
				    	super.afterHookedMethod(param);
				    	String resultString=(String)param.getResult();
				    	Log.v("hook_ArrayList", resultString);
				    }
				});
			
			XposedHelpers.findAndHookMethod("java.util.ArrayList", lpparam.classLoader, "toString",
					new XC_MethodHook() {
				    @Override
				    protected void afterHookedMethod(MethodHookParam param)
				    		throws Throwable {
				    	super.afterHookedMethod(param);
				    	String resultString=(String)param.getResult();
				    	Log.v("hook_ArrayList", resultString);
				    }
				});
	
			
		}
	}

}
