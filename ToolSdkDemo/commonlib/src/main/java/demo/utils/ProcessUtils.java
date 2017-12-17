package demo.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.Iterator;
import java.util.List;

/**
 * Created by hulimin on 2017/9/22.
 */

public class ProcessUtils {
    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @param context 上下文
     * @param pid     进程的id
     * @return 返回进程的名字
     */
    public static String getCurrentPrecessName(Context context, int pid) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    return info.processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }




    public static  boolean isMainProcess(Context context){
        int pid= android.os.Process.myPid();
        String name=ProcessUtils.getCurrentPrecessName(context, pid);
        MyLog.e(" test pid ---- pid %d, name:%s", pid, name);
        MyLog.e("pid - getPrecessIdByPackageName - "+ProcessUtils.getPrecessIdByPackageName(context));
        if(name.equals("com.tencent.mm")){
            return true;
        }
        return false;
    }



    private static  String getCurrentPackageName(Context context){
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String name = info.packageName;
            return name;
        }catch (Exception e){

        }
        return  "";
    }



    public static int getPrecessIdByPackageName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String name = info.packageName;
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List list = activityManager.getRunningAppProcesses();
            Iterator i = list.iterator();
            while (i.hasNext()) {
                ActivityManager.RunningAppProcessInfo proc_info = (ActivityManager.RunningAppProcessInfo) (i.next());
                try {
                    if (proc_info.processName.equals(name)) {
                        // 根据进程的信息获取当前进程的名字
                        return proc_info.pid;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 没有匹配的项，返回为null
        } catch (Exception e) {

        }
        return -1;
    }


}
