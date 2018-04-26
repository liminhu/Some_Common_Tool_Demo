package com.mx.sdk.nativeutils;

import android.util.Log;

import java.util.HashMap;

import demo.utils.MyLog;

public class SdkManager extends Singleton{
    //1 -- 包名;    1--base64, 2---md5, 3--aes
    //2 -- 签名
    //3 -- 数据加密
    private HashMap<Integer, String> sdkTable = new HashMap<>();

    @Override
    protected Object createInstance() {
        return new SdkManager();
    }

    public void setSdkTable(int index, String algorithm) {
        MyLog.e("index = "+index+ "--  algorithm = "+algorithm);
        sdkTable.put(index, algorithm);
    }



    public HashMap<Integer, String> getSdkTable() {
        return sdkTable;
    }

    public void setSdkTable(HashMap<Integer, String> sdkTable) {
        this.sdkTable = sdkTable;
    }


    

}
