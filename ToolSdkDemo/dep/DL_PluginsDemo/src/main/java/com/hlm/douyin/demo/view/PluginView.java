package com.hlm.douyin.demo.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import demo.toolsdk.hlm.com.webviewlib.R;

/**
 * Created by hulimin on 2017/9/7.
 */

public class PluginView extends FrameLayout implements View.OnClickListener {
    private Context context;
    private TextView tv_insturct_0;
    private static  ClipboardManager clipboardManager;


    private static boolean flag=false;
    public PluginView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    void initView() {
        inflate(context, R.layout.main_layout, this);
        tv_insturct_0 = (TextView) findViewById(R.id.insturct_0);
        tv_insturct_0.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.insturct_0) {
            Toast.makeText(getContext(), "click me test ", Toast.LENGTH_SHORT).show();

        }
    }

}
