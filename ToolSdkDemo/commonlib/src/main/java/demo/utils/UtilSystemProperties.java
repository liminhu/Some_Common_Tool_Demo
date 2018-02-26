package demo.utils;

import java.lang.reflect.Method;

/**
 * Created by hulimin on 2018/2/26.
 */

public class UtilSystemProperties {
    private static Class<?> clsSystemProperties;
    private static Method metGet;
    private static Method metGetWithDef;
    private static Method metGetIntWithDef;
    private static Method metGetLongWithDef;
    private static Method metGetBooleanWithDef;
    private static Method metSet;

    public UtilSystemProperties() {
    }

    private static void initOnce() throws Exception {
        clsSystemProperties = Class.forName("android.os.SystemProperties");
        metGet = clsSystemProperties.getDeclaredMethod("get", new Class[]{String.class});
        metGet.setAccessible(true);
        metGetWithDef = clsSystemProperties.getDeclaredMethod("get", new Class[]{String.class, String.class});
        metGetWithDef.setAccessible(true);
        metGetIntWithDef = clsSystemProperties.getDeclaredMethod("getInt", new Class[]{String.class, Integer.TYPE});
        metGetIntWithDef.setAccessible(true);
        metGetLongWithDef = clsSystemProperties.getDeclaredMethod("getLong", new Class[]{String.class, Long.TYPE});
        metGetLongWithDef.setAccessible(true);
        metGetBooleanWithDef = clsSystemProperties.getDeclaredMethod("getBoolean", new Class[]{String.class, Boolean.TYPE});
        metGetBooleanWithDef.setAccessible(true);
        metSet = clsSystemProperties.getDeclaredMethod("set", new Class[]{String.class, String.class});
        metSet.setAccessible(true);
    }

    public static String get(String key) {
        try {
            return (String)metGet.invoke((Object)null, new Object[]{key});
        } catch (Exception var2) {
            return null;
        }
    }

    public static String get(String key, String def) {
        try {
            return (String)metGetWithDef.invoke((Object)null, new Object[]{key, def});
        } catch (Exception var3) {
            return null;
        }
    }

    public static int getInt(String key, int def) {
        try {
            return ((Integer)metGetIntWithDef.invoke((Object)null, new Object[]{key, Integer.valueOf(def)})).intValue();
        } catch (Exception var3) {
            var3.printStackTrace();
            return def;
        }
    }

    public static long getLong(String key, long def) {
        try {
            return ((Long)metGetLongWithDef.invoke((Object)null, new Object[]{key, Long.valueOf(def)})).longValue();
        } catch (Exception var4) {
            return def;
        }
    }

    public static boolean getBoolean(String key, boolean def) {
        try {
            return ((Boolean)metGetBooleanWithDef.invoke((Object)null, new Object[]{key, Boolean.valueOf(def)})).booleanValue();
        } catch (Exception var3) {
            return def;
        }
    }

    public static void set(String key, String val) {
        try {
            metSet.invoke((Object)null, new Object[]{key, val});
        } catch (Exception var3) {
            ;
        }

    }

    static {
        try {
            initOnce();
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }
}
