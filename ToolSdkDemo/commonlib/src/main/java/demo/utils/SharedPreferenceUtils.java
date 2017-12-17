package demo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.gameassist.plugin.bean.LoginBean;

import java.util.Map;

/**
 * Created by hulimin on 2017/9/22.
 */

public class SharedPreferenceUtils {
    public static   String P_PID="p_pid";        //
    public  static String P_NAME="p_name";  //com.tencent.mm
    public  static String IS_MONITOR="is_monitor";

    public static String IS_IMAGEPREVIEWUI="is_ImagePreviewUI";
    public static String IS_ALBUMPREVIEWUI="is_AlbumPreviewUI";

    private final static String SHAREDPREFERENCE_NAME="Share_Data";


    public static boolean saveOrUpdateToken(Context context, LoginBean.TokenBean tokenBean){
       // MyLog.e(" tokenBean :"+tokenBean.token);
        SharedPreferences sps = context.getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sps.edit();
        editor.putString(LoginBean.TokenBean.USERNAME, tokenBean.username);
        editor.putString(LoginBean.TokenBean.TOKEN, tokenBean.token);
        editor.commit();
        return true;
    }





    public static String getTokenStrFromSharedPre(Context context){
        SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        String token=sp.getString(LoginBean.TokenBean.TOKEN, "");
        MyLog.e("token:%s   ---- ", token);
        return token;
    }




    public static LoginBean.TokenBean getTokenFromSharedPre(Context context){
        SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        LoginBean loginBean=new LoginBean();
        LoginBean.TokenBean tokenBean=loginBean.getTokenBean();

        tokenBean.username=sp.getString(LoginBean.TokenBean.USERNAME,"");
        tokenBean.token=sp.getString(LoginBean.TokenBean.TOKEN, "");
        MyLog.e("username:%s, token:%s", tokenBean.username, tokenBean.token);
        return tokenBean;
    }










    public  static boolean clearAllData(Context context){
        MyLog.e(" ------ clearAllData");
        SharedPreferences sps = context.getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sps.edit();
        editor.clear();
        editor.commit();
        return true;
    }


    public  static boolean updateDataToSharedPre(Context context, String key, int value){
        SharedPreferences sps = context.getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sps.edit();
        editor.putInt(key, value);
        editor.commit();
        return true;
    }


    public  static boolean updateDataToSharedPre(Context context, String key, String value){
        SharedPreferences sps = context.getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sps.edit();
        editor.putString(key, value);
        editor.commit();
        return true;
    }


    public  static boolean updateDataToSharedPre(Context context, String key, boolean value){
        SharedPreferences sps = context.getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sps.edit();
        editor.putBoolean(key, value);
        editor.commit();
        return true;
    }



    public  static boolean saveDataToSharedPre(Context context, SharedPreferenceUtils sp){
        SharedPreferences sps = context.getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sps.edit();
        editor.putInt(P_PID,  sp.p_pid);
        editor.putString(P_NAME, sp.p_name);
        editor.putBoolean(IS_ALBUMPREVIEWUI, sp.is_AlbumPreviewUI);
        editor.putBoolean(IS_IMAGEPREVIEWUI, sp.is_ImagePreviewUI);
        editor.putBoolean(IS_MONITOR, sp.is_monitor);
        editor.commit();
        return true;
    }



    public static SharedPreferenceUtils getDataFromSharedPre(Context context){
        SharedPreferences sp = context.getSharedPreferences(SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferenceUtils utils=new SharedPreferenceUtils();
        utils.setP_pid(sp.getInt(P_PID, -1));
        utils.setP_name(sp.getString(P_NAME, ""));
        utils.setIs_AlbumPreviewUI(sp.getBoolean(IS_ALBUMPREVIEWUI, false));
        utils.setIs_ImagePreviewUI(sp.getBoolean(IS_IMAGEPREVIEWUI, false));
        utils.setIs_monitor(sp.getBoolean(IS_MONITOR,false));
        return utils;
    }



    private int p_pid;
    private  String p_name;
    private boolean is_monitor;
    private boolean is_ImagePreviewUI;
    private boolean is_AlbumPreviewUI;


    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public boolean is_monitor() {
        return is_monitor;
    }

    public void setIs_monitor(boolean is_monitor) {
        this.is_monitor = is_monitor;
    }

    public boolean is_ImagePreviewUI() {
        return is_ImagePreviewUI;
    }

    public void setIs_ImagePreviewUI(boolean is_ImagePreviewUI) {
        this.is_ImagePreviewUI = is_ImagePreviewUI;
    }

    public boolean is_AlbumPreviewUI() {
        return is_AlbumPreviewUI;
    }

    public void setIs_AlbumPreviewUI(boolean is_AlbumPreviewUI) {
        this.is_AlbumPreviewUI = is_AlbumPreviewUI;
    }

    public int getP_pid() {
        return p_pid;
    }

    public void setP_pid(int p_pid) {
        this.p_pid = p_pid;
    }
}
