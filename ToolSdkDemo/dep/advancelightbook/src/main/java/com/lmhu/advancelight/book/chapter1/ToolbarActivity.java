package com.lmhu.advancelight.book.chapter1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lmhu.advancelight.book.R;

public class ToolbarActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView tv_close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        initViews();
    }

    private void initViews() {
        tv_close=(TextView)findViewById(R.id.tv_close);
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("Toolbar");

        setSupportActionBar(mToolbar);
        //是否给左上角图标的左边加一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.action_settions) {
                    Toast.makeText(ToolbarActivity.this, "action_settings", Toast.LENGTH_SHORT).show();

                } else if (i == R.id.action_share) {
                    Toast.makeText(ToolbarActivity.this, "action_share", Toast.LENGTH_SHORT).show();

                }
                return true;
            }
        });


        //设置侧或布局
        mDrawerLayout=(DrawerLayout)findViewById(R.id.id_drawerlayout);
        mDrawerToggle=new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        setPatette();
    }

    //Palette 可以对一张图片进行色彩分析，开发者可以通过调用Palette提供的方法获取图片中的主题色值等
    private void setPatette() {
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.bitmap);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@NonNull Palette palette) {
                Palette.Swatch swatch=palette.getVibrantSwatch();
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(swatch.getRgb()));
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
