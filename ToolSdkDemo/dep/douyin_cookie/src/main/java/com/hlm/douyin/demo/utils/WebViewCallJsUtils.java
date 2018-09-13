package com.hlm.douyin.demo.utils;

import android.os.Handler;
import android.webkit.JavascriptInterface;


import demo.utils.MyLog;

import static com.hlm.douyin.demo.DouyinMainActivity.data_param;
import static com.hlm.douyin.demo.DouyinMainActivity.random_str;

public class WebViewCallJsUtils {

    private Handler handler;

    public WebViewCallJsUtils(Handler handler) {
        this.handler = handler;
    }

    @JavascriptInterface
    public void callAndroid(String checkValue){
        MyLog.e(checkValue+" --- calAndroid    --   checkValue "+checkValue);
        handler.obtainMessage(0, checkValue).sendToTarget();
    }




    @JavascriptInterface
    public String genInitParam(){
        String aaa = data_param + "@"+random_str;
        MyLog.e(aaa+" ---  原串");
       return  aaa;
    }

}
