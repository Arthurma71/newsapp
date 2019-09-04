package com.example.panjunchen;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.os.Bundle;
import android.renderscript.Script;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.panjunchen.models.NewsAccount;
import com.example.panjunchen.models.TableOperate;

public class ChangeActivity extends AppCompatActivity {

    private EditText oldPassword;
    private EditText newPassword;
    private EditText newPasswordAgain;

    private Button changeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        oldPassword = findViewById(R.id.changeOld);
        newPassword = findViewById(R.id.changeNew);
        newPasswordAgain = findViewById(R.id.changeNewAgain);

        changeButton = findViewById(R.id.change);

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newPassword.getText().toString().equals(newPasswordAgain.getText().toString())){
                    NewsAccount newsAccount = TableOperate.getCurrentNewsAccount();
                    newsAccount.setPassword(oldPassword.getText().toString());
                    if(TableOperate.getInstance().changeAccountPassword(newsAccount,newPassword.getText().toString())){
                        Toast toast=Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    }
                    else {
                        Toast toast=Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else {
                    Toast toast=Toast.makeText(getApplicationContext(),"两次密码不一致",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
