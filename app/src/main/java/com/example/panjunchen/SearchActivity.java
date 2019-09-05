package com.example.panjunchen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.panjunchen.models.News;
import com.example.panjunchen.models.TableOperate;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.Date;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rv;
    private RefreshLayout refresh;
    private int index;
    private List<News> list;
    private NewsAdapter adapter;
    String keyword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_favread);
        Intent intent = getIntent();
        Bundle bund=intent.getExtras();
        keyword=bund.getString("keyword");
        toolbar=findViewById(R.id.toolbar2);
        toolbar.setTitle("搜索结果");
        rv=findViewById(R.id.newslist);
        index=0;
        list=TableOperate.getInstance().getNewsSearch(keyword,10,index);
        index=list.size();
        Log.d("DEBUG","length:"+index);
        adapter=new NewsAdapter(list,this);
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(new SingleItemClickListener(rv, new SingleItemClickListener.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getBaseContext(),ReadActivity.class);
                try {
                    list.get(position).setReadtime(new Date());
                    TableOperate.getInstance().renewNews(list.get(position));
                    adapter.notifyDataSetChanged();
                    intent.putExtra("index", list.get(position).getDBindex());
                    startActivity(intent);
                }
                catch(IndexOutOfBoundsException e)
                {
                    Log.d("DEBUG:","out of bound:"+position+" "+list.size());
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.d("DEBUG:","long click:"+position);
            }
        }));

        refresh=findViewById(R.id.refresh);
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                List<News> k;
                Log.d("DEBUG:","index:"+index);
                k= TableOperate.getInstance().getNewsSearch(keyword, 10, index);

                for (int i = 0; i < k.size(); i++) {
                    list.add(k.get(i));
                }
                index = list.size();
                adapter.notifyDataSetChanged();
                refreshlayout.finishLoadMore();
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        refresh.setEnableRefresh(false);


    }
}
