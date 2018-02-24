package com.lmhu.advancelight.book.chapter10.moonmvpsimple.ipinfo;

import com.lmhu.advancelight.book.chapter10.moonmvpsimple.LoadTasksCallBack;
import com.lmhu.advancelight.book.chapter10.moonmvpsimple.model.IpInfo;
import com.lmhu.advancelight.book.chapter10.moonmvpsimple.net.NetTask;

import demo.utils.MyLog;

/**
 * Created by hulimin on 2018/2/23.
 */

public class IpInfoPresenter implements IpInfoContract.Presenter, LoadTasksCallBack<IpInfo>{
    private NetTask netTask;
    private IpInfoContract.View addTaskView;


    public IpInfoPresenter(IpInfoContract.View addTaskView, NetTask netTask) {
        this.netTask = netTask;
        this.addTaskView = addTaskView;
    }



    @Override
    public void onSuccess(IpInfo ipInfo) {
        if(addTaskView.isActive()){
            MyLog.e("addTaskView --- "+ipInfo.toString());
            addTaskView.setIpInfo(ipInfo);
        }
    }

    @Override
    public void onStart() {
        if(addTaskView.isActive()){
            MyLog.e("addTaskView --- showLoading ");
            addTaskView.showLoading();
        }
    }

    @Override
    public void onFailed() {
        if(addTaskView.isActive()){
            MyLog.e("addTaskView --- onFailed ");
            addTaskView.showError();
            addTaskView.hideLoading();
        }
    }

    @Override
    public void onFinish() {
        if(addTaskView.isActive()){
            MyLog.e("addTaskView --- onFinish ");
            addTaskView.hideLoading();
        }
    }

    @Override
    public void getIpInfo(String ip) {
        MyLog.e("netTask --- getIpInfo ");
        netTask.execute(ip, this);
    }
}
