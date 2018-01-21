package com.lmhu.floatwindow.PermissonDemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lmhu.floatwindow.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.utils.MyLog;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };
    //@BindView(R.id.main_t_toolbar) Toolbar mTToolbar;


    private PermissionsChecker mPermissionsChecker; // 权限检测器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_permisson);
        ButterKnife.bind(this);
        Toolbar mTToolbar=(Toolbar)findViewById(R.id.main_t_toolbar);
        setSupportActionBar(mTToolbar);
        MyLog.e(".... 1111");
        mPermissionsChecker = new PermissionsChecker(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyLog.e(".... 2222");
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }else {
            MyLog.e(" 9999 ... ");
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        MyLog.e("1111 --  ");
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }
}
