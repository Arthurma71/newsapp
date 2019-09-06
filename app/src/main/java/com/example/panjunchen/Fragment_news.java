package com.example.panjunchen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.panjunchen.models.TableOperate;
import com.google.android.material.tabs.TabLayout;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.DefaultSuggestionsAdapter;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.Inflater;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

class SuggestionHolder extends RecyclerView.ViewHolder{
    protected TextView title;

    public SuggestionHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.history);
    }
}

class CustomSuggestionsAdapter extends SuggestionsAdapter<String, SuggestionHolder> {
    CustomSuggestionsAdapter(LayoutInflater inflater)
    {
        super(inflater);

    }


    @Override
    public void onBindSuggestionHolder(String suggestion, SuggestionHolder holder, int position) {
        holder.title.setText(suggestion);
    }

    @Override
    public SuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item,parent, false);
        parent.setVisibility(View.VISIBLE);
        return new SuggestionHolder(view);
    }

    @Override
    public int getSingleViewHeight() {
        return 60;
    }
}


public class Fragment_news extends Fragment  {

    private ViewPager vp;
    private String[] titles;
    private TextView sections[];
    private TabLayout tabs;
    private ArrayList<Fragment> sectionpage;
    private Button b;
    private MaterialSearchBar searchBar;
    private List<String> historylist;
    private int[] mylist;
    public Fragment_news(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_newslist,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchBar=getActivity().findViewById(R.id.search);
        tabs=getActivity().findViewById(R.id.tabsview);
        vp=getActivity().findViewById(R.id.viewpager);
        b=getActivity().findViewById(R.id.button);
        historylist= TableOperate.getInstance().getSearchHistory();
        sectionpage=new ArrayList<Fragment>();
        mylist=TableOperate.getInstance().getTabList().stream().mapToInt(Integer::intValue).toArray();
        titles=new String[]{"社会","财经","文化","教育","娱乐","体育","军事","健康","汽车","推荐"};
        searchBar.setHint("Search...");
        LayoutInflater inflater=getLayoutInflater();
        DefaultSuggestionsAdapter adapter=new DefaultSuggestionsAdapter(inflater);
        adapter.setSuggestions(historylist);
        searchBar.setCustomSuggestionAdapter(adapter);

        searchBar.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                searchBar.setText(historylist.get(position));
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {
                historylist.remove(position);
                searchBar.updateLastSuggestions(historylist);
            }
        });
        boolean a=searchBar.isSuggestionsVisible();
        Log.d("Debug:","visible"+a);
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(enabled) {
                    historylist = TableOperate.getInstance().getSearchHistory();
                    Log.d("DEBUG", "size:" + historylist.size());
                    searchBar.showSuggestionsList();
                }
                else
                {
                    searchBar.hideSuggestionsList();
                }

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent intent=new Intent(getContext(),SearchActivity.class);
                Bundle bund=new Bundle();
                bund.putString("keyword",text.toString());
                intent.putExtras(bund);
                adapter.addSuggestion(text.toString());
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),NewsChannelActivity.class);
                Bundle bund=new Bundle();
                bund.putIntArray("enablelist",mylist);
                intent.putExtras(bund);
                startActivityForResult(intent,0);
            }
        });



        for(int i=0;i<titles.length;i++)
        {
            Section frag=new Section(titles[i]);
            sectionpage.add(frag);
        }


        vp.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Log.d("DEBUG:","my position:"+position);
                return sectionpage.get(mylist[position]);
            }

            @Override
            public int getCount() {
                return mylist.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {

                return titles[mylist[position]];
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return POSITION_NONE;
            }
        });

        tabs.setupWithViewPager(vp);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0)
        {
            if(resultCode==1){
                Bundle bund=data.getExtras();
                mylist=bund.getIntArray("mylist");
                TableOperate.getInstance().renewTabList(Arrays.stream(mylist).boxed().collect(Collectors.toList()));
                vp.getAdapter().notifyDataSetChanged();
                tabs.setupWithViewPager(vp);
            }
        }
    }
}
