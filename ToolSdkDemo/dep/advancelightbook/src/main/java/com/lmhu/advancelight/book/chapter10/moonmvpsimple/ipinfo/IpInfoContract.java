package com.lmhu.advancelight.book.chapter10.moonmvpsimple.ipinfo;

import com.lmhu.advancelight.book.chapter10.moonmvpsimple.BaseView;
import com.lmhu.advancelight.book.chapter10.moonmvpsimple.model.IpInfo;

/**
 * Created by hulimin on 2018/2/23.
 */

public interface IpInfoContract {
    interface Presenter{
        void getIpInfo(String ip);
    }

    interface View extends BaseView<Presenter>{
        void setIpInfo(IpInfo ipInfo);
        void showLoading();
        void hideLoading();
        void showError();
        boolean isActive();
    }
}
