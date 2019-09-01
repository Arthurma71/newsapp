package com.example.panjunchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.panjunchen.models.AccountServerConnect;
import com.example.panjunchen.models.News;
import com.example.panjunchen.models.TableOperate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

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
        setContentView(R.layout.main_menu);
        initfragment();
        TableOperate.init(getApplicationContext());
        TableOperate.getInstance().getNewsFromServer("科技",20);
        List<News> newsList = TableOperate.getInstance().getNewsFromLocal("科技",10,10);
        for(int i = 0;i < newsList.size();i ++)
        {
            Log.d("getNewsFromLocal",newsList.get(i).getTitle());
        }

        AccountServerConnect accountServerConnect =  new AccountServerConnect("shit","shit","shit","NEW");
        Thread a = new Thread(accountServerConnect);
        a.start();
        while(a.isAlive());
    }
}
