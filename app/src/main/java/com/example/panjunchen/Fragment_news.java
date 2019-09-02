package com.example.panjunchen;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import androidx.fragment.app.Fragment;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class Fragment_news extends Fragment implements View.OnClickListener  {
    private HorizontalScrollView hs;
    private ViewPager vp;
    private String[] titles;
    private TextView sections[];
    private LinearLayout liner;
    private ArrayList<Fragment> sectionpage;

    public Fragment_news(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.newslist,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hs=getActivity().findViewById(R.id.sectionscroll);
        vp=getActivity().findViewById(R.id.viewpager);
        liner=getActivity().findViewById(R.id.sectionnav);
        sectionpage=new ArrayList<Fragment>();
        titles=new String[]{"社会","财经","文化","教育","娱乐","体育","军事","健康","汽车"};
        inittext();

        for(int i=0;i<titles.length;i++)
        {
            Section frag=new Section(titles[i]);
            sectionpage.add(frag);
        }


        vp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Log.d("DEBUG:","my position:"+position);
                return sectionpage.get(position);
            }

            @Override
            public int getCount() {
                return titles.length;
            }
        });
        setOnClickListener();
    }

    private void inittext() {
        sections=new TextView[titles.length];
        for(int i=0;i<titles.length;i++)
        {
            TextView text=new TextView(getActivity());
            text.setText(titles[i]);
            text.setTextSize(25);
            if (i == 0) {
                text.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
            text.setText(titles[i]);
            text.setId(i);
            text.setOnClickListener(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10,10,10,10);//设置左上右下四个margin值;
            liner.addView(text,layoutParams);
            sections[i]=text;
        }
    }

    private void setOnClickListener() {
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直被调用。
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // 此方法是页面跳转完后被调用
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < titles.length; i++) {
                    if (i == position) {
                        sections[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    } else {
                        sections[i].setTextColor(Color.GRAY);
                    }

                }
                // 标题滑动功能
                int width = sections[position].getWidth();
                int totalWidth = (width + 20) * position;
                hs.smoothScrollTo(totalWidth, 0);
            }

            // 此方法是在状态改变的时候调用。
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Log.d("DEBUG:","currentid:"+id);
        vp.setCurrentItem(id);
    }
}
