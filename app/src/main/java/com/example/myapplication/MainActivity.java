package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myapplication.models.AccountServerConnect;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initviews();
        new AccountServerConnect().start();
    }
    public void initviews()
    {
        TextView tv1=findViewById(R.id.textView1);
        tv1.setText(getResources().getString(R.string.title));
        tv1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,getResources().getDimension(R.dimen.title));

        TextView tv2=findViewById(R.id.textView2);
        tv2.setText(getResources().getString(R.string.news));
        tv2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,getResources().getDimension(R.dimen.body));


        ScrollView sv=findViewById(R.id.id_scrollView);
        ImageView img=findViewById(R.id.imageView);
        img.setImageDrawable(getResources().getDrawable(R.drawable.newstest));
    }

}
