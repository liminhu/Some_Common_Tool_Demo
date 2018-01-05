package com.webview.jsinject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import demo.utils.MyLog;

/**
 * Created by hulimin on 2018/1/5.
 */

public class WebViewBase extends WebView {
    public WebViewBase(Context context) {
        super(context);
        initWebSetting();
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initWebSetting(){
        MyLog.d("hook_html_init %s","init context");
        WebSettings webSettings=getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //将那个实例化的函数类设置为”AndroidDealFunction"的js接口这个。
        // 这里什么android名字前台js调用就用什么,比如这这里给名字为abc,html中调用就用onclick="window.abc.callAndroid(...."
      //  addJavascriptInterface(new InJavaScriptLocalObj(), "AndroidDealFunction");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        MyLog.e("onKeyDown ... ");
        if ((keyCode == KeyEvent.KEYCODE_BACK) && canGoBack()) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
        }
        return super.onTouchEvent(ev);
    }



    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            int len = html.length();
            MyLog.d("hook_showSource_len",String.valueOf(len));
            int index = 0;
            byte[] array = new byte[1024];
            for (int i = 0; i < len; i += 1023) {
                index += 1023;
                if (index < len) {
                    System.arraycopy(html.getBytes(), index - 1023, array, 0, 1023);
                    MyLog.d("hook_data_1 %s", new String(array));
                } else {
                    System.arraycopy(html.getBytes(), index - 1023, array, 0, len - index);
                    MyLog.d("hook_data_1 %s", new String(array));
                }
            }
        }
    }
}
