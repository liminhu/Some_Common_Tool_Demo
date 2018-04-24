package com.hlm.douyin.demo.utils;

import com.hlm.douyin.demo.cookie.CookieJarImpl;
import com.hlm.douyin.demo.cookie.PersistentCookieStore;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import demo.utils.MyLog;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.hlm.douyin.demo.DouyinMainActivity.PHPSESSIID;

public class OkhttpManager {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36";

    private static OkhttpManager okhttpUtils;
    private static OkHttpClient client;




    //_ga=GA1.2.2069166943.1524151110; _gid=GA1.2.1807046530.1524151110; PHPSESSIID=390686241525; _gat=1

    public String getDownloadLinkByPostMethod(String url, Map<String, String> map){
        try{

            String cookie="_ga=GA1.2.2069166943.1524151110; _gid=GA1.2.1807046530.1524151110; PHPSESSIID="+PHPSESSIID+"; _gat=1";
            MyLog.e("map .... "+map.size());
            FormBody.Builder formBodyBuild=new FormBody.Builder();
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                formBodyBuild.add(entry.getKey(), entry.getValue());
            }
            FormBody formBody=formBodyBuild.build();
            Request request = new Request.Builder()
                    .post(formBody)
                    .url(url)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .addHeader("Cookie", cookie)
                    .addHeader("Origin", "http://douyin.iiilab.com")//设置获取的cookie)
                    .addHeader("Host", "service.iiilab.com") //设置获取的cookie
                    .build();
            MyLog.e(request.body().toString());
            MyLog.e(request.headers().toString());

            Response response = client.newCall(request).execute();
            String result=response.body().string();
            MyLog.e("result .... "+result);
            return  result;
        }catch (Exception e){
            MyLog.e("异常了。。。 "+e.getMessage());
            e.printStackTrace();
        }
        return  null;
    }



    // TODO: 2018/4/21 使用cookie and get请求方式
    public String initCookieByGetMethod(String url){
        try{
            Request request = new Request.Builder()
                   // .addHeader("ContentType", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8; charset=UTF-8")
                    .url(url)
                    .build();
            //execute,同步操作，是在主线程运行的, enqueue异步操作
            Response response = client.newCall(request).execute();
            String result=response.body().string();
            if(result!=null) {
              if(result.length()>100){
                  MyLog.e("请求结果：前100 ---- "+result.substring(0, 100));
              }else{
                  MyLog.e("请求结果： ---- "+result);
              }
            }
            return result;
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
        return  null;
    }





    /**
     * @Title: 获取单例
     */
    public static OkhttpManager getInstance(PersistentCookieStore persistentCookieStore) {
        if (null == okhttpUtils) {
            synchronized (OkhttpManager.class) {
                if (null == okhttpUtils) {
                    okhttpUtils = new OkhttpManager();
                    if(persistentCookieStore!=null){
                        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
                        client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(6, TimeUnit.SECONDS).cookieJar(cookieJarImpl).build();
                    }else{
                        client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(6, TimeUnit.SECONDS).build();
                    }

                }
            }
        }
        return okhttpUtils;
    }

}
