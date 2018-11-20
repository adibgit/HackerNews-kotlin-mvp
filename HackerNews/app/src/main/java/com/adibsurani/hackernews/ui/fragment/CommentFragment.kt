package com.adibsurani.hackernews.ui.fragment

import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewTreeObserver
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
        setupLoad()
        setupRecycler()
    }

    override fun initData() {

    }

    fun setupChildComments(commentList: ArrayList<Comment>) {
        Log.e("CHILD COMMENTS ::", "$commentList")

        view?.let {
            commentAdapter = CommentAdapter(context!!, commentList)
            recycler_comment.adapter = commentAdapter
            doneLoad()
        }

    }

    private fun setupRecycler() {
        RVHelper.setupVertical(recycler_comment,context!!)
    }

    fun setupLoad() {
        view?.let {
            recycler_comment.visibility = GONE
            layout_shimmer_comment.visibility = VISIBLE
            layout_shimmer_comment.startShimmerAnimation()
        }
    }

    fun doneLoad() {
        view?.let {
            recycler_comment.visibility = VISIBLE
            layout_shimmer_comment.stopShimmerAnimation()
            layout_shimmer_comment.visibility = GONE
        }
    }

    override fun initBack() {
        activity!!.finish()
    }




}