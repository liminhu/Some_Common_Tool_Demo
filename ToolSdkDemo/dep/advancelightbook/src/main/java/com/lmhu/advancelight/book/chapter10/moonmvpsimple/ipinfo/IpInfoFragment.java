package com.lmhu.advancelight.book.chapter10.moonmvpsimple.ipinfo;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lmhu.advancelight.book.R;
import com.lmhu.advancelight.book.chapter10.moonmvpsimple.model.IpData;
import com.lmhu.advancelight.book.chapter10.moonmvpsimple.model.IpInfo;

import demo.utils.MyLog;

/**
 * Created by hulimin on 2018/2/23.
 */

public class IpInfoFragment extends Fragment implements IpInfoContract.View{
    private TextView tv_country;
    private TextView tv_area;
    private TextView tv_city;
    private Button bt_ipinfo;
    private Dialog mDialog;
    private IpInfoContract.Presenter mPresenter;

    public static IpInfoFragment newInstance(){
        return new IpInfoFragment();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MyLog.e("获取数据中 ... ");
        mDialog=new ProgressDialog(getActivity());
        mDialog.setTitle("获取数据中");
        bt_ipinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.getIpInfo("39.155.184.147");
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MyLog.e("onCreateView  ... fragment_ipinfo");
        View root=inflater.inflate(R.layout.fragment_ipinfo, container,  false);
        tv_country=(TextView)root.findViewById(R.id.tv_country);
        tv_area=(TextView)root.findViewById(R.id.tv_area);
        tv_city=(TextView)root.findViewById(R.id.tv_city);
        bt_ipinfo=(Button)root.findViewById(R.id.bt_ipinfo);
        return  root;
    }

    @Override
    public void setPresenter(IpInfoContract.Presenter presenter) {
        mPresenter=presenter;
    }

    @Override
    public void setIpInfo(IpInfo ipInfo) {
        if(ipInfo!=null && ipInfo.getData() != null){
            IpData ipData=ipInfo.getData();
            tv_country.setText(ipData.getCountry());
            tv_area.setText(ipData.getArea());
            tv_city.setText(ipData.getCity());
        }
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        if(mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    @Override
    public void showError() {
        Toast.makeText(getActivity().getApplicationContext(), "网络出错", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
