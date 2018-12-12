package com.adibsurani.hackernews.controller.contract

import com.adibsurani.hackernews.networking.data.Story
import com.adibsurani.hackernews.ui.base.BaseContract

class HomeContract {

    interface View: BaseContract.View {
        fun showProgress(show: Boolean)
        fun showErrorMessage(error: String)
        fun getStoriesIDSuccess(storiesID: ArrayList<Int>)
        fun getStorySuccess(story: Story)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun getStoriesID()
        fun getStory(storyID: Int)
    }

}