package com.adibsurani.hackernews.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.helper.Constants.Companion.CHILD
import com.adibsurani.hackernews.helper.view.RVHelper
import com.adibsurani.hackernews.helper.TimeAgoUtil
import com.adibsurani.hackernews.networking.data.Comment
import com.adibsurani.hackernews.ui.fragment.CommentFragment
import kotlinx.android.synthetic.main.row_comments.view.*


class CommentAdapter (private var context: Context,
                      private var fragment: CommentFragment,
                      private var dataList: List<Comment>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>(){

    private lateinit var commentAdapter : CommentAdapter
    private lateinit var childCommentAdapter : ChildCommentAdapter
    private var commentList = ArrayList<Comment>()


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

            itemView.text_time?.text = TimeAgoUtil.getTimeAgo(data.time)

            data.text?.let {
                itemView.text_comment?.text = Html.fromHtml(it).toString()
            }

            itemView.text_author?.text = data.author

            if (data.kids != null) {
                itemView.text_comment_count?.text = "${data.kids.size}" + " Comments"
                RVHelper.setupVertical(itemView.recycler_child_comment, context)
                for (comment in data.kids) {
                    fragment.setupChildCommentRequest(comment, CHILD)
                }

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

        fun addChildComment(comment: Comment) {
            commentList.add(comment)
            //if (commentList.size == dataList[position].kids.size) {
                childCommentAdapter = ChildCommentAdapter(context, commentList)
                itemView.recycler_child_comment.adapter = childCommentAdapter
            //}
        }

    }
}