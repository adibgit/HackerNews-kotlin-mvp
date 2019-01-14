package com.adibsurani.hackernews.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.adibsurani.hackernews.R;
import com.adibsurani.hackernews.helper.TimeAgoUtil;
import com.adibsurani.hackernews.helper.RVHelper;
import com.adibsurani.hackernews.networking.api.RestAPI;
import com.adibsurani.hackernews.networking.data.Comment;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.lzyzsd.randomcolor.RandomColor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private List<Comment> commentList;
    private Context context;
    private CommentListAdapter commentListAdapter;

    public CommentListAdapter(Context context) {
        this.context = context;
        commentList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comments, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Comment comment = commentList.get(position);
        RandomColor randomColor = new RandomColor();
        int color = randomColor.randomColor();
        holder.textTime.setText(TimeAgoUtil.getTimeAgo(comment.getTime()));
        holder.textAuthor.setText(comment.getAuthor());
        holder.childCommentView.setBackgroundColor(color);
        if (comment.getText() != null) {
            holder.textComment.setText(Html.fromHtml(comment.getText()));
        } else {
            ViewGroup.LayoutParams params = holder.layoutRoot.getLayoutParams();
            params.height = 0;
            holder.layoutRoot.setLayoutParams(params);
        }
        if (comment.getKids() != null) {
            if (comment.getKids().size() > 0) {
                if (comment.getKids().size() == 1) {
                    holder.textCommentCount.setText((Integer.toString(comment.getKids().size()) + " Comment"));
                } else {
                    holder.textCommentCount.setText((Integer.toString(comment.getKids().size()) + " Comments"));
                }
                holder.recyclerViewComment.setVisibility(View.GONE);
                holder.layoutExpandComment.setOnClickListener((v) -> {
                    if (holder.recyclerViewComment.getVisibility() == View.GONE) {
                        holder.recyclerViewComment.setVisibility(View.VISIBLE);
                        holder.childCommentView.setVisibility(View.VISIBLE);
                        holder.imageExpand.setVisibility(View.GONE);
                        holder.imageCollapse.setVisibility(View.VISIBLE);
                        setupAdapter(holder.recyclerViewComment);
                        for (int i = 0; i < comment.getKids().size(); i++) {
                            String commentID = Integer.toString(comment.getKids().get(i));
                            getComment(commentID);
                        }
                    } else {
                        holder.recyclerViewComment.setVisibility(View.GONE);
                        holder.childCommentView.setVisibility(View.GONE);
                        holder.imageExpand.setVisibility(View.VISIBLE);
                        holder.imageCollapse.setVisibility(View.GONE);
                        commentListAdapter.clearAdapter();
                    }
                });
            } else {
                holder.layoutExpandComment.setVisibility(View.GONE);
                holder.layoutNestComment.setVisibility(View.GONE);
            }
        } else {
            holder.layoutExpandComment.setVisibility(View.GONE);
            holder.layoutNestComment.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    private void getComment(String commentID) {
        AndroidNetworking
                .get(RestAPI.Companion.getComment(commentID))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Parent", Integer.toString(response.optInt("parent")));
                        Comment comment = new Comment(
                                response.optString("by"),
                                response.optInt("id"),
                                parseKids(response.optJSONArray("kids")),
                                response.optInt("parent"),
                                response.optString("text"),
                                response.optLong("time"),
                                response.optString("type")
                        );
                        commentListAdapter.addDataSource(comment);
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e("CHILD COMMENT KO", error.getErrorDetail());
                    }
                });
    }

    private ArrayList<Integer> parseKids(JSONArray kidArray) {
        ArrayList<Integer> kidsList = new ArrayList<>();
        if (kidArray != null) {
            for (int i = 0; i < kidArray.length(); ++i) {
                kidsList.add(kidArray.optInt(i));
            }
        }
        return kidsList;
    }

    public void setDataSource(List<Comment> commentList) {
        this.commentList.addAll(commentList);
        notifyDataSetChanged();
    }

    public void addDataSource(Comment comment) {
        this.commentList.add(comment);
        notifyDataSetChanged();
    }

    public void clearAdapter() {
        this.commentList.clear();
        notifyDataSetChanged();
    }

    public void setupAdapter(RecyclerView recyclerView) {
        RVHelper.Companion.setupVertical(recyclerView, context);
        commentListAdapter = new CommentListAdapter(context);
        recyclerView.setAdapter(commentListAdapter);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textTime, textComment, textAuthor, textCommentCount;
        private LinearLayout layoutExpandComment, layoutRoot, layoutNestComment;
        private RecyclerView recyclerViewComment;
        private View childCommentView;
        private ImageView imageCollapse, imageExpand;

        ViewHolder(View itemView) {
            super(itemView);
            textTime = itemView.findViewById(R.id.text_time);
            textComment = itemView.findViewById(R.id.text_comment);
            textAuthor = itemView.findViewById(R.id.text_author);
            textCommentCount = itemView.findViewById(R.id.text_comment_count);
            layoutExpandComment = itemView.findViewById(R.id.layout_expand_comment);
            layoutRoot = itemView.findViewById(R.id.layout_root);
            layoutNestComment = itemView.findViewById(R.id.layout_nest_comment);
            recyclerViewComment = itemView.findViewById(R.id.recycler_child_comment);
            childCommentView = itemView.findViewById(R.id.line_comment);
            imageCollapse = itemView.findViewById(R.id.image_collapse);
            imageExpand = itemView.findViewById(R.id.image_expand);
        }
    }
}
