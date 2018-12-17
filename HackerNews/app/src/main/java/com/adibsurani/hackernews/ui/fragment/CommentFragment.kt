package com.adibsurani.hackernews.ui.fragment

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.controller.contract.CommentContract
import com.adibsurani.hackernews.dagger.component.DaggerFragmentComponent
import com.adibsurani.hackernews.dagger.module.FragmentModule
import com.adibsurani.hackernews.helper.view.RVHelper
import com.adibsurani.hackernews.networking.data.Comment
import com.adibsurani.hackernews.ui.adapter.CommentListAdapter
import com.adibsurani.hackernews.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_comments.*
import javax.inject.Inject

class CommentFragment :
    BaseFragment(),
    CommentContract.View {

    @Inject
    lateinit var commentPresenter: CommentContract.Presenter
    private lateinit var commentListAdapter: CommentListAdapter
    private var kidsCount: Int = 0
    private var kidsList =  ArrayList<Int>()
    private var commentList = ArrayList<Comment>()

    override fun initLayout(): Int {
        return R.layout.fragment_comments
    }

    override fun initInject() {
        val commentComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()
        commentComponent.inject(this)
    }

    override fun initPresenter() {
        commentPresenter.attach(this)
        commentPresenter.subscribe()
    }

    override fun initView() {
        setupLoad()
        setupRecycler()
    }

    override fun initData() {}

    override fun showErrorMessage(error: String) {
        Log.e("CommentPresenter E", error)
    }

    override fun getCommentSuccess(comment: Comment) {
        Log.e("CommentID ID", "${comment.id}")
        comment.kids?.let {
            Log.e("CommentID kids", "${comment.kids.size}")
        }

        commentList.add(comment)
        if (commentList.size == kidsCount) {
            commentListAdapter.setDataSource(commentList)
            doneLoad()
        }
    }

    fun setupCommentRequest(kids: ArrayList<Int>) {
        view?.let {
            if (kids != null) {
                kidsCount = kids.size
                kidsList = kids
                for (comment in kids) {
                    commentPresenter.getComment(comment)
                }
            }
        } ?: kotlin.run {
            Log.e("CommentFragment", "null call")
        }
    }

    fun reloadComments() {
        view?.let {
            setupLoad()
            commentListAdapter.clearAdapter()
            commentList.clear()
            for (comment in kidsList) {
                commentPresenter.getComment(comment)
            }
        }
    }

    private fun setupRecycler() {
        RVHelper.setupVertical(recycler_comment,context!!)
        commentListAdapter = CommentListAdapter(context)
        recycler_comment.adapter = commentListAdapter
    }

    private fun setupLoad() {
        view?.let {
            recycler_comment.visibility = GONE
            layout_shimmer_comment.visibility = VISIBLE
            layout_shimmer_comment.startShimmer()
        }
    }

    private fun doneLoad() {
        view?.let {
            recycler_comment.visibility = VISIBLE
            layout_shimmer_comment.stopShimmer()
            layout_shimmer_comment.visibility = GONE
        }
    }

}