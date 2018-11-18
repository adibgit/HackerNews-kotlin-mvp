package com.adibsurani.hackernews.view

import com.adibsurani.base.mvp.BaseView
import com.adibsurani.hackernews.model.Comment
import com.adibsurani.hackernews.model.Story

interface DetailView : BaseView {
    fun getComment(comment: Comment)
    fun getChildComment(comment: Comment)
}