package com.adibsurani.hackernews.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.helper.RVHelper
import com.adibsurani.hackernews.helper.TimeHelper
import com.adibsurani.hackernews.model.Comment
import kotlinx.android.synthetic.main.row_comments.view.*


class CommentAdapter (private var context: Context,
                      private var dataList: List<Comment>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>(){

    private lateinit var commentAdapter : CommentAdapter
    private lateinit var childCommentAdapter : ChildCommentAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_comments, parent, false)
        return ViewHolder(context, itemView)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(dataList[position])
    }

    inner class ViewHolder(private val context: Context, itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        fun bindItems(data: Comment) {

            itemView.text_time?.text = TimeHelper.getTimeAgo(data.time)

            data.text?.let {
                itemView.text_comment?.text = Html.fromHtml(it).toString()
            }

            itemView.text_author?.text = data.author

            if (data.kids != null) {
                itemView.text_comment_count?.text = "${data.kids.size}" + " Comments"

                Log.e("RECYCLER CHILD", "${data.comment}")

                childCommentAdapter = ChildCommentAdapter(context,data.comment)
                RVHelper.setupVertical(itemView.recycler_child_comment,context)
                itemView.recycler_child_comment.adapter = childCommentAdapter

            } else {
                itemView.layout_expand_comment.visibility = GONE
            }

            itemView.layout_expand_comment.setOnClickListener {
                when (itemView.image_collapse.visibility) {
                    VISIBLE -> {
                        itemView.image_expand.visibility = VISIBLE
                        itemView.image_collapse.visibility = GONE
                        itemView.recycler_child_comment.visibility = GONE
                    }
                    GONE -> {
                        itemView.image_expand.visibility = GONE
                        itemView.image_collapse.visibility = VISIBLE
                        itemView.recycler_child_comment.visibility = VISIBLE
                    }
                }
            }


        }

    }
}