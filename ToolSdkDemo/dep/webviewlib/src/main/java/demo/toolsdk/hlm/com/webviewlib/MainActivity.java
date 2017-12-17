package demo.toolsdk.hlm.com.webviewlib;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import demo.utils.MyLog;
import demo.utils.ResourceUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private  static  WebViewClientBase webViewClientBase;
    private WebView webview;
    private FrameLayout frame;
    private static  final String baidu_url="http://www.baidu.com";
    private Handler mHandler;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.e("onCreate  ---- begin ... 111");
        int test_linelayout= ResourceUtil.getLayoutId(this, "activity_main_1");
        setContentView(test_linelayout);
        int view_id= ResourceUtil.getId(this, "frameLayout");
        frame=(FrameLayout)findViewById(view_id);
        int call_map= ResourceUtil.getId(this, "call_map");
        findViewById(call_map).setOnClickListener(this);
        mHandler= new Handler();
    }

    @SuppressLint("AddJavascriptInterface")
    private void initWebViewSetting(){
        if(webview==null){
            webview=new WebView(this);
            MyLog.e("initWebViewSetting  ---- init ... 111");
        }
        MyLog.e("initWebViewSetting  ---- begin ... 222");
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
        //将那个实例化的函数类设置为”android"的js接口这个。这里什么android名字前台js调用就用什么,比如这这里给名字为abc,html中调用就用onclick="window.abc.callAndroid(...."
        webview.addJavascriptInterface(new AndroidBridge(), "android");
        webViewClientBase=new WebViewClientBase();
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(webViewClientBase);
    }

    @Override
    public void onClick(View view) {
        int call_map= ResourceUtil.getId(this, "call_map");
        initWebViewSetting();
        if(webview !=null && view.getId()==call_map){
            MyLog.e("call map ... 222 "+R.id.call_map);
            webview.loadUrl("file:///android_asset/index.html");
            //webview.loadUrl(baidu_url);
        }
        try {
            frame.removeAllViews();
            frame.addView(webview);
        } catch (Exception e) {
            frame.removeAllViews();
            MyLog.e(e.getMessage());
        }
    }



    final class AndroidBridge{
        @JavascriptInterface
        public void callAndroid(final String arg1,final String arg2 ){
            MyLog.e("111 --- calAndroid("+arg1+")"+arg2);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MyLog.e("222 --- calAndroid("+arg1+")"+arg2);
                    frame.removeAllViews();
                    //webview=null;
                    webview.clearHistory();
                    webview.clearCache(true);
                    webview.removeAllViews();
                    webview.destroy();
                    webview = null;

                }
            });
        }
    }


}
