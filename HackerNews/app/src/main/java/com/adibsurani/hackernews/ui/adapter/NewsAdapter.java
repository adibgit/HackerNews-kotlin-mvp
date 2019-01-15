package com.adibsurani.hackernews.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.github.lzyzsd.randomcolor.RandomColor;
import com.squareup.picasso.Picasso;
import io.github.ponnamkarthik.richlinkpreview.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
        if(story.getScore() > 100) {
            holder.imageScore.setColorFilter(context.getResources().getColor(R.color.red_600));
        } else {
            holder.imageScore.setColorFilter(context.getResources().getColor(R.color.grey_800));
        }
        if (story.getKids() != null) {
            String kids = Integer.toString(story.getKids().size());
            holder.textCommentCount.setText(kids);
            if(story.getKids().size() > 20) {
                holder.imageComment.setColorFilter(context.getResources().getColor(R.color.red_600));
            } else {
                holder.imageComment.setColorFilter(context.getResources().getColor(R.color.grey_800));
            }
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
        holder.imageBookmark.setOnClickListener((v) -> {

        });
        if (story.getUrl() != null) {
            if (URLUtil.isValidUrl(story.getUrl())) {
                if (!story.getUrl().contains(".pdf")) {
                    RichPreview richPreview;
                    richPreview = new RichPreview(new ResponseListener() {
                        @Override
                        public void onData(MetaData metaData) {
                            Log.e("RICH", metaData.getImageurl());
                            if (metaData.getImageurl() != null) {
                                if (!metaData.getImageurl().isEmpty() || !metaData.getImageurl().equals("")) {
                                    Picasso
                                            .get()
                                            .load(metaData.getImageurl())
                                            .into(holder.imagePreview);
                                    holder.imagePreview.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        @Override
                        public void onError(Exception e) {
                            //handle error
                        }
                    });
                    richPreview.getPreview(story.getUrl());
                }
            }
        } else {
            ViewGroup.LayoutParams params = holder.layoutRoot.getLayoutParams();
            params.height = 0;
            holder.layoutRoot.setLayoutParams(params);
        }
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
        private ImageView imageBookmark, imageScore, imagePreview, imageComment;
        private LinearLayout layoutRoot;
        private RichLinkViewSkype layoutPreview;

        ViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            textTime = itemView.findViewById(R.id.text_time);
            textSource = itemView.findViewById(R.id.text_source);
            textScoreCount = itemView.findViewById(R.id.text_point);
            textCommentCount = itemView.findViewById(R.id.text_comment);
            imageBookmark = itemView.findViewById(R.id.img_bookmark);
            imageScore = itemView.findViewById(R.id.img_score);
            imagePreview = itemView.findViewById(R.id.img_preview);
            imageComment = itemView.findViewById(R.id.img_comment);
            layoutRoot = itemView.findViewById(R.id.layout_root);
            layoutPreview = itemView.findViewById(R.id.layout_preview);
        }
    }
}
