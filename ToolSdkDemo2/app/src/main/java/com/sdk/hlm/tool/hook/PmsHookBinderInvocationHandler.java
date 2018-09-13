package com.sdk.hlm.tool.hook;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import com.sdk.hlm.tool.utils.MyLog;
import com.test.hlm.application.MyApp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PmsHookBinderInvocationHandler implements InvocationHandler {
    private Object base;

    //应用正确的签名信息
    private String SIGN;
    private String appPkgName = "";

    public PmsHookBinderInvocationHandler(Object base, String sign, String appPkgName, int hashCode) {
        try {
            this.base = base;
            this.SIGN = sign;
            this.appPkgName = appPkgName;
        } catch (Exception e) {
            MyLog.d("error:" + Log.getStackTraceString(e));
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("getPackageInfo".equals(method.getName())) {
            String pkgName = (String) args[0];
            if (pkgName.equals("com.dn.test") || pkgName.equals("com.example.gg")) {
                MyLog.d(pkgName + " --- sign  ---  " + method.getName());
                MyLog.printStackLog("sign ---  ");
            } else {
                MyLog.e(pkgName + "  --- sign  ---  " + method.getName());
            }


            Integer flag = (Integer) args[1];
            if (flag == PackageManager.GET_SIGNATURES && appPkgName.equals(pkgName)) {
                Signature sign = new Signature(SIGN);
                PackageInfo info = (PackageInfo) method.invoke(base, args);
                info.signatures[0] = sign;
                MyLog.d("leng --- "+SIGN.length() +" --- "+SIGN.substring(SIGN.length()/2));


                int hashcode = info.signatures[0].toCharsString().hashCode();
                String msg="new Signature HashCode = 0x" + Integer.toHexString(hashcode);
                MyLog.d("new sign --- "+msg);

                return info;
            }
        }
        return method.invoke(base, args);
    }

}
