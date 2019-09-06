package com.example.panjunchen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.panjunchen.models.News;
import com.example.panjunchen.models.TableOperate;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private NewsAdapter adapter;
    private RecyclerView rv;
    private RefreshLayout refresh;
    private List<News> list = new ArrayList<News>();
    private int index;
    private int lastindex=-1;
    private int lastchanged=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        rv=findViewById(R.id.historynewslist);
        refresh=findViewById(R.id.historyrefresh);
        list= TableOperate.getInstance().getHistory(10,0);
        index=list.size();
        adapter=new NewsAdapter(list,this);
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(new SingleItemClickListener(rv, new SingleItemClickListener.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getApplicationContext(),ReadActivity.class);
                try {
                    list.get(position).setReadtime(new Date());
                    lastindex=list.get(position).getDBindex();
                    lastchanged=position;
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
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        refresh.setEnableRefresh(false);

        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                List<News> k;
                Log.d("DEBUG:","index:"+index);
                k= TableOperate.getInstance().getHistory( 10, index);
                index = index + k.size();
                for (int i = 0; i < k.size(); i++) {
                    list.add(k.get(i));
                }
                adapter.notifyDataSetChanged();
                refreshlayout.finishLoadMore();
            }
        });
    }
}
