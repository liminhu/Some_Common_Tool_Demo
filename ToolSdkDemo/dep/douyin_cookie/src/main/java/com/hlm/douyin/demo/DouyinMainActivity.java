package com.hlm.douyin.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hlm.douyin.demo.cookie.PersistentCookieStore;
import com.hlm.douyin.demo.utils.OkhttpManager;
import com.hlm.douyin.demo.utils.WebViewCallJsUtils;
import com.hlm.douyin.demo.utils.WebViewClientBase;

import java.util.HashMap;
import java.util.Map;

import demo.toolsdk.hlm.com.webviewlib.R;
import demo.utils.MyLog;

public class DouyinMainActivity extends AppCompatActivity implements View.OnClickListener{
    private static  final String init_url="http://douyin.iiilab.com/";
    public  static String data_param = "https://www.iesdouyin.com/share/video/6543137706434628872/?region=CN&mid=6540592528318860035&titleType=title&utm_source=copy_link&utm_campaign=client_share&utm_medium=android&app=aweme&iid=30740108697&timestamp=1523979269";
    private final static String url= "http://service.iiilab.com/video/douyin";
    public  static String random_str=String.valueOf(Math.random()).substring(2);

    public static String PHPSESSIID="";

    private FrameLayout frame;
    private WebView webview;

    private Button button;
    private PersistentCookieStore persistentCookieStore;
    private OkhttpManager okhttpManager;

    private static  String checkStr;

    private  Handler handler=new Handler(){
        @Override
        public void handleMessage(final Message msg) {
           // super.handleMessage(msg);
            if(msg.what==0){
                checkStr=(String) msg.obj;
               final Map param=genParamMap((String) msg.obj);
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       final String msg=okhttpManager.getDownloadLinkByPostMethod(url,param);
                       handler.post(new Runnable() {
                           @Override
                           public void run() {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();;
                           }
                       });
                   }
               }).start();
            }else if(msg.what==1){
                try {
                    // 格式规定为:file:///android_asset/文件名.html
                    if(webview != null ){
                        webview.loadUrl("file:///android_asset/js.html");
                    }
                    MyLog.e("加载 webview ... ");
                    frame.removeAllViews();
                    frame.addView(webview);
                } catch (Exception e) {
                    frame.removeAllViews();
                    MyLog.e(e.getMessage());
                }

            }
        }
    };



    private static Map genParamMap(String checkValue){
        MyLog.e(" checkvalue -- "+checkValue);
        Map map=new HashMap();
        map.put("link", data_param);
        map.put("r", random_str);   // 随机数
        map.put("s",checkValue);  // 校验数
        MyLog.e(map.toString());
        return map;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_douyin_main);
        button=(Button)findViewById(R.id.dy_bt);
        button.setOnClickListener(this);
        if(persistentCookieStore==null || okhttpManager==null){
            persistentCookieStore=new PersistentCookieStore(getApplication());
            okhttpManager=OkhttpManager.getInstance(persistentCookieStore);
        }
        initWebViewSetting(this);
        frame=(FrameLayout)findViewById(R.id.frameLayout);
    }











    @Override
    public void onClick(View view) {
        if(view.getId()==button.getId()){
            if(checkStr!=null){
                handler.obtainMessage(0,checkStr).sendToTarget();
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    okhttpManager.initCookieByGetMethod(init_url);
                    handler.sendEmptyMessage(1);
                /*    try {
                        Thread.sleep(1000);
                        handler.sendEmptyMessage(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
            }).start();
            Toast.makeText(this," click test ", Toast.LENGTH_SHORT).show();
        }
    }










    /**WebView与JS交互代码*/
    @SuppressLint("SetJavaScriptEnabled")
    public  void initWebViewSetting(Context context){
        if(webview==null){
            webview=new WebView(context);
        }else{
            return;
        }
        MyLog.e("initWebViewSetting  ---- 初始化");
        WebSettings webSettings = webview.getSettings();
        // 让WebView能够执行javaScript
        webSettings.setJavaScriptEnabled(true);
        // 让JavaScript可以自动打开windows
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置缓存
        webSettings.setAppCacheEnabled(true);
        // 设置缓存模式,一共有四种模式
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 设置缓存路径
//        webSettings.setAppCachePath("");
        // 支持缩放(适配到当前屏幕)
        webSettings.setSupportZoom(true);
        // 将图片调整到合适的大小
        webSettings.setUseWideViewPort(true);
        // 支持内容重新布局,一共有四种方式
        // 默认的是NARROW_COLUMNS
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置可以被显示的屏幕控制
        webSettings.setDisplayZoomControls(true);
        // 设置默认字体大小
        webSettings.setDefaultFontSize(12);


        webview.setVerticalScrollBarEnabled(true);
        webview.setHorizontalScrollBarEnabled(true);


        //将那个实例化的函数类设置为”android"的js接口这个。这里什么android名字前台js调用就用什么,比如这这里给名字为abc,html中调用就用onclick="window.abc.callAndroid(...."
        webview.addJavascriptInterface(new WebViewCallJsUtils(handler), "android");
        WebViewClientBase webViewClientBase=new WebViewClientBase();
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(webViewClientBase);
    }







}
