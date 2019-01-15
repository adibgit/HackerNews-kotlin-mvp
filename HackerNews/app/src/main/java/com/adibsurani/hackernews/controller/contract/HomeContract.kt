package com.adibsurani.hackernews.controller.contract

import com.adibsurani.hackernews.networking.data.Comment
import com.adibsurani.hackernews.networking.data.Story
import com.adibsurani.hackernews.ui.base.BaseContract

class HomeContract {

    interface View: BaseContract.View {
        fun showProgress(show: Boolean)
        fun showErrorMessage(error: String)

        // stories
        fun getStoriesIDSuccess(storiesID: ArrayList<Int>)
        fun getStorySuccess(story: Story)

        // comment
        fun getCommentSuccess(comment: Comment)

    }

    interface Presenter: BaseContract.Presenter<View> {
        // stories
        fun getStoriesID()
        fun getStory(storyID: Int)

        //comment
        fun getComment(commentID: Int)
    }

}