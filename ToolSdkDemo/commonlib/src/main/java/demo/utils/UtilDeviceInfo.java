package demo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Created by hulimin on 2018/2/26.
 */

public class UtilDeviceInfo {
    public UtilDeviceInfo() {
    }

    public static JSONObject getDeviceInfo(Context targetContext) {
        JSONObject _deviceInfo = null;

        try {
            _deviceInfo = new JSONObject();
            _deviceInfo.put("mac", UtilSystemInfo.getWifiMacAddress(targetContext));
            if(Build.FINGERPRINT.split("/").length < 2) {
                _deviceInfo.put("fingerprint", Build.BRAND + "/" + Build.PRODUCT + "/" + Build.DEVICE + ":" + Build.VERSION.RELEASE + "/" + Build.ID + "/" + Build.VERSION.INCREMENTAL + ":" + Build.TYPE + "/" + Build.TAGS);
            } else {
                _deviceInfo.put("fingerprint", Build.FINGERPRINT);
            }

            _deviceInfo.put("model", Build.MODEL);
            _deviceInfo.put("product", Build.PRODUCT);
            _deviceInfo.put("vendor", Build.MANUFACTURER);
            _deviceInfo.put("sdk", Build.VERSION.SDK_INT);
            _deviceInfo.put("prop", getProp());
            _deviceInfo.put("imei", UtilSystemInfo.getGlobalDeviceId(targetContext));
            WindowManager wm = (WindowManager)targetContext.getSystemService("window");
            Display display = wm.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            display.getMetrics(dm);
            _deviceInfo.put("widthPixels", dm.widthPixels);
            _deviceInfo.put("heightPixels", dm.heightPixels);
            _deviceInfo.put("density", dm.densityDpi);
            String androidId = UtilSystemInfo.getAndroidId(targetContext);
            _deviceInfo.put("currentAndroidId", androidId);
            _deviceInfo.put("firstAndroidId", androidId);
            _deviceInfo.put("firstBoot", UtilSystemProperties.getLong("ro.runtime.firstboot", -1L));
            _deviceInfo.put("firstImei", UtilSystemInfo.getGlobalDeviceId(targetContext));
            _deviceInfo.put("language", Locale.getDefault().getLanguage());
            _deviceInfo.put("country", Locale.getDefault().getCountry());
            _deviceInfo.put("connectionType", getCurrentNetworkType(targetContext));
            _deviceInfo.put("carrier", UtilSystemInfo.getProvider(targetContext));
            _deviceInfo.put("imsi", UtilSystemInfo.getImsi(targetContext));
        } catch (Exception var6) {
            var6.printStackTrace();
            _deviceInfo = null;
        }

        return _deviceInfo;
    }

    public static JSONObject getDeviceInfo(Context targetContext, String firstUUId) {
        JSONObject _deviceInfo = null;

        try {
            _deviceInfo = new JSONObject();
            _deviceInfo.put("mac", UtilSystemInfo.getWifiMacAddress(targetContext));
            if(Build.FINGERPRINT.split("/").length < 2) {
                _deviceInfo.put("fingerprint", Build.BRAND + "/" + Build.PRODUCT + "/" + Build.DEVICE + ":" + Build.VERSION.RELEASE + "/" + Build.ID + "/" + Build.VERSION.INCREMENTAL + ":" + Build.TYPE + "/" + Build.TAGS);
            } else {
                _deviceInfo.put("fingerprint", Build.FINGERPRINT);
            }

            _deviceInfo.put("model", Build.MODEL);
            _deviceInfo.put("product", Build.PRODUCT);
            _deviceInfo.put("vendor", Build.MANUFACTURER);
            _deviceInfo.put("sdk", Build.VERSION.SDK_INT);
            _deviceInfo.put("prop", getProp());
            _deviceInfo.put("imei", UtilSystemInfo.getGlobalDeviceId(targetContext));
            WindowManager wm = (WindowManager)targetContext.getSystemService("window");
            Display display = wm.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            display.getMetrics(dm);
            _deviceInfo.put("widthPixels", dm.widthPixels);
            _deviceInfo.put("heightPixels", dm.heightPixels);
            _deviceInfo.put("density", dm.densityDpi);
            String androidId = UtilSystemInfo.getAndroidId(targetContext);
            if(TextUtils.isEmpty(androidId)) {
                androidId = firstUUId;
            }

            _deviceInfo.put("currentAndroidId", androidId);
            _deviceInfo.put("firstAndroidId", androidId);
            _deviceInfo.put("firstBoot", UtilSystemProperties.getLong("ro.runtime.firstboot", -1L));
            _deviceInfo.put("firstImei", UtilSystemInfo.getGlobalDeviceId(targetContext));
            _deviceInfo.put("language", Locale.getDefault().getLanguage());
            _deviceInfo.put("country", Locale.getDefault().getCountry());
            _deviceInfo.put("connectionType", getCurrentNetworkType(targetContext));
            _deviceInfo.put("carrier", UtilSystemInfo.getProvider(targetContext));
            _deviceInfo.put("imsi", UtilSystemInfo.getImsi(targetContext));
        } catch (Exception var7) {
            var7.printStackTrace();
            _deviceInfo = null;
        }
        return _deviceInfo;
    }


    private static int getNetworkClass(Context context) {
        int networkType = 0;

        try {
            NetworkInfo network = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
            if(network != null && network.isAvailable() && network.isConnected()) {
                int type = network.getType();
                if(type == 1) {
                    networkType = -101;
                } else if(type == 0) {
                    TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
                    networkType = telephonyManager.getNetworkType();
                }
            } else {
                networkType = -1;
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return getNetworkClassByType(networkType);
    }



    private static int getNetworkClassByType(int networkType) {
        switch(networkType) {
            case -101:
                return -101;
            case -1:
                return -1;
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return 1;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return 2;
            case 13:
                return 3;
            default:
                return 0;
        }
    }



    public static String getCurrentNetworkType(Context context) {
        int networkClass = getNetworkClass(context);
        String type = "Unknown";
        switch(networkClass) {
            case -101:
                type = "WiFi";
                break;
            case -1:
                type = "Unknown";
                break;
            case 0:
                type = "Unknown";
                break;
            case 1:
                type = "2G";
                break;
            case 2:
                type = "3G";
                break;
            case 3:
                type = "4G";
        }

        return type;
    }


    private static JSONObject getProp() {
        try {
            JSONObject jsonObject = new JSONObject();
            Class spClazz = Class.forName("android.os.SystemProperties");
            Method get = spClazz.getDeclaredMethod("get", new Class[]{String.class});
            get.setAccessible(true);
            String[] propKeys = new String[]{"ro.product.brand", "ro.product.name", "ro.product.model", "ro.build.fingerprint", "ro.build.version.sdk", "ro.build.version.release", "ro.build.date", "ro.build.date.utc", "ro.boot.cpuid", "ro.btconfig.vendor", "persist.sys.timezone", "persist.sys.country", "persist.sys.language", "persist.sys.dalvik.vm.lib", "ro.build.description", "ro.runtime.firstboot", "ro.serialno", "ro.product.device", "ro.kernel.qemu", "ro.hardware", "ro.product.cpu.abi"};
            String[] var4 = propKeys;
            int var5 = propKeys.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String key = var4[var6];
                jsonObject.put(key, (String)get.invoke((Object)null, new Object[]{key}));
            }

            return jsonObject;
        } catch (Exception var8) {
            return null;
        }
    }
}
