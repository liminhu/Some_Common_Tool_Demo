package com.demo;

import com.my.utils.tool.MyLog;

import de.robv.android.xposed.XSharedPreferences;

public class g {
    private static XSharedPreferences a;

    static {
        g.a = null;
    }

    public g() {
        super();
    }

    public static boolean a() {
        MyLog.e("jump_flag true ");
        return true; //g.b().getBoolean("jump_flag", true);
    }

    private static XSharedPreferences b() {
        if(g.a == null) {
            g.a = new XSharedPreferences(g.class.getPackage().getName().replace(".xposed", ""));
            g.a.makeWorldReadable();
        }
        else {
            g.a.reload();
        }

        return g.a;
    }
}
