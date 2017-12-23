package com.gameassist.plugin.hook;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.gameassist.utils.HookHelp;

import java.lang.reflect.Method;

import demo.utils.MyLog;

//https://www.cnblogs.com/AsionTang/p/6837340.html
public class Hook_wh_android_onLocationChanged extends HookHelp.HookMethod{
    static {
        className = "android.location.LocationManager";
            methodName = "requestLocationUpdates";
            // String provider, long minTime, float minDistance,LocationListener listener
            methodSig = "(Ljava/lang/String;JfLandroid/location/LocationListener)V";
            targetMethod = getMethod(methodName,String.class,long.class,float.class,LocationListener.class);
        }
    public static void hook(Context thiz,  String provider, long minTime, float minDistance,LocationListener listener) {
        MyLog.e("Hook_wh_android_onLocationChanged hook... ");
        if(listener != null) {
            MyLog.e("Hook_wh_android_onLocationChanged is not null ");
            LocationListener ll = listener;

            Class<?> clazz = LocationListener.class;
            Method m = null;
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals("onLocationChanged")) {
                    m = method;
                    break;
                }
            }

            try {
                if (m != null) {
                    Object[] args = new Object[1];
                    Location l = new Location(LocationManager.GPS_PROVIDER);
                    // 39.9057600000,116.5145200000
                    //台北经纬度:121.53407,25.077796
                    //39.9057694431,116.5145426989
                    double la=121.53407;  //经度
                    double lo=25.077796;  //纬度
                    l.setLatitude(la);
                    l.setLongitude(lo);
                    args[0] = l;
                    m.invoke(ll, args);
                    MyLog.e( "fake location----: " + la + ", " + lo);
                }
            } catch (Exception e) {
                MyLog.e(e.getMessage());
            }
        }
        origin(thiz, provider, minTime,minDistance,listener);
    }

    public static void origin(Context thiz,  String provider, long minTime, float minDistance,LocationListener listener) {
        MyLog.e("old method ... ");
         originInvoke(thiz,provider, minTime,minDistance,listener);
    }
}
