package com.example.panjunchen;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.panjunchen.models.News;
import com.example.panjunchen.models.TableOperate;

import java.util.List;

import static android.view.View.GONE;

public class ReadActivity extends AppCompatActivity {
    private Toolbar tb;
    private TextView title;
    private TextView info;
    private TextView content;
    private ImageView img;
    private Menu mMenu;
    private News news;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsread);
        Intent intent = getIntent();
        int mynews = intent.getIntExtra("index",0);
        news= TableOperate.getInstance().getNewsAt(mynews);
        tb=findViewById(R.id.toolbar_read);
        title=findViewById(R.id.title_read);
        info=findViewById(R.id.info_read);
        content=findViewById(R.id.content_read);
        img=findViewById(R.id.image_read);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.action_share:
                    {
                        break;
                    }
                    case R.id.action_like:
                    {
                        if(news.isIsfavorite())
                        {
                            Log.d("DEBUG:","unliked");
                            news.setIsfavorite(0);
                            TableOperate.getInstance().renewNews(news);

                            menuItem.setIcon(R.drawable.unlike);
                        }
                        else
                        {
                            Log.d("DEBUG:","liked");
                            news.setIsfavorite(1);
                            TableOperate.getInstance().renewNews(news);
                            menuItem.setIcon(R.drawable.like);
                        }
                        break;
                    }
                }
                return false;
            }
        });

        title.setText(news.getTitle());
        title.setTextSize(20);
        content.setText((news.getContent()));
        content.setTextSize(16);
        String inf=news.getPublisher();
        info.setText(inf);
        info.setTextSize(10);
        List<String> pic=news.getImageURL();
        if(pic.size()>0) {
            Glide.with(this).load(pic.get(0))
                    .into(img);
        }
        else
        {
            img.setVisibility(GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.readmenu,menu);
        mMenu=menu;
        MenuItem like=mMenu.findItem(R.id.action_like);
        if(news.isIsfavorite())
        {
            like.setIcon(R.drawable.like);
        }
        else
        {
            like.setIcon(R.drawable.unlike);
        }

        return true;
    }



}
