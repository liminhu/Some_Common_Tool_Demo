package com.test.hlm.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.sdk.hlm.tool.hook.ServiceManagerWraper;
import com.sdk.hlm.tool.utils.MyLog;

public class MyApp extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MyLog.e("test ... 111 ");
        try{
            MyLog.e("attachBaseContext test --- 111 ");
            PackageInfo pm =  base.getPackageManager().getPackageInfo(base.getPackageName(), PackageManager.GET_SIGNATURES);
            int hashcode = pm.signatures[0].toCharsString().hashCode();
            String msg="Signature HashCode = 0x" + Integer.toHexString(hashcode);
            MyLog.d("sig: --- "+pm.signatures[0].toCharsString());
            MyLog.e( msg);

        }catch (Exception e){
            Log.e("jw","test --- "+e.getMessage());
        }
        ServiceManagerWraper.hookPMS(base);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.e("new test ... 000 ");
/*        BugleConfig.initConfig(getApplicationContext(), "67a9144871");
        BugleConfig.initDelay=300;
        BugleConfig.channel="test";
        BugleConfig.addCanShowUpgradeActs(MainActivity.class);
        BugleConfig.initBeta();*/
       // Bugly.init(getApplicationContext(), "67a9144871", true);
    }




}
