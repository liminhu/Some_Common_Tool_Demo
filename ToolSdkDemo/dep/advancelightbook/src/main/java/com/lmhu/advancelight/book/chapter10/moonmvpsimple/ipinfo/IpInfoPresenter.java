package com.lmhu.advancelight.book.chapter10.moonmvpsimple.ipinfo;

import com.lmhu.advancelight.book.chapter10.moonmvpsimple.LoadTasksCallBack;
import com.lmhu.advancelight.book.chapter10.moonmvpsimple.model.IpInfo;
import com.lmhu.advancelight.book.chapter10.moonmvpsimple.net.NetTask;

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
            addTaskView.setIpInfo(ipInfo);
        }
    }

    @Override
    public void onStart() {
        if(addTaskView.isActive()){
            addTaskView.showLoading();
        }
    }

    @Override
    public void onFailed() {
        if(addTaskView.isActive()){
            addTaskView.showError();
            addTaskView.hideLoading();
        }
    }

    @Override
    public void onFinish() {
        if(addTaskView.isActive()){
            addTaskView.hideLoading();
        }
    }

    @Override
    public void getIpInfo(String ip) {
        netTask.execute(ip, this);
    }
}
