package com.gameassist.plugin.test;

import android.os.Build;
import android.view.View;

import com.gameassist.plugin.Plugin;
import com.gameassist.plugin.nativeutils.NativeUtils;
import com.gameassist.utils.HookHelp;

import demo.utils.MyLog;

import static com.gameassist.utils.HookHelp.hookList;

/**
 * Created by hulimin on 2017/12/23.
 */

public class PluginEntry extends Plugin{
    private static final String LIB_HOOK_PLUGIN = "PluginSdk";
    public static final String TAG = "my_hook_java";
    private View pluginView;

    @Override
    public boolean OnPluginCreate() {
        MyLog.e("test ... 0000");
        System.loadLibrary(LIB_HOOK_PLUGIN);
        NativeUtils.nativeinitHook(Build.VERSION.SDK_INT);
       hookList.add("Hook_wh_android_onLocationChanged");
        // hookList.add("Hook_wh_com_c_a_a_k_onLocationChanged");
       // hookList.add("Hook_wh_ct_bx_onLocationChanged");
        HookHelp.doHookInit(getTargetApplication().getClassLoader());
        return false;
    }

    @Override
    public void OnPlguinDestroy() {

    }

    @Override
    public View OnPluginUIShow() {
        return null;
    }

    @Override
    public void OnPluginUIHide() {
    }
}
