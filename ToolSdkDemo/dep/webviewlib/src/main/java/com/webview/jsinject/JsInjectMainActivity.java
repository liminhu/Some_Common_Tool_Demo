package com.webview.jsinject;

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

import demo.toolsdk.hlm.com.webviewlib.R;
import demo.toolsdk.hlm.com.webviewlib.WebViewClientBase;
import demo.utils.MyLog;
import demo.utils.ResourceUtil;

public class JsInjectMainActivity extends AppCompatActivity implements View.OnClickListener{
    private  static  WebViewJsInjectClientBase webViewClientBase;
    private WebView webview;
    private FrameLayout frame;
    private static  final String baidu_url="https://kyfw.12306.cn/otn/login/init";
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
            webview=new WebViewBase(this);
            MyLog.e("initWebViewSetting  ---- init ... 111");
        }
        webViewClientBase=new WebViewJsInjectClientBase();
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(webViewClientBase);
    }






    @Override
    public void onClick(View view) {
        int call_map= ResourceUtil.getId(this, "call_map");
        initWebViewSetting();
        if(webview !=null && view.getId()==call_map){
            MyLog.e("call map ... 222 "+R.id.call_map);
            webview.loadUrl(baidu_url);
        }
        try {
            frame.removeAllViews();
            frame.addView(webview);
        } catch (Exception e) {
            frame.removeAllViews();
            MyLog.e(e.getMessage());
        }
    }



}
