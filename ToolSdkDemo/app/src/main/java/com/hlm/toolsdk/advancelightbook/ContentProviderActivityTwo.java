package com.hlm.toolsdk.advancelightbook;

import android.os.Bundle;

import com.hlm.douyin.demo.DouyinMainActivity;


public class ContentProviderActivityTwo extends DouyinMainActivity {
    static {
        System.loadLibrary("JniSdk");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
