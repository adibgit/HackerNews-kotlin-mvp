package com.adibsurani.hackernews.controller.contract

import com.adibsurani.hackernews.networking.data.Comment
import com.adibsurani.hackernews.ui.base.BaseContract

class CommentContract {

    interface View: BaseContract.View {
        fun showErrorMessage(error: String)
        fun getCommentSuccess(comment: Comment)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun getComment(commentID: Int)
    }

}