
package com.example.panjunchen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class Fragment_log extends Fragment {
    private TextView textview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.login,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*
        textview=getActivity().findViewById(R.id.logtext);
        //设置背景磨砂效果
        Glide.with(this).load(R.drawable.head)
                .bitmapTransform(new BlurTransformation(this, 25), new CenterCrop(this))
                .into(mHBack);
        //设置圆形图像
        Glide.with(this).load(R.drawable.head)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(mHHead);*/
    }
}
