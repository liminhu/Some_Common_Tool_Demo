package demo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by hulimin on 2017/12/12.
 */

public class ApkUtils {
    /**
     * 是否安装应用
     * @param context
     * @param pakName
     * @return
     */
    public static boolean isInstallApk(Context context, String pakName){
      final PackageManager packageManager=context.getPackageManager();
      List<PackageInfo> packageInfoList=packageManager.getInstalledPackages(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
      for(int i=0; i<packageInfoList.size(); i++){
          if(packageInfoList.get(i).packageName.equals(pakName)){
              return true;
          }
      }
      return false;
    }




}
