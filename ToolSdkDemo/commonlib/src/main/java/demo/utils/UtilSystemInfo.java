package demo.utils;

/**
 * Created by hulimin on 2018/2/26.
 */


import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class UtilSystemInfo {
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static Method mReadProcLines;

    public UtilSystemInfo() {
    }

    public static String getGlobalDeviceId(Context appContext) {
        try {
            TelephonyManager tm = (TelephonyManager)appContext.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception var2) {
            return "UNKNOWN";
        }
    }

    public static String getAndroidId(Context context) {
        String androidId = "0";

        try {
            androidId = Settings.Secure.getString(context.getContentResolver(), "android_id");
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return androidId;
    }

    public static String getNetworkType(Context appContext, TelephonyManager telephonyManager) {
        WifiManager wifiManager = (WifiManager)appContext.getSystemService("wifi");

        try {
            if(wifiManager.isWifiEnabled()) {
                return "wifi";
            }
        } catch (Exception var4) {
            ;
        }

        switch(telephonyManager.getNetworkType()) {
            case 0:
                return "unknown";
            case 1:
                return "GPRS";
            case 2:
                return "edge";
            case 3:
                return "UMTS";
            default:
                return "none";
        }
    }

    public static boolean checkConnectivity(Context context) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager == null) {
            return false;
        } else {
            NetworkInfo networkinfo = manager.getActiveNetworkInfo();
            return networkinfo != null && networkinfo.isConnectedOrConnecting();
        }
    }

    public static boolean isWapNetwork(Context context) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connMgr.getActiveNetworkInfo();
            if(info.getType() != 1 && info.getType() != 9) {
                String currentAPN = info.getExtraInfo();
                return currentAPN == null?false:currentAPN.equalsIgnoreCase("cmwap") || currentAPN.equalsIgnoreCase("ctwap") || currentAPN.equalsIgnoreCase("3gwap") || currentAPN.equalsIgnoreCase("uniwap");
            } else {
                return false;
            }
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean isWifiNetwork(Context context) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connMgr.getActiveNetworkInfo();
            if(info.getType() == 1) {
                return true;
            }
        } catch (Exception var3) {
            ;
        }

        return false;
    }

    public static boolean isMobileNetwork(Context context) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connMgr.getActiveNetworkInfo();
            if(info.getType() == 0) {
                return true;
            }
        } catch (Exception var3) {
            ;
        }

        return false;
    }

    public static boolean isMobileNetworkConnecting(Context context) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return connMgr.getNetworkInfo(0).isConnectedOrConnecting();
        } catch (Exception var2) {
            return false;
        }
    }

    public static float dip2px(Context context, float dip) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(1, dip, r.getDisplayMetrics());
        return px;
    }

    public static float sp2px(Context context, float sp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(2, sp, r.getDisplayMetrics());
        return px;
    }

    public static float px2sp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return pxValue / scale + 0.5F;
    }

    public static CharSequence getLabel(Context context, String packageName, String className, String defaultValue) {
        try {
            PackageManager pm = context.getPackageManager();
            CharSequence label = null;
            ComponentName componentName = new ComponentName(packageName, className);
            label = pm.getActivityInfo(componentName, 0).loadLabel(pm);
            if(label == null) {
                label = packageName;
            }

            return (CharSequence)label;
        } catch (Exception var7) {
            return defaultValue;
        }
    }

    public static boolean isMIUI() {
        try {
            UtilSystemInfo.BuildProperties prop = UtilSystemInfo.BuildProperties.newInstance();
            return prop.getProperty("ro.miui.ui.version.code", (String)null) != null || prop.getProperty("ro.miui.ui.version.name", (String)null) != null || prop.getProperty("ro.miui.internal.storage", (String)null) != null;
        } catch (IOException var1) {
            return false;
        }
    }

    public static final void readProcLines(String path, String[] reqFields, long[] outSize) {
        if(mReadProcLines == null) {
            try {
                Class<Process> cls = Process.class;
                mReadProcLines = cls.getDeclaredMethod("readProcLines", new Class[]{String.class, String[].class, long[].class});
            } catch (Exception var5) {
                ;
            }
        }

        try {
            mReadProcLines.invoke((Object)null, new Object[]{path, reqFields, outSize});
        } catch (Exception var4) {
            ;
        }

    }

    public static final int getParentPid(int pid) {
        String[] procStatusLabels = new String[]{"PPid:"};
        long[] procStatusValues = new long[]{-1L};
        readProcLines("/proc/" + pid + "/status", procStatusLabels, procStatusValues);
        return (int)procStatusValues[0];
    }

    public static final int getUidForPid(int pid) {
        String[] procStatusLabels = new String[]{"Uid:"};
        long[] procStatusValues = new long[]{-1L};
        readProcLines("/proc/" + pid + "/status", procStatusLabels, procStatusValues);
        return (int)procStatusValues[0];
    }

    public static String getPidCmdline(int pid) {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(String.format("/proc/%1$s/cmdline", new Object[]{Integer.valueOf(pid)})));
            String var2 = br.readLine().trim();
            return var2;
        } catch (FileNotFoundException var14) {
            ;
        } catch (IOException var15) {
            ;
        } finally {
            try {
                br.close();
            } catch (Exception var13) {
                ;
            }

        }

        return getCommandLineOutput(String.format("cat /proc/%1$s/cmdline", new Object[]{Integer.valueOf(pid)})).trim();
    }

    private static String getCommandLineOutput(String cmdLine) {
        String output = "";

        try {
            java.lang.Process p = Runtime.getRuntime().exec(cmdLine);

            BufferedReader input;
            String line;
            for(input = new BufferedReader(new InputStreamReader(p.getInputStream())); (line = input.readLine()) != null; output = output + line + '\n') {
                ;
            }

            input.close();
        } catch (Exception var5) {
            ;
        }

        return output;
    }

    public static String getCurrentCountryString(Context context) {
        return context.getResources().getConfiguration().locale.getDisplayCountry();
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        int statusBarHeight = 0;

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
             x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return statusBarHeight;
    }

    public static String getWifiMacAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager)context.getSystemService("wifi");
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if(wifiInfo != null) {
                return wifiInfo.getMacAddress();
            }
        } catch (Exception var3) {
            ;
        }

        BluetoothAdapter bAdapt = BluetoothAdapter.getDefaultAdapter();
        return bAdapt != null?bAdapt.getAddress():"";
    }

    public static List<String> queryAllSdcardPath(Context context) {
        Set<String> result = new HashSet();
        if(Build.VERSION.SDK_INT > 14) {
            StorageManager sm = (StorageManager)context.getSystemService("storage");

            try {
                Method m = StorageManager.class.getDeclaredMethod("getVolumePaths", new Class[0]);
                m.setAccessible(true);
                String[] ps = (String[])((String[])m.invoke(sm, new Object[0]));
                String[] var5 = ps;
                int var6 = ps.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    String p = var5[var7];
                    File f = new File(p);
                    if(f.exists() && f.canWrite() && f.canRead()) {
                        try {
                            new StatFs(p);
                            result.add(p);
                        } catch (Exception var11) {
                            ;
                        }
                    }
                }
            } catch (Exception var12) {
                ;
            }

            if(result.size() == 0 && Environment.getExternalStorageState().equals("mounted")) {
                result.add(Environment.getExternalStorageDirectory().getAbsolutePath());
            }
        } else if(Environment.getExternalStorageState().equals("mounted")) {
            result.add(Environment.getExternalStorageDirectory().getAbsolutePath());
        }

        return new ArrayList(result);
    }

    public static File queryDownloadFolder(Context context) {
        File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if(isFolderAvailable(downloadFolder)) {
            return downloadFolder;
        } else {
            downloadFolder = context.getExternalFilesDir((String)null);
            if(isFolderAvailable(downloadFolder)) {
                downloadFolder = new File(downloadFolder, "downloads");
                if(downloadFolder.exists()) {
                    return downloadFolder;
                }

                if(downloadFolder.mkdir()) {
                    return downloadFolder;
                }
            }

            if(Environment.getExternalStorageState().equals("mounted")) {
                downloadFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), context.getPackageName());
                if(isFolderAvailable(downloadFolder)) {
                    downloadFolder = new File(downloadFolder, "downloads");
                    if(downloadFolder.exists()) {
                        return downloadFolder;
                    }

                    if(downloadFolder.mkdir()) {
                        return downloadFolder;
                    }
                }
            }

            return null;
        }
    }

    public static boolean isFolderAvailable(File folder) {
        if(folder != null && folder.canRead() && folder.canWrite()) {
            File tmpFile = new File(folder.getPath(), "tmp");

            try {
                tmpFile.createNewFile();
                if(tmpFile.exists()) {
                    tmpFile.delete();
                    return true;
                } else {
                    return tmpFile.createNewFile();
                }
            } catch (Exception var3) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isAvailableStorageEnough(long total) {
        StatFs fs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long free = (long)fs.getAvailableBlocks() * (long)fs.getBlockSize();
        return free > total;
    }

    public static String getUIPName(Context context) {
        int pid = Process.myPid();
        ActivityManager mActivityManager = (ActivityManager)context.getSystemService("activity");
        Iterator var3 = mActivityManager.getRunningAppProcesses().iterator();

        ActivityManager.RunningAppProcessInfo appProcess;
        do {
            if(!var3.hasNext()) {
                return "";
            }

            appProcess = (ActivityManager.RunningAppProcessInfo)var3.next();
        } while(appProcess.pid != pid);

        return appProcess.processName;
    }

    public static ViewGroup.LayoutParams getImageParams(Context context, ViewGroup.LayoutParams params) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        params.width = width;
        params.height = width / 16 * 9;
        return params;
    }

    public static boolean hasSIM(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimState() != 1;
    }

    public static String apiLevelExchangePLatformVersion(int apiLevel) {
        switch(apiLevel) {
            case 15:
                return "Android 4.0.3";
            case 16:
                return "Android 4.1";
            case 17:
                return "Android 4.2";
            case 18:
                return "Android 4.3";
            case 19:
                return "Android 4.4";
            case 20:
                return "Android 4.4W";
            case 21:
                return "Android 5.0";
            case 22:
                return "Android 5.1";
            case 23:
                return "Android 6.0";
            case 24:
                return "Android 7.0";
            case 25:
                return "Android 7.1";
            default:
                return "未知类型";
        }
    }

    public static boolean isUnAvailable(long size) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = (long)stat.getBlockSize();
        long availableBlocks = (long)stat.getAvailableBlocks();
        return blockSize * availableBlocks < size;
    }

    public static Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    public static void setStatusPadding(View v) {
        if(v != null && Build.VERSION.SDK_INT >= 19) {
            v.setPadding(0, getStatusBarHeight(v.getContext()), 0, 0);
        }

    }

    public static JSONObject getCpuInfo() {
        try {
            Map<String, String> cpuInfoMap = new HashMap();
            InputStream is = new FileInputStream("/proc/cpuinfo");
            getStringFromInputStream(is, cpuInfoMap);
            return parseCpuInfoMap(cpuInfoMap);
        } catch (Exception var2) {
            return null;
        }
    }

    private static JSONObject parseCpuInfoMap(Map<String, String> map) throws JSONException {
        String[] mapKeys = new String[]{"processorcnt", "modelname", "features", "cpuimplementer", "cpuarchitecture", "cpuvariant", "cpupart", "cpurevision", "hardware", "revision", "serial"};
        JSONObject jsonObject = new JSONObject();
        Iterator keys = map.keySet().iterator();
        if(keys != null && keys.hasNext()) {
            while(keys.hasNext()) {
                String key = (String)keys.next();
                String[] var5 = mapKeys;
                int var6 = mapKeys.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    String mapKey = var5[var7];
                    if(key.replace(" ", "").toLowerCase().contains(mapKey)) {
                        jsonObject.put(mapKey, map.get(key));
                    }
                }
            }

            if(!jsonObject.has("processorcnt")) {
                jsonObject.put("processorcnt", 1);
            }

            return jsonObject;
        } else {
            return null;
        }
    }

    private static String getStringFromInputStream(InputStream is, Map formatter) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        boolean isMapInitPut = formatter != null && formatter.size() >= 1;

        try {
            while((line = br.readLine()) != null) {
                if(formatter != null && line.contains(":")) {
                    String[] values = line.split(":");
                    if(isMapInitPut) {
                        putValueToMap(formatter, values[0], values[1]);
                    } else if(values[0].trim().equals("processor")) {
                        formatter.put("processorcnt", values[1].trim());
                    } else {
                        formatter.put(values[0].trim(), values[1].trim());
                    }
                }

                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException var15) {
            ;
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException var14) {
                    ;
                }
            }

        }

        return sb.toString();
    }

    private static void putValueToMap(Map<String, String> map, String key, String val) {
        key = key.replace("[", "").replace("]", "").trim();
        val = val.replace("[", "").replace("]", "").trim();
        Iterator iterator = map.keySet().iterator();
        if(iterator != null && iterator.hasNext()) {
            while(iterator.hasNext()) {
                String lKey = (String)iterator.next();
                if(lKey.equals(key)) {
                    map.put(lKey, val);
                    break;
                }
            }
        }

    }

    public static String getProvider(Context context) {
        String provider = "Unknown";

        try {
            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
            String IMSI = telephonyManager.getSubscriberId();
            Log.v("tag", "getProvider.IMSI:" + IMSI);
            if(IMSI == null) {
                if(5 == telephonyManager.getSimState()) {
                    String operator = telephonyManager.getSimOperator();
                    Log.v("tag", "getProvider.operator:" + operator);
                    if(operator != null) {
                        if(!operator.equals("46000") && !operator.equals("46002") && !operator.equals("46007")) {
                            if(operator.equals("46001")) {
                                provider = "中国联通";
                            } else if(operator.equals("46003")) {
                                provider = "中国电信";
                            }
                        } else {
                            provider = "中国移动";
                        }
                    }
                }
            } else if(!IMSI.startsWith("46000") && !IMSI.startsWith("46002") && !IMSI.startsWith("46007")) {
                if(IMSI.startsWith("46001")) {
                    provider = "中国联通";
                } else if(IMSI.startsWith("46003")) {
                    provider = "中国电信";
                }
            } else {
                provider = "中国移动";
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return provider;
    }

    public static String getImsi(Context context) {
        String imsi = null;
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
        return telephonyManager.getSubscriberId();
    }

    public static class BuildProperties {
        private final Properties properties = new Properties();

        private BuildProperties() throws IOException {
            this.properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }

        public boolean containsKey(Object key) {
            return this.properties.containsKey(key);
        }

        public boolean containsValue(Object value) {
            return this.properties.containsValue(value);
        }

        public Set<Map.Entry<Object, Object>> entrySet() {
            return this.properties.entrySet();
        }

        public String getProperty(String name) {
            return this.properties.getProperty(name);
        }

        public String getProperty(String name, String defaultValue) {
            return this.properties.getProperty(name, defaultValue);
        }

        public boolean isEmpty() {
            return this.properties.isEmpty();
        }

        public Enumeration<Object> keys() {
            return this.properties.keys();
        }

        public Set<Object> keySet() {
            return this.properties.keySet();
        }

        public int size() {
            return this.properties.size();
        }

        public Collection<Object> values() {
            return this.properties.values();
        }

        public static UtilSystemInfo.BuildProperties newInstance() throws IOException {
            return new UtilSystemInfo.BuildProperties();
        }
    }
}
