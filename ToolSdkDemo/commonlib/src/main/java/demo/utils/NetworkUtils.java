package demo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * Created by hulimin on 2017/9/25.
 */

public class NetworkUtils {
    /* 代码IP */
    private static String PROXY_IP = null;
    /* 代理端口 */
    private static int PROXY_PORT = 0;
    /**
     * 判断当前是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetwork(Context context) {
        boolean network = isWifi(context);
        boolean mobilework = isMobile(context);
        if (!network && !mobilework) { // 无网络连接
            MyLog.e("NetworkUtil  --- %s", "无网路链接！");
            return false;
        } else if (network == true && mobilework == false) { // wifi连接
            MyLog.i("NetworkUtil  -- %s", "wifi连接！");
        } else { // 网络连接
            MyLog.e("NetworkUtil  ---- %s", "手机网路连接，读取代理信息！");
            readProxy(context); // 读取代理信息
            return true;
        }
        return true;
    }

    /**
     * 读取网络代理
     *
     * @param context
     */
    private static void readProxy(Context context) {
        Uri uri = Uri.parse("content://telephony/carriers/preferapn");
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            PROXY_IP = cursor.getString(cursor.getColumnIndex("proxy"));
            PROXY_PORT = cursor.getInt(cursor.getColumnIndex("port"));
        }
        cursor.close();
    }

    /**
     * 判断当前网络是否是wifi局域网
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info != null) {
            return info.isConnected(); // 返回网络连接状态
        }
        return false;
    }

    /**
     * 判断当前网络是否是手机网络
     *
     * @param context
     * @return
     */
    public static boolean isMobile(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (info != null) {
            return info.isConnected(); // 返回网络连接状态
        }
        return false;
    }

}
