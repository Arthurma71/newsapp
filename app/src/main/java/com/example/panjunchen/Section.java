package com.example.panjunchen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.panjunchen.models.News;
import com.example.panjunchen.models.TableOperate;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class Section extends Fragment{
    private String secname;
    private NewsAdapter adapter;
    private RecyclerView rv;
    private RefreshLayout refresh;
    private List<News> list = new ArrayList<News>();
    private TableOperate db;
    private int index;
    public Section(String name)
    {
        super();
        secname=name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.section,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        init();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                List<News> k=TableOperate.getInstance().getNewsFromServer(secname,10);
                for(int i=0;i<k.size();i++)
                {
                    list.add(k.get(i));
                }
                adapter.notifyDataSetChanged();
                index=10;
                refreshlayout.finishRefresh();
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

                List<News> k = TableOperate.getInstance().getNewsFromLocal(secname, 10, index);
                if(k.size()==0)
                {
                    refreshlayout.finishLoadMoreWithNoMoreData();
                }
                else
                {
                    index = index + k.size();
                    for (int i = 0; i < k.size(); i++) {
                        list.add(k.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    refreshlayout.finishLoadMore();
                }
            }
        });

    }

    private void init() {
        rv=getActivity().findViewById(R.id.newslist);
        db=TableOperate.getInstance();
        refresh=getActivity().findViewById(R.id.refreshLayout);
        list=db.getNewsFromServer(secname,10);
        index=list.size();
        adapter=new NewsAdapter(list,getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
