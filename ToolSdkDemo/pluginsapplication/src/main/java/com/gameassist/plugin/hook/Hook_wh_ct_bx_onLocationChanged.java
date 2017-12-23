package com.gameassist.plugin.hook;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;

import com.gameassist.utils.HookHelp;

import demo.utils.MyLog;
//https://www.cnblogs.com/AsionTang/p/6837340.html
public class Hook_wh_ct_bx_onLocationChanged extends HookHelp.HookMethod{
    static {
        className = "ct.bx";
        methodName = "onLocationChanged";
        methodSig = "(Landroid/location/Location;)V";
        targetMethod = getMethod(methodName,Location.class);
    }
    public static void hook(Context thiz,Location id) {
        MyLog.e("Hook_wh_ct_bx_onLocationChanged hook... ");
        if(id != null) {
            double la=id.getLatitude();  //经度
            double lo=id.getLongitude();  //纬度
            MyLog.e("原经度："+la+"; 原纬度："+lo);
            Location l = new Location(LocationManager.GPS_PROVIDER);
            la=121.53407;  //经度
            lo=25.077796;  //纬度
            l.setLatitude(la);
            l.setLongitude(lo);
            origin(thiz, l);
            return;
        }
        origin(thiz, id);
    }

    public static void origin(Context thiz, Location id) {
         originInvoke(thiz,id);
    }
}
