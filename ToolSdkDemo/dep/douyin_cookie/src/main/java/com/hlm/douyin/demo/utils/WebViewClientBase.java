package com.hlm.douyin.demo.utils;

import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import demo.utils.MyLog;

public class WebViewClientBase extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //super.shouldOverrideUrlLoading(view, url);  //这个返回的方法会调用父类方法，也就是跳转至手机浏览器
        MyLog.e("url ... "+url);
      //   return true; //表示当前url即使是重定向url也不会再执行（除了在return true之前使用webview.loadUrl(url)除外，因为这个会重新加载）
        //return false  表示由系统执行url，直到不再执行此方法，即加载完重定向的url（即具体的url，不再有重定向）
        return false;
    }

    public WebViewClientBase() {
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        MyLog.e("hook_url_onPageFinished %s", url);
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        MyLog.e("hook_HttpError %s", view.getUrl());
    }


    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        MyLog.e("hook_onLoadResource  --  %s", ""+view.getUrl());
    }

//ctrl+O:查看所有重载的方法

    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        // TODO Auto-generated method stub
        super.onReceivedError(view, errorCode, description, failingUrl);
        MyLog.e("hook_url_rceivedError  --  %s", failingUrl);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url,
                                       boolean isReload) {
        // TODO Auto-generated method stub
        MyLog.e("hook_url_rceivedError  --  %s", url);
        super.doUpdateVisitedHistory(view, url, isReload);
    }



}
