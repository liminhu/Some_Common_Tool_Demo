package com.lmhu.advancelight.book.chapter2.Designsupportlibrary;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lmhu.advancelight.book.R;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import demo.utils.MyLog;

public class TextInputLayoutActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;
    private TextInputLayout tl_username;
    private TextInputLayout tl_password;
    private static final String EMAIL_PATTERN= "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern=Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;
    private Button bt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_input_layout);
        tl_username=(TextInputLayout)this.findViewById(R.id.tl_username_design);
        tl_password=(TextInputLayout)this.findViewById(R.id.tl_password_design);

        bt_login=(Button)findViewById(R.id.bt_login_design);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String username=tl_username.getEditText().getText().toString();
        MyLog.e("username  ---- "+username);
        String password=tl_password.getEditText().getText().toString();
        if(!validateUserName(username)){
            tl_username.setErrorEnabled(true);
            tl_username.setError("请输入正确的邮箱地址");
        }else if(!validatePassword(password)){
            tl_password.setErrorEnabled(true);
            tl_password.setError("密码字数太少");
        }else {
            tl_password.setErrorEnabled(false);
            tl_username.setErrorEnabled(false);
            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
        }
    }



    private boolean validatePassword(String password){
        return password.length()>6;
    }


    private boolean validateUserName(String username){
        matcher=pattern.matcher(username);
        MyLog.e("username --- "+username+" --- matcher.matches() "+matcher.matches());
        return matcher.matches();
    }
}
