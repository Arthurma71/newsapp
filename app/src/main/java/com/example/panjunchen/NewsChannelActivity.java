package com.example.panjunchen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.panjunchen.models.IOnDragVHListener;
import com.example.panjunchen.models.IOnItemMoveListener;
import com.example.panjunchen.models.NewsChannelBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Meiji on 2017/3/10.
 */

class ItemDragHelperCallback extends ItemTouchHelper.Callback {

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager || manager instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }
        // 如果想支持滑动(删除)操作, swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // 不同Type之间不可移动
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        if (recyclerView.getAdapter() instanceof IOnItemMoveListener) {
            IOnItemMoveListener listener = ((IOnItemMoveListener) recyclerView.getAdapter());
            listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // 不在闲置状态
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof IOnDragVHListener) {
                IOnDragVHListener itemViewHolder = (IOnDragVHListener) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof IOnDragVHListener) {
            IOnDragVHListener itemViewHolder = (IOnDragVHListener) viewHolder;
            itemViewHolder.onItemFinish();
        }
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        // 不支持长按拖拽功能 手动控制
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        // 不支持滑动功能
        return false;
    }
}










public class NewsChannelActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private NewsChannelAdapter adapter;
    List<NewsChannelBean> enableItems;
    List<NewsChannelBean> disableItems;
    private Toolbar tb;
    int[] enablelist;
    private ArrayList<String> titles;
    private boolean[] isactive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_channel);
        Intent intent = getIntent();
        Bundle bund=intent.getExtras();
        enablelist=bund.getIntArray("enablelist");

        initView();
        initData();
    }

    private void initView() {
        tb=findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void onStop() {
        super.onStop();
        enableItems=adapter.getmMyChannelItems();
        int mylist[]=new int[enableItems.size()];
        for(int i=0;i<enableItems.size();i++)
        {
            mylist[i]=titles.indexOf(enableItems.get(i).getChannelName());

        }
        Intent intent=new Intent();
        Bundle bund=new Bundle();
        bund.putIntArray("mylist",mylist);
        intent.putExtras(bund);
        setResult(1,intent);
        finish();
    }


    @Override
    public void onBackPressed()
    {
        onStop();
    }


    private void initData() {
        String[] tit=new String[]{"社会","财经","文化","教育","娱乐","体育","军事","健康","汽车","推荐"};
        titles=new ArrayList<String>();
        for(int i=0;i<tit.length;i++)
        {
            titles.add(tit[i]);
        }
        isactive=new boolean[10];
        for(int i=0;i<10;i++)
        {
            isactive[i]=false;
        }
        enableItems=new ArrayList<NewsChannelBean>();
        disableItems=new ArrayList<NewsChannelBean>();
        for(int i=0;i<enablelist.length;i++)
        {
                NewsChannelBean bean=new NewsChannelBean();
                bean.setIsEnable(1);
                bean.setPosition(i);
                bean.setChannelName(titles.get(enablelist[i]));
                isactive[enablelist[i]]=true;
                enableItems.add(bean);
        }

        for(int i=0;i<titles.size();i++) {
            if (!isactive[i])
            {
                NewsChannelBean bean=new NewsChannelBean();
                bean.setIsEnable(1);
                bean.setPosition(i);
                bean.setChannelName(titles.get(i));
                disableItems.add(bean);
            }
        }



        GridLayoutManager manager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(manager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        adapter = new NewsChannelAdapter(this, helper, enableItems, disableItems);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                return viewType == NewsChannelAdapter.TYPE_MY || viewType == NewsChannelAdapter.TYPE_OTHER ? 1 : 4;
            }
        });
        recyclerView.setAdapter(adapter);

        adapter.setOnMyChannelItemClickListener((v, position) -> Toast.makeText(NewsChannelActivity.this, enableItems.get(position).getChannelName() + position, Toast.LENGTH_SHORT).show());
    }

    public synchronized <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
//        Collections.sort(a);
//        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }
}

