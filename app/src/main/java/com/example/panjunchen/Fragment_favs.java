package com.example.panjunchen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.panjunchen.models.News;
import com.example.panjunchen.models.TableOperate;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fragment_favs extends Fragment {
    private NewsAdapter adapter;
    private RecyclerView rv;
    private RefreshLayout refresh;
    private List<News> list = new ArrayList<News>();
    private int index;
    private int lastindex=-1;
    private int lastchanged=-1;

    public Fragment_favs(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("DEBUG:","fav_create_view");
        if(adapter!=null)
        {
            list=TableOperate.getInstance().getFavorite(10,0);
            index=list.size();
            adapter.notifyDataSetChanged();
            rv.setAdapter(adapter);
        }

        View view=inflater.inflate(R.layout.fragment_favread,container,false);
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                List<News> k;
                Log.d("DEBUG:","index:"+index);
                k= TableOperate.getInstance().getFavorite( 10, index);
                index = index + k.size();
                for (int i = 0; i < k.size(); i++) {
                    list.add(k.get(i));
                }
                adapter.notifyDataSetChanged();
                refreshlayout.finishLoadMore();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("DEBUG:","fav_resume");
        list=TableOperate.getInstance().getFavorite(10,0);
        index=list.size();
        adapter=new NewsAdapter(list,getContext());
        rv.setAdapter(adapter);
    }



    private void init() {
        rv=getView().findViewById(R.id.newslist);
        refresh=getView().findViewById(R.id.refresh);
        list=TableOperate.getInstance().getFavorite(10,0);
        index=list.size();
        adapter=new NewsAdapter(list,getContext());
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(new SingleItemClickListener(rv, new SingleItemClickListener.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getContext(),ReadActivity.class);
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
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        refresh.setEnableRefresh(false);
    }
}
