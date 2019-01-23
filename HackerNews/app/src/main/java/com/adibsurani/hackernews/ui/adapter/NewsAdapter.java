package com.adibsurani.hackernews.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.adibsurani.hackernews.local.realm.KidsRealm;
import com.adibsurani.hackernews.local.realm.RealmController;
import com.adibsurani.hackernews.local.realm.StoryRealm;
import com.adibsurani.hackernews.networking.data.Story;
import com.adibsurani.hackernews.local.LocalDB;
import com.adibsurani.hackernews.ui.activity.HomeActivity;
import com.squareup.picasso.Picasso;
import io.github.ponnamkarthik.richlinkpreview.*;
import io.realm.Realm;
import io.realm.RealmList;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<Story> storyList;
    private Context context;
    private HomeActivity activity;
    private LocalDB localDB;
    private Realm realm;

    public NewsAdapter(Context context, HomeActivity activity) {
        this.context = context;
        this.activity = (HomeActivity) context;
        this.localDB = new LocalDB(context);
        this.storyList = new ArrayList<>();
        this.realm = RealmController.with(activity).getRealm();
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
        Log.e("STORY", position + " :" + story + "");

        // populate
        holder.textTitle.setText(story.getTitle());
        holder.textTime.setText(TimeAgoUtil.getTimeAgo(story.getTime()));
        String score = Integer.toString(story.getScore());
        holder.textScoreCount.setText(score);
        if (story.getUrl() != null) {
            String URL = Util.Companion.getHostName(story.getUrl());
            holder.textSource.setText(URL);
        }
        holder.imageBookmark.bringToFront();

        // color score if reach 100
        if(story.getScore() > 100) {
            holder.imageScore.setColorFilter(context.getResources().getColor(R.color.red_600));
        } else {
            holder.imageScore.setColorFilter(context.getResources().getColor(R.color.grey_800));
        }

        // populate comment if got kids
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

        // if url is valid and can preview thumbnail
        if (story.getUrl() != null && story.getUrl().length() != 0) {
            if (!story.getUrl().isEmpty() || !story.getUrl().equals("") || !StringUtil.isBlank(story.getUrl())) {
                if (URLUtil.isValidUrl(story.getUrl())) {
                    if (!story.getUrl().contains(".pdf")) {
                        Log.e("story URL", position + ": " + story.getUrl());
                        RichPreview richPreview;
                        richPreview = new RichPreview(new ResponseListener() {
                            @Override
                            public void onData(MetaData metaData) {
                                if (metaData != null) {
                                    Log.e("meta", position + ": " + metaData + "");
                                    if (metaData.getImageurl() != null) {
                                        if (!metaData.getImageurl().isEmpty() || !metaData.getImageurl().equals("")) {
                                            if (metaData.getImageurl().contains(".png") || metaData.getImageurl().contains(".jpg")) {
                                                Log.e("story THUMBNAIL", metaData.getImageurl() + ", " + position);
                                                if (position < 25) {
                                                    Picasso
                                                            .get()
                                                            .load(metaData.getImageurl())
                                                            .into(holder.imagePreview);
                                                    holder.imagePreview.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("story THUMBNAIL E", e.getMessage());
                            }
                        });
                        richPreview.getPreview(story.getUrl());
                    }
                }
            }
        } else {
            ViewGroup.LayoutParams params = holder.layoutRoot.getLayoutParams();
            params.height = 0;
            holder.layoutRoot.setLayoutParams(params);
        }

        // realm
        StoryRealm storyObject = realm
                .where(StoryRealm.class)
                .equalTo("id", story.getId())
                .findFirst();
        if (storyObject != null) {
            holder.imageBookmark.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_bookmark_fill));
            holder.imageBookmark.setColorFilter(context.getResources().getColor(R.color.orange_500), PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.imageBookmark.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_bookmark_blank));
            holder.imageBookmark.setColorFilter(context.getResources().getColor(R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
        }

        holder.imageBookmark.setOnClickListener((v) -> {
            Log.e("BOOKMARK clicked", "");

            StoryRealm storyRealm = realm
                    .where(StoryRealm.class)
                    .equalTo("id", story.getId())
                    .findFirst();
            if (storyRealm != null) {
                Log.e("BOOKMARK Removed", "");
                realm.beginTransaction();
                storyRealm.removeFromRealm();
                realm.commitTransaction();
                holder.imageBookmark.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_bookmark_blank));
                holder.imageBookmark.setColorFilter(context.getResources().getColor(R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
            } else {
                Log.e("BOOKMARK Added", "");
                StoryRealm newStory = new StoryRealm();
                newStory.setId(story.getId());
                if (story.getAuthor() != null) {
                    newStory.setAuthor(story.getAuthor());
                }
                if (story.getDescendants() != null) {
                    newStory.setDescendants(story.getDescendants());
                }
                if (story.getKids() != null) {
                    RealmList<KidsRealm> kidList = new RealmList<>();
                    for (int i=0; i<story.getKids().size(); i++) {
                        int kid = story.getKids().get(i);
                        KidsRealm kidsRealm = new KidsRealm();
                        kidsRealm.setKid(kid);
                    }
                    newStory.setKids(kidList);
                }
                if (story.getTitle() != null) {
                    newStory.setTitle(story.getTitle());
                }
                if (story.getType() != null) {
                    newStory.setType(story.getType());
                }
                if (story.getUrl() != null) {
                    newStory.setUrl(story.getUrl());
                }
                newStory.setScore(story.getScore());
                newStory.setTime(story.getTime());
                realm.beginTransaction();
                realm.copyToRealm(newStory);
                realm.commitTransaction();
                holder.imageBookmark.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_bookmark_fill));
                holder.imageBookmark.setColorFilter(context.getResources().getColor(R.color.orange_500), PorterDuff.Mode.SRC_ATOP);
            }
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textTitle, textTime, textSource, textScoreCount, textCommentCount;
        private ImageView imageBookmark, imageScore, imagePreview, imageComment;
        private LinearLayout layoutRoot;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
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
            imageBookmark.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Story story = storyList.get(getAdapterPosition());
            if (v == imageBookmark) {

            } else {
                activity.dataClicked(story);
            }
        }
    }
}
