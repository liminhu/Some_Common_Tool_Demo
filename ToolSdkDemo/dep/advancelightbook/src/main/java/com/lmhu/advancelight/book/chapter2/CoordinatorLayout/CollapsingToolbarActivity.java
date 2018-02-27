package com.lmhu.advancelight.book.chapter2.CoordinatorLayout;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import com.lmhu.advancelight.book.R;
import com.lmhu.advancelight.book.chapter2.TabLayout.RecyclerViewAdapter;

public class CollapsingToolbarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing_toolbar);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_tablayout);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsion_toolbar);
        collapsingToolbarLayout.setTitle("哆啦A梦");
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerview_collapsing);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new RecyclerViewAdapter(CollapsingToolbarActivity.this));

    }
}
