package com.example.panjunchen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.panjunchen.models.NewsAccount;
import com.example.panjunchen.models.TableOperate;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText repassword;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.registerUserName);
        password = findViewById(R.id.registerPassword);
        repassword = findViewById(R.id.registerPasswordAgain);

        registerButton = findViewById(R.id.register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().equals(repassword.getText().toString())){
                    if(TableOperate.getInstance().addNewAccount(new NewsAccount(username.getText().toString(),password.getText().toString(),""))){
                        Toast toast=Toast.makeText(getApplicationContext(),"创建账户成功",Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    }
                    else {
                        Toast toast=Toast.makeText(getApplicationContext(),"用户名被占用",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else {
                    Toast toast=Toast.makeText(getApplicationContext(),"两次密码不相同",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
