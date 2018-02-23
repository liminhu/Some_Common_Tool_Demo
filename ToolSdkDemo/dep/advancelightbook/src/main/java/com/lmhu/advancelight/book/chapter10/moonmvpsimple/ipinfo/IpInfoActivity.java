package com.lmhu.advancelight.book.chapter10.moonmvpsimple.ipinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lmhu.advancelight.book.R;
import com.lmhu.advancelight.book.chapter10.moonmvpsimple.net.IpInfoTask;
import com.lmhu.advancelight.book.chapter10.moonmvpsimple.utils.ActivityUtils;

public class IpInfoActivity extends AppCompatActivity {
    private IpInfoPresenter ipInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_info);
        IpInfoFragment ipInfoFragment=(IpInfoFragment)getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(ipInfoFragment==null){
            ipInfoFragment=IpInfoFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), ipInfoFragment, R.id.contentFrame);
        }
        IpInfoTask ipInfoTask=IpInfoTask.getInstance();
        ipInfoPresenter=new IpInfoPresenter(ipInfoFragment, ipInfoTask);
        ipInfoFragment.setPresenter(ipInfoPresenter);
    }
}
