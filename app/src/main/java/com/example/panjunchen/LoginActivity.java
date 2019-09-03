package com.example.panjunchen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allen.library.SuperTextView;
import com.example.panjunchen.models.NewsAccount;
import com.example.panjunchen.models.TableOperate;

public class LoginActivity extends AppCompatActivity {

    private Toolbar tb;
    private EditText username;
    private EditText password;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tb=findViewById(R.id.toolbar_read);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.loginUserName);
        password = findViewById(R.id.loginPassword);

        loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = username.getText().toString();
                String b = password.getText().toString();
                if(TableOperate.getInstance().checkAccount(new NewsAccount(a,b,""))){
                    Toast toast=Toast.makeText(getApplicationContext(),"登录成功，等待同步",Toast.LENGTH_SHORT);
                    toast.show();
                    TableOperate.getInstance().loadAccount(new NewsAccount(a,b,""));
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
                }
                else {
                    Toast toast=Toast.makeText(getApplicationContext(),"账户不存在或密码不正确",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
