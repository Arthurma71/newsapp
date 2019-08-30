package com.example.panjunchen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class Fragment_news extends Fragment {
    private HorizontalScrollView hs;
    private ViewPager vp;
    private String[] titles;
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
        titles=new String[]{"社会","财经","文化","教育","娱乐","体育","军事","健康","汽车"}
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new Section(titles[position]);
            }

            @Override
            public int getCount() {
                return titles.length;
            }
        });

        private void setOnClickListener() {
            view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                //当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直被调用。
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }
                // 此方法是页面跳转完后被调用
                @Override
                public void onPageSelected(int position) {
                    // 标题变色,用循环改变标题颜色,通过判断来决定谁红谁灰;
                    // 举例:娱乐的下标是position是1
                    for (int i = 0; i < titles.length; i++) {
                        if(i == position){
                            titlesView.get(i).setTextColor(Color.RED);
                        }else {
                            titlesView.get(i).setTextColor(Color.GRAY);
                        }

                    }
                    // 标题滑动功能
                    int width = titlesView.get(position).getWidth();
                    int totalWidth = (width +20)*position;
                    hs.scrollTo(totalWidth,0);
                }

                // 此方法是在状态改变的时候调用。
                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


        }



    }
}
