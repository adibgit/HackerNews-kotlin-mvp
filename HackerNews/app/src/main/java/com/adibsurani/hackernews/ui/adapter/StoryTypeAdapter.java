package com.adibsurani.hackernews.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.adibsurani.hackernews.R;
import com.adibsurani.hackernews.ui.activity.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class StoryTypeAdapter extends RecyclerView.Adapter<StoryTypeAdapter.ViewHolder> {

    private List<String> typeList;
    private Context context;
    private HomeActivity activity;
    private int selectedPosition = 0;

    public StoryTypeAdapter(Context context, HomeActivity activity) {
        this.context = context;
        this.activity = (HomeActivity) context;
        typeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_story_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String type = typeList.get(position);
        holder.textType.setText(type.toUpperCase());
        holder.layoutRoot.setBackgroundColor(selectedPosition == position? context.getColor(R.color.blue_800) : Color.WHITE);
        holder.textType.setTextColor(selectedPosition == position? Color.WHITE : Color.BLACK);
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

    public void setDataSource(List<String> typeList) {
        this.typeList.addAll(typeList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textType;
        private LinearLayout layoutRoot;

        ViewHolder(View itemView) {
            super(itemView);
            textType = itemView.findViewById(R.id.text_type);
            layoutRoot = itemView.findViewById(R.id.layout_root);

            itemView.setOnClickListener((v) -> {
                if (getAdapterPosition() == RecyclerView.NO_POSITION) {
                    return;
                } else {
                    notifyItemChanged(selectedPosition);
                    selectedPosition = getAdapterPosition();
                    notifyItemChanged(selectedPosition);
                    activity.typeClicked(getAdapterPosition());
                }
            });
        }
    }
}