package com.adibsurani.hackernews.ui.fragment

import android.os.Handler
import android.util.Log
import android.view.View.GONE
import com.adibsurani.base.mvp.BaseFragment
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.helper.RVHelper
import com.adibsurani.hackernews.model.Comment
import com.adibsurani.hackernews.presenter.DetailPresenter
import com.adibsurani.hackernews.ui.activity.DetailActivity
import com.adibsurani.hackernews.ui.adapter.CommentAdapter
import kotlinx.android.synthetic.main.fragment_comments.*
import org.jetbrains.anko.info

class CommentFragment :
    BaseFragment(),
    CommentAdapter.AddChildCommentListener {

    private lateinit var commentAdapter : CommentAdapter
    private lateinit var commentList : ArrayList<Comment>
    private var parentCommentList = ArrayList<Comment>()

    interface OnFragmentFetchChildComment {
        fun getFragmentFetchChildComment(childComments: ArrayList<Comment>)
    }

    override fun initLayout(): Int {
        return R.layout.fragment_comments
    }

    override fun initView() {
        layout_shimmer_comment.startShimmerAnimation()
        setupRecycler()
    }

    override fun initData() {

    }

    override fun setupChildRecycler(childComments: ArrayList<Comment>) {
        Log.d("CHILD RECYCLER ::", "$childComments")
        parentCommentList = childComments
        Log.d("CHILD PARETNSS ::", "$parentCommentList")
    }

    fun setupChildComments(commentList: ArrayList<Comment>) {
        commentAdapter.addChildComment(this,commentList)
    }

    fun setupComments(parentCommentList : ArrayList<Comment>) {
        Log.e("COMMENT LIST", "$parentCommentList")
        commentList = parentCommentList
        updateRecycler()
    }

    private fun setupRecycler() {
        RVHelper.setupVertical(recycler_comment,context!!)
    }

    private fun updateRecycler() {
        commentAdapter = CommentAdapter(context!!,commentList,{ partItem : Comment -> dataClicked(partItem) })
        recycler_comment.adapter = commentAdapter

        layout_shimmer_comment.stopShimmerAnimation()
        layout_shimmer_comment.visibility = GONE
    }

    private fun dataClicked(data : Comment) {
        (activity as DetailActivity).getChildCommentID(data.kids)
    }

    fun getFetchChild(onFragmentFetchChildComment: OnFragmentFetchChildComment,
                      data : Comment) {
        onFragmentFetchChildComment.getFragmentFetchChildComment(parentCommentList)
        (activity as DetailActivity).getChildCommentID(data.kids)
    }


}