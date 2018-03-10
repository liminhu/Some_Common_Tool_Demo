package com.lmhu.advancelight.book.chapter2.Designsupportlibrary;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lmhu.advancelight.book.R;

public class DesignsupportActivity extends AppCompatActivity {
    private Button bt_snackbar;
    private Button bt_textInputLayout;
    private RelativeLayout activity_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_support);
        activity_main=(RelativeLayout)findViewById(R.id.activity_main_design_support);
        bt_textInputLayout=(Button)findViewById(R.id.bt_textInputLayout);
        bt_snackbar=(Button)findViewById(R.id.bt_snackbar);

        bt_textInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DesignsupportActivity.this, TextInputLayoutActivity.class);
                startActivity(intent);
            }
        });

        bt_snackbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSnackbar();
            }
        });
    }


    private void showSnackbar() {
        Snackbar.make(activity_main, "标题",Snackbar.LENGTH_LONG)
                .setAction("点击事件", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(DesignsupportActivity.this, "Toast", Toast.LENGTH_SHORT).show();
                    }
                }).setDuration(Snackbar.LENGTH_LONG).show();
    }

}
