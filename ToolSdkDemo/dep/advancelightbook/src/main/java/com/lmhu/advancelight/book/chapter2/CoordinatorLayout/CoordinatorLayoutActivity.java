package com.lmhu.advancelight.book.chapter2.CoordinatorLayout;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lmhu.advancelight.book.R;
import com.lmhu.advancelight.book.chapter10.moonmvpsimple.ipinfo.IpInfoContract;
import com.lmhu.advancelight.book.chapter2.TabLayout.FragmentAdapter;
import com.lmhu.advancelight.book.chapter2.TabLayout.ListFragment;

import java.util.ArrayList;
import java.util.List;

import demo.utils.MyLog;

public class CoordinatorLayoutActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_tablayout);
        setSupportActionBar(toolbar);

        final ActionBar ab=getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        viewPager=(ViewPager)findViewById(R.id.viewpage);
        initViewPager();
    }

    private void initViewPager() {
        mTabLayout=(TabLayout) findViewById(R.id.tabs_tablayout);
        List<String> titles=new ArrayList<>();
        titles.add("精选");
        titles.add("体育");
        titles.add("巴萨");
        titles.add("购物");
        titles.add("明星");
        titles.add("视频");
        titles.add("健康");
        titles.add("励志");
        titles.add("图文");
        titles.add("本地");
        titles.add("动漫");
        titles.add("搞笑");
        titles.add("精选");
        for(int i=0; i<titles.size(); i++){
            MyLog.e(titles.get(i));
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments=new ArrayList<>();
        for(int i=0; i<titles.size(); i++){
            fragments.add(new ListFragment());
        }
        FragmentAdapter fragmentAdapter=new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        //给viewpage设置适配
        viewPager.setAdapter(fragmentAdapter);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(viewPager);
        //给TabLayout设置适配器
        mTabLayout.setTabsFromPagerAdapter(fragmentAdapter);
    }


    public void chechin(View view){
        Snackbar.make(view, "点击成功",Snackbar.LENGTH_SHORT).show();
    }

}
