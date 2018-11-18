package com.adibsurani.hackernews.ui.fragment

import com.adibsurani.base.mvp.BaseFragment
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.model.Comment
import com.adibsurani.hackernews.ui.activity.DetailActivity

class CommentFragment :
    BaseFragment(),
    DetailActivity.OnGetMoreComments {


    override fun initLayout(): Int {
        return R.layout.fragment_news
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun getMoreComment(commentList: ArrayList<Comment>) {

    }

    fun setupComments(parentCommentList : ArrayList<Comment>) {

    }



}