package com.example.panjunchen;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.panjunchen.models.News;
import com.example.panjunchen.models.TableOperate;

import java.io.IOException;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static android.view.View.GONE;

public class ReadActivity extends AppCompatActivity {
    private Toolbar tb;
    private TextView title;
    private TextView info;
    private TextView content;
    private ImageView img;
    private LinearLayout group;
    private Menu mMenu;
    private News news;
    private JCVideoPlayerStandard playerStandard2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsread);
        Intent intent = getIntent();
        int mynews = intent.getIntExtra("index",0);
        news= TableOperate.getInstance().getNewsAt(mynews);
        tb=findViewById(R.id.toolbar_read);
        title=findViewById(R.id.title_read);
        info=findViewById(R.id.info_read);
        content=findViewById(R.id.content_read);

        playerStandard2 = (JCVideoPlayerStandard) findViewById(R.id.playerstandard2);

        Log.d("video",news.getVideoURL());

        if(news.getVideoURL().equals("")){
            playerStandard2.setVisibility(GONE);
        }
        else{
            playerStandard2.setUp(news.getVideoURL(),JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"视频");
            playerStandard2.startVideo();
        }

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
                        OnekeyShare oks = new OnekeyShare();
                        // title标题，微信、QQ和QQ空间等平台使用
                        oks.setTitle(news.getTitle());
                        // titleUrl QQ和QQ空间跳转链接
                        oks.setTitleUrl("http://sharesdk.cn");
                        // text是分享文本，所有平台都需要这个字段
                        oks.setText("我是分享文本");
                        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                        // url在微信、微博，Facebook等平台中使用
                        oks.setUrl("http://sharesdk.cn");
                        // comment是我对这条分享的评论，仅在人人网使用
                        oks.setComment("我是测试评论文本");
// 启动分享GUI
                        oks.show(getApplicationContext());
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

        group = findViewById(R.id.LinearOperate);

        for(int i = 0;i < pic.size();i ++){
            img=new ImageView(this);
            Glide.with(this).load(pic.get(i))
                    .into(img);
            group.addView(img,2);
            LinearLayout layout = new LinearLayout(this);
            layout.setMinimumHeight(10);
            group.addView(layout,2);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            JCVideoPlayer.releaseAllVideos();
        } catch (Exception e) {
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
