package com.hlm.douyin.demo.cookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.hlm.douyin.demo.DouyinMainActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import demo.utils.MyLog;
import demo.utils.StringUtils;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class PersistentCookieStore implements CookieStore {
    public static final String COOKIE_PREFS = "CookiePrefsFile";
    private  static final String TAG="Cookie_";
    private static final String COOKIE_NAME_PREFIX = "cookie_";

    private final SharedPreferences cookiePrefs;
    private HashMap<String, ConcurrentHashMap<String, Cookie>> cookies;
    private static boolean isCookieExpired(Cookie cookie) {
        MyLog.e(" 是否过期。。。 ");
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    public PersistentCookieStore(Context context) {
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        cookies = new HashMap<String, ConcurrentHashMap<String, Cookie>>();
        Map<String, ?> prefsMap = cookiePrefs.getAll();
        MyLog.e(TAG+"正在首次持久化cookies  PersistentCookieStore ----- size : "+prefsMap.size());
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            if (((String) entry.getValue()) != null && !((String) entry.getValue()).startsWith(COOKIE_NAME_PREFIX)) {
                String[] cookieNames = TextUtils.split((String) entry.getValue(), ",");
                for (String name : cookieNames) {
                    String encodedCookie = cookiePrefs.getString(COOKIE_NAME_PREFIX + name, null);
                    if (encodedCookie != null) {
                        Cookie decodedCookie = decodeCookie(encodedCookie);
                        if (decodedCookie != null) {
                            if (!cookies.containsKey(entry.getKey()))
                                cookies.put(entry.getKey(), new ConcurrentHashMap<String, Cookie>());
                            cookies.get(entry.getKey()).put(name, decodedCookie);
                        }
                        MyLog.e(TAG+"----- "+decodedCookie.name()+"----"+decodedCookie.value());
                    }
                }

            }
        }
	}
    
    protected void add(HttpUrl uri, Cookie cookie) {
        String name = getCookieToken(cookie);
        MyLog.e("cookie ---"+name);
        //将cookies缓存到内存中 如果缓存过期 就重置此cookie
        if (!cookie.persistent()) {
        	MyLog.e(TAG+"addhost--%s ---- getCookieToken --- "+name+"\t---"+cookie.name()+" cookie.value() --"+ cookie.value(), uri.host());
        	if(cookie.name().equals("PHPSESSIID")){
        	    MyLog.e("设置 cookie "+cookie.value());
                DouyinMainActivity.PHPSESSIID=cookie.value();
            }
        	if (!cookies.containsKey(uri.host())) {
                cookies.put(uri.host(), new ConcurrentHashMap<String, Cookie>());
            }
            cookies.get(uri.host()).put(name, cookie);
        } else {
            if (cookies.containsKey(uri.host())) {
                cookies.get(uri.host()).remove(name);
            }
        }
        //讲cookies持久化到本地
        MyLog.e(TAG+"name; "+name+"  ---- add --- "+uri.host());
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.putString(uri.host(), TextUtils.join(",", cookies.get(uri.host()).keySet()));
        prefsWriter.putString(COOKIE_NAME_PREFIX + name, encodeCookie(new SerializableHttpCookie(cookie)));
        prefsWriter.apply();
    }



    protected String getCookieToken(Cookie cookie) {
        return cookie.name() + cookie.domain();
    }

    @Override
    public void add(HttpUrl uri, List<Cookie> cookies) {
    	MyLog.e(TAG+"add --- "+uri.toString()+"  "+cookies.size());
        for (Cookie cookie : cookies) {
            add(uri, cookie);
        }
    }

    @Override
    public List<Cookie> get(HttpUrl uri) {
    	MyLog.e(TAG+"get --- "+uri.toString()+" -- "+uri.host());
        ArrayList<Cookie> ret = new ArrayList<Cookie>();
        if (cookies.containsKey(uri.host())) {
            Collection<Cookie> cookies = this.cookies.get(uri.host()).values();
            for (Cookie cookie : cookies) {
                if (isCookieExpired(cookie)) {
                    remove(uri, cookie);
                } else {
                    ret.add(cookie);
                }
            }
        }
        return ret;
    }

    @Override
    public boolean removeAll() {
      //  MyLog.e(TAG+"remove --- all");
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.apply();
        cookies.clear();
        return true;
    }



    public static  void  myremoveAll(Context context) {
        MyLog.e("------  myremoveAll --- %s", COOKIE_PREFS);
        SharedPreferences cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.apply();
    }




    @Override
    public boolean remove(HttpUrl uri, Cookie cookie) {
        String name = getCookieToken(cookie);
        MyLog.e("remove ---"+name);
        if (cookies.containsKey(uri.host()) && cookies.get(uri.host()).containsKey(name)) {
            cookies.get(uri.host()).remove(name);

            SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
            if (cookiePrefs.contains(COOKIE_NAME_PREFIX + name)) {
                prefsWriter.remove(COOKIE_NAME_PREFIX + name);
            }
            prefsWriter.putString(uri.host(), TextUtils.join(",", cookies.get(uri.host()).keySet()));
            prefsWriter.apply();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Cookie> getCookies() {
    	//MyLog.e(TAG+"getCookies");
        ArrayList<Cookie> ret = new ArrayList<Cookie>();
        for (String key : cookies.keySet())
            ret.addAll(cookies.get(key).values());
        return ret;
    }


    protected String encodeCookie(SerializableHttpCookie cookie) {
        if (cookie == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (IOException e) {
            //Log.d("google_lenve_fb", "IOException in encodeCookie", e);
            return null;
        }
        return StringUtils.byteArrayToHexString(os.toByteArray());
    }

    protected Cookie decodeCookie(String cookieString) {
        byte[] bytes = StringUtils.hexStringtoByteArray(cookieString);
        Cookie cookie = null;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableHttpCookie) objectInputStream.readObject()).getCookie();
        } catch (IOException e) {
           // M.d("google_lenve_fb", "IOException in decodeCookie", e);
        } catch (ClassNotFoundException e) {
            //Log.d("google_lenve_fb", "ClassNotFoundException in decodeCookie", e);
        }
        return cookie;
    }

}
