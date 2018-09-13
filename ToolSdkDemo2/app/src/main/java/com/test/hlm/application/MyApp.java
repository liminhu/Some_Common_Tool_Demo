package com.test.hlm.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.sdk.hlm.tool.hook.ServiceManagerWraper;
import com.sdk.hlm.tool.utils.MyLog;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.beta.Beta;

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
        MyLog.e("test ... 000 ");
        Beta.checkUpgrade(false,false);
        Beta.autoInit = true;
        Beta.enableHotfix = true;
        Beta.canShowUpgradeActs.add(MainActivity.class);

        Beta.initDelay = 1 * 300;

        //false表示关闭调试模式
        /***** Bugly高级设置 *****/
        BuglyStrategy strategy = new BuglyStrategy();
        strategy.setAppChannel("test");

        /***** 统一初始化Bugly产品，包含Beta *****/
        Bugly.init(getApplicationContext(), "67a9144871", false, strategy);

       // Bugly.init(getApplicationContext(), "67a9144871", true);
    }




}
