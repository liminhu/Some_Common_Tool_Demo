package com.lmhu.advancelight.book.chapter10.moonmvpsimple.net;

import com.lmhu.advancelight.book.chapter10.moonmvpsimple.LoadTasksCallBack;
import com.lmhu.advancelight.book.chapter10.moonmvpsimple.model.IpInfo;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import demo.utils.MyLog;

/**
 * Created by hulimin on 2018/2/23.
 */

public class IpInfoTask implements NetTask<String>{
    private static IpInfoTask INSTANCE=null;
    private static final String HOST="http://ip.taobao.com/service/getIpInfo.php";

    public IpInfoTask() {
    }

    public static IpInfoTask getInstance(){
        if(INSTANCE==null){
            INSTANCE=new IpInfoTask();
        }
        return  INSTANCE;
    }


    @Override
    public void execute(final String ip, final  LoadTasksCallBack loadTasksCallBack) {
        MyLog.e("ip --- "+ip);
        RequestParams requestParams=new RequestParams();
        requestParams.addFormDataPart("ip", ip);
        HttpRequest.post(HOST, requestParams, new BaseHttpRequestCallback<IpInfo>(){
            @Override
            public void onStart() {
                super.onStart();
                MyLog.e("loadTasksCallBack --- onStart ");
                loadTasksCallBack.onStart();
            }


            @Override
            protected void onSuccess(IpInfo ipInfo) {
                super.onSuccess(ipInfo);
                MyLog.e("loadTasksCallBack --- onSuccess ");
                loadTasksCallBack.onSuccess(ipInfo);
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                MyLog.e("loadTasksCallBack --- onFailed ");
                loadTasksCallBack.onFailed();
            }


            @Override
            public void onFinish() {
                super.onFinish();
                MyLog.e("loadTasksCallBack --- onFinish ");
                loadTasksCallBack.onFinish();
            }
        });
    }
}
