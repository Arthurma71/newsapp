package com.example.panjunchen;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.panjunchen.models.News;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<News> list;
    private Context _context;


    public NewsAdapter(List<News> news,Context context)
    {
        list=news;
        _context=context;

    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getNewsType();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(_context);
        switch (viewType) {
            case 0:
                return new ViewHolder1(inflater.inflate(R.layout.listitem_type1, parent, false));
            case 1:
                return new ViewHolder2(inflater.inflate(R.layout.listitem_type2, parent, false));
            case 2:
                return new ViewHolder3(inflater.inflate(R.layout.listitem_type3, parent, false));
            case 3:
                return new ViewHolder4(inflater.inflate(R.layout.listitem_type4, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String title = list.get(position).getTitle();
        String publisher = list.get(position).getPublisher();
        Date time = list.get(position).getPublishtime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String info = publisher +"  "+df.format(time)+ " " ;
        List<String> pic=list.get(position).getImageURL();
        String vid=list.get(position).getVideoURL();
        long read=list.get(position).getReadtime().getTime();


        //如果单图文
        if (holder instanceof ViewHolder1) {
            ((ViewHolder1) holder).title.setText(title);
            if (read != 0) {
                ((ViewHolder1) holder).title.setTextColor(_context.getResources().getColor(R.color.read));
            }
            else
            {
                ((ViewHolder1) holder).title.setTextColor(_context.getResources().getColor(R.color.unread));
            }

            ((ViewHolder1) holder).title.setTextSize(20);
            ((ViewHolder1) holder).info.setText(info);
            ((ViewHolder1) holder).info.setTextSize(10);
            Log.d("URL:",pic.get(0));
            Glide.with(_context).load(pic.get(0))
                    .into(((ViewHolder1) holder).img);
            return;
        }

        if (holder instanceof ViewHolder2) {
            if (read != 0) {
                ((ViewHolder2) holder).title.setTextColor(_context.getResources().getColor(R.color.read));
            }
            else
            {
                ((ViewHolder2) holder).title.setTextColor(_context.getResources().getColor(R.color.unread));
            }
            ((ViewHolder2) holder).title.setText(title);
            ((ViewHolder2) holder).title.setTextSize(20);
            ((ViewHolder2) holder).info.setText(info);
            ((ViewHolder2) holder).info.setTextSize(8);
            Log.d("URL:",pic.get(0));
            Log.d("URL:",pic.get(1));
            Log.d("URL:",pic.get(2));
            Glide.with(_context).load(pic.get(0))
                    .into(((ViewHolder2) holder).img1);
            Glide.with(_context).load(pic.get(1))
                    .into(((ViewHolder2) holder).img2);
            Glide.with(_context).load(pic.get(2))
                    .into(((ViewHolder2) holder).img3);
            return;
        }
        if (holder instanceof ViewHolder3) {
            if (read != 0) {
                ((ViewHolder3) holder).title.setTextColor(_context.getResources().getColor(R.color.read));
            }
            else
            {
                ((ViewHolder3) holder).title.setTextColor(_context.getResources().getColor(R.color.unread));
            }
            ((ViewHolder3) holder).title.setText(title);
            ((ViewHolder3) holder).title.setTextSize(20);
            ((ViewHolder3) holder).info.setText(info);
            ((ViewHolder3) holder).info.setTextSize(8);
            return;
        }
        if (holder instanceof ViewHolder4) {
            info+=" 3";
            if (read != 0) {
                ((ViewHolder4) holder).title.setTextColor(_context.getResources().getColor(R.color.read));
            }
            else
            {
                ((ViewHolder4) holder).title.setTextColor(_context.getResources().getColor(R.color.unread));
            }
            ((ViewHolder4) holder).title.setText(title);
            ((ViewHolder4) holder).title.setTextSize(20);
            ((ViewHolder4) holder).info.setText(info);
            ((ViewHolder4) holder).info.setTextSize(8);
            ((ViewHolder4) holder).video.setUp(vid,JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"视频");
            ((ViewHolder4) holder).video.startVideo();
            return;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class ViewHolder1 extends RecyclerView.ViewHolder {
    TextView title;//标题
    TextView info;//多图文模式的新闻作者
    ImageView img;

    ViewHolder1(View itemView) {
        super(itemView);
        img=itemView.findViewById(R.id.img_01);
        title =  itemView.findViewById(R.id.title_01);
        info = itemView.findViewById(R.id.info_01);
    }
}


class ViewHolder2 extends RecyclerView.ViewHolder {
    TextView title;//标题
    ImageView img1;
    ImageView img2;
    ImageView img3;
    TextView info;//多图文模式的新闻作者

    ViewHolder2(View itemView) {
        super(itemView);
        title =  itemView.findViewById(R.id.title_02);
        img1 =  itemView.findViewById(R.id.img1_02);
        img2 =  itemView.findViewById(R.id.img2_02);
        img3 =  itemView.findViewById(R.id.img3_02);
        info = itemView.findViewById(R.id.info_02);
    }
}

class ViewHolder3 extends RecyclerView.ViewHolder {
    TextView title;//标题
    TextView info;//多图文模式的新闻作者

    ViewHolder3(View itemView) {
        super(itemView);
        title =  itemView.findViewById(R.id.title_03);
        info = itemView.findViewById(R.id.info_03);
    }
}

class ViewHolder4 extends RecyclerView.ViewHolder {
    TextView title;//标题
    JCVideoPlayerStandard video;
    TextView info;//多图文模式的新闻作者

   ViewHolder4(View itemView) {
        super(itemView);
        title =  itemView.findViewById(R.id.title_04);
        video= itemView.findViewById(R.id.playerstandard);
        info = itemView.findViewById(R.id.info_04);
    }
}