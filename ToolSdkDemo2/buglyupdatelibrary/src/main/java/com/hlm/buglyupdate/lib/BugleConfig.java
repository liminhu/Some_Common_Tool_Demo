package com.hlm.buglyupdate.lib;

import android.app.Activity;
import android.content.Context;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.beta.Beta;

public class BugleConfig {
    public static long initDelay=500;
    private static Context context;
    public static String channel="D0";
    public static String appId;
    public static boolean isDebug=false;
    public static boolean isSilence=true;


    public static  void initConfig(Context context, String appId){
        BugleConfig.context =context;
        BugleConfig.appId=appId;
    }


    public  static void addCanShowUpgradeActs(Class<? extends Activity> activity){
        Beta.canShowUpgradeActs.add(activity);
    }




    public static void initBeta(){
        /**
         * @param isManual  用户手动点击检查，非用户点击操作请传false
         * @param isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
         */
        Beta.checkUpgrade(false, isSilence);
        Beta.autoInit = true;
        Beta.enableHotfix = true;
        Beta.initDelay = initDelay;
        Beta.autoCheckUpgrade = true;
        //false表示关闭调试模式
        /***** Bugly高级设置 *****/
        BuglyStrategy strategy = new BuglyStrategy();
        strategy.setAppChannel(channel);
        /***** 统一初始化Bugly产品，包含Beta *****/
        Bugly.init(context, appId, isDebug, strategy);
    }



    public static void checkUpgrade(){
        Beta.checkUpgrade();
    }



}
