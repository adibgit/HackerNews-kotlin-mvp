package com.adibsurani.hackernews.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.adibsurani.hackernews.R;
import com.adibsurani.hackernews.helper.TimeAgoUtil;
import com.adibsurani.hackernews.helper.Util;
import com.adibsurani.hackernews.networking.data.Story;
import com.adibsurani.hackernews.ui.activity.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<Story> storyList;
    private Context context;
    private HomeActivity activity;

    public NewsAdapter(Context context, HomeActivity activity) {
        this.context = context;
        this.activity = (HomeActivity) context;
        storyList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Story story = storyList.get(position);
        holder.textTitle.setText(story.getTitle());
        holder.textTime.setText(TimeAgoUtil.getTimeAgo(story.getTime()));
        String score = Integer.toString(story.getScore());
        holder.textScoreCount.setText(score);
        if (story.getKids() != null) {
            String kids = Integer.toString(story.getKids().size());
            holder.textCommentCount.setText(kids);
        } else {
            holder.textCommentCount.setText("0");
        }
        if (story.getUrl() != null) {
            String URL = Util.Companion.getHostName(story.getUrl());
            holder.textSource.setText(URL);
        }
        holder.layoutRoot.setOnClickListener((v) -> {
            activity.dataClicked(story);
        });
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    public void setDataSource(List<Story> storyList) {
        this.storyList.addAll(storyList);
        notifyDataSetChanged();
    }

    public void addDataSource(Story story) {
        this.storyList.add(story);
        notifyDataSetChanged();
    }

    public void clearAdapter() {
        this.storyList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle, textTime, textSource, textScoreCount, textCommentCount;
        private ImageView imageBookmark;
        private LinearLayout layoutRoot;

        ViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            textTime = itemView.findViewById(R.id.text_time);
            textSource = itemView.findViewById(R.id.text_source);
            textScoreCount = itemView.findViewById(R.id.text_point);
            textCommentCount = itemView.findViewById(R.id.text_comment);
            imageBookmark = itemView.findViewById(R.id.img_bookmark);
            layoutRoot = itemView.findViewById(R.id.layout_root);
        }
    }
}
