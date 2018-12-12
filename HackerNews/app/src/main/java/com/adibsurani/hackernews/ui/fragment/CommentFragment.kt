package com.adibsurani.hackernews.ui.fragment

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.controller.contract.CommentContract
import com.adibsurani.hackernews.dagger.component.DaggerFragmentComponent
import com.adibsurani.hackernews.dagger.module.FragmentModule
import com.adibsurani.hackernews.helper.Constants.Companion.CHILD
import com.adibsurani.hackernews.helper.Constants.Companion.PARENT
import com.adibsurani.hackernews.helper.view.RVHelper
import com.adibsurani.hackernews.networking.data.Comment
import com.adibsurani.hackernews.ui.adapter.CommentAdapter
import com.adibsurani.hackernews.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_comments.*
import javax.inject.Inject

class CommentFragment :
    BaseFragment(),
    CommentContract.View {

    @Inject
    lateinit var commentPresenter: CommentContract.Presenter
    private lateinit var commentAdapter : CommentAdapter
    private var parentKidList = ArrayList<Int>()
    private var parentCommentList = ArrayList<Comment>()
    private var childCommentList = ArrayList<Comment>()
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

    override fun getCommentSuccess(comment: Comment,
                                   commentType: Int) {

        when (commentType) {
            PARENT -> {
                parentCommentList.add(comment)
                Log.e("ParentComment size", "${parentCommentList.size}")
                Log.e("KidList size", "${parentKidList.size}")

                if (parentCommentList.size == parentKidList.size) {
                    for (parent in parentCommentList) {
                        passCommentList(parent)
                    }
                }
            }
            CHILD -> {
                commentAdapter.ViewHolder(context!!, view!!).addChildComment(comment)
            }
        }
    }

    private fun passCommentList(commentWithChild : Comment) {
        childCommentList.add(commentWithChild)
        Log.e("CommentPassed Size", "${childCommentList.size}")
        Log.e("Parent Comment Size", "${parentKidList.size}")

        if (childCommentList.size == parentKidList.size) {
            setupChildComments(childCommentList)
        }
    }

    fun setupChildCommentRequest(commentID: Int, commentType: Int) {
        commentPresenter.getComment(commentID, commentType)
    }

    fun setupCommentRequest(kids: ArrayList<Int>) {
        view?.let {
            if (kids != null) {
                for (comment in kids) {
                    parentKidList.add(comment)
                    commentPresenter.getComment(comment, PARENT)
                }
            }
        } ?: kotlin.run {
            Log.e("CommentFragment", "null call")
        }
    }

    private fun setupChildComments(commentList: ArrayList<Comment>) {
        Log.e("COMMENTS ::", "$commentList")
        Log.e("COMMENTS SIZE ::", "${commentList.size}")

        commentAdapter = CommentAdapter(context!!,this, commentList)
        recycler_comment.adapter = commentAdapter
        doneLoad()
    }

    fun reloadComments() {
        view?.let {
            setupLoad()
            parentCommentList.clear()
            for (comment in parentKidList) {
                commentPresenter.getComment(comment, PARENT)
            }
        }
    }

    private fun setupRecycler() {
        RVHelper.setupVertical(recycler_comment,context!!)
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