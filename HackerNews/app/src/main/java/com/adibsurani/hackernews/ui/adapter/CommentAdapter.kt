package com.adibsurani.hackernews.ui.adapter

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.helper.RVHelper
import com.adibsurani.hackernews.helper.TimeHelper
import com.adibsurani.hackernews.model.Comment
import com.adibsurani.hackernews.presenter.DetailPresenter
import com.adibsurani.hackernews.ui.fragment.CommentFragment
import kotlinx.android.synthetic.main.row_comments.view.*


class CommentAdapter (private var context: Context,
                      private var dataList: List<Comment>,
                      val clickListener : (Comment) -> Unit) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private lateinit var commentAdapter : CommentAdapter
    private lateinit var commentFragment: CommentFragment
    private val loadHandler = Handler()

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
        holder.bindItems(dataList[position],clickListener)
    }

    fun addChildComment(addChildCommentListener: AddChildCommentListener,
                        childComments : ArrayList<Comment>) {
        Log.d("CHILD COM ADAPTER ::", "$childComments")
        addChildCommentListener.setupChildRecycler(childComments)
    }

    interface AddChildCommentListener {
        fun setupChildRecycler(childComments : ArrayList<Comment>)
    }

    inner class ViewHolder(private val context: Context,
                           itemView: View
    ) : RecyclerView.ViewHolder(itemView) , CommentFragment.OnFragmentFetchChildComment {

        fun bindItems(data: Comment, clickListener: (Comment) -> Unit) {

            itemView.text_time?.text = TimeHelper.getTimeAgo(data.time)

            data.text?.let {
                itemView.text_comment?.text = Html.fromHtml(it).toString()
            }

            itemView.text_author?.text = data.author

            data.kids?.let {
                itemView.text_comment_count?.text = "${it.size}" + " Comments"
            }

            itemView.layout_expand_comment.setOnClickListener {
//                clickListener(data)
                loadHandler.postDelayed({
                    commentFragment = CommentFragment()
                    commentFragment.getFetchChild(this,data)
                }, 500)
            }
        }

        override fun getFragmentFetchChildComment(childComments: ArrayList<Comment>) {
            Log.d("CHILD FETCH ::", "$childComments")
            RVHelper.setupVertical(itemView.recycler_child_comment,context)
            commentAdapter = CommentAdapter(context,childComments,{ partItem : Comment -> dataClicked(partItem) })
            itemView.recycler_child_comment.adapter = commentAdapter
        }

        private fun dataClicked(data : Comment) {
//            clickListener(data)
            loadHandler.postDelayed({
                commentFragment = CommentFragment()
                commentFragment.getFetchChild(this,data)
            }, 500)
        }

    }
}