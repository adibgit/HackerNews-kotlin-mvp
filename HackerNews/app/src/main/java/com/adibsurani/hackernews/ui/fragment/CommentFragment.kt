package com.adibsurani.hackernews.ui.fragment

import android.util.Log
import android.view.View.GONE
import com.adibsurani.base.mvp.BaseFragment
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.helper.RVHelper
import com.adibsurani.hackernews.model.Comment
import com.adibsurani.hackernews.ui.adapter.CommentAdapter
import kotlinx.android.synthetic.main.fragment_comments.*

class CommentFragment :
    BaseFragment() {

    private lateinit var commentAdapter : CommentAdapter

    override fun initLayout(): Int {
        return R.layout.fragment_comments
    }

    override fun initView() {
        layout_shimmer_comment.startShimmerAnimation()
        setupRecycler()
    }

    override fun initData() {

    }

    fun setupChildComments(commentList: ArrayList<Comment>) {
        Log.e("CHILD COMMENTS ::", "$commentList")
        commentAdapter = CommentAdapter(context!!,commentList)
        recycler_comment.adapter = commentAdapter

        layout_shimmer_comment.stopShimmerAnimation()
        layout_shimmer_comment.visibility = GONE
    }

    private fun setupRecycler() {
        RVHelper.setupVertical(recycler_comment,context!!)
    }

}