package com.example.panjunchen;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.panjunchen.models.News;

import java.util.Date;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
        String info = publisher + time.toString();

        //如果单图文
        if (holder instanceof ViewHolder1) {

            ((ViewHolder1) holder).title.setText(title);
            ((ViewHolder1) holder).info.setText(info);
            ((ViewHolder1) holder).img.setImageURI();
            return;
        }

        if (holder instanceof ViewHolder2) {

            ((ViewHolder2) holder).title.setText(title);
            ((ViewHolder2) holder).info.setText(info);
            ((ViewHolder2) holder).img1.setImageURI();
            ((ViewHolder2) holder).img2.setImageURI();
            ((ViewHolder2) holder).img3.setImageURI();

            return;
        }
        if (holder instanceof ViewHolder3) {

            ((ViewHolder3) holder).title.setText(title);
            ((ViewHolder3) holder).info.setText(info);
            return;
        }
        if (holder instanceof ViewHolder4) {

            ((ViewHolder4) holder).title.setText(title);
            ((ViewHolder4) holder).info.setText(info);
            ((ViewHolder4) holder).video.setVideoURI();
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
    VideoView video;
    TextView info;//多图文模式的新闻作者

   ViewHolder4(View itemView) {
        super(itemView);
        title =  itemView.findViewById(R.id.title_04);
        video= itemView.findViewById(R.id.video_04);
        info = itemView.findViewById(R.id.info_04);
    }
}