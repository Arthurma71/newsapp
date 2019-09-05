package com.example.panjunchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.panjunchen.models.News;
import com.example.panjunchen.models.TableOperate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bnv;
    private Fragment_favs fragment_favs;
    private Fragment_log fragment_log;
    private Fragment_news fragment_news;
    private Fragment_setting fragment_setting;
    private Fragment[] fragments;
    private int lastfragment;

    private void initfragment()
    {
        fragment_favs=new Fragment_favs();
        fragment_log=new Fragment_log();
        fragment_news=new Fragment_news();
        fragment_setting=new Fragment_setting();
        fragments=new Fragment[]{fragment_news,fragment_favs,fragment_log,fragment_setting};
        getSupportFragmentManager().beginTransaction().replace(R.id.mainview,fragment_news).show(fragment_news).commit();
        bnv=findViewById(R.id.navigation);

        bnv.setOnNavigationItemSelectedListener(changeFragment);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.id1:
                {
                    if(lastfragment!=0)
                    {
                        switchFragment(lastfragment,0);
                        lastfragment=0;
                    }
                    return true;
                }
                case R.id.id2:
                {
                    if(lastfragment!=1)
                    {
                        switchFragment(lastfragment,1);
                        lastfragment=1;
                    }
                    return true;
                }
                case R.id.id3:
                {
                    if(lastfragment!=2)
                    {
                        switchFragment(lastfragment,2);
                        lastfragment=2;
                    }
                    return true;
                }
                case R.id.id4:
                {
                    if(lastfragment!=3)
                    {
                        switchFragment(lastfragment,3);
                        lastfragment=3;
                    }
                    return true;
                }
            }


            return false;
        }
    };

    private void switchFragment(int lastfragment,int newfragment)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if(!fragments[newfragment].isAdded())
        {
            transaction.add(R.id.mainview,fragments[newfragment]);
        }
        transaction.show(fragments[newfragment]).commitAllowingStateLoss();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initfragment();
        TableOperate.init(getApplicationContext());
        News videocheck = new News();
        videocheck.setHashcode("123456");
        videocheck.setIsfavorite(1);
        videocheck.setVideoURL("https://key002.ku6.com/xy/d7b3278e106341908664638ac5e92802.mp4");
        videocheck.setContent("videocheck");
        videocheck.setImageURL(new ArrayList<>());
        videocheck.setTitle("追龙");
        videocheck.setTag(new ArrayList<>());
        videocheck.setCategory("娱乐");
        if(!TableOperate.getInstance().isinDB(videocheck.getHashcode()))TableOperate.getInstance().addNews(videocheck);
        News videocheck2 = new News();
        videocheck2.setHashcode("456789");
        videocheck2.setIsfavorite(1);
        videocheck2.setVideoURL("https://www.w3schools.com/html/movie.mp4");
        videocheck2.setContent("videocheck");
        videocheck2.setImageURL(new ArrayList<>());
        videocheck2.setTitle("熊");
        videocheck2.setTag(new ArrayList<>());
        videocheck2.setCategory("娱乐");
        if(!TableOperate.getInstance().isinDB(videocheck2.getHashcode()))TableOperate.getInstance().addNews(videocheck2);
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        Log.d("DEBUG","FUCK!");

    }

}
