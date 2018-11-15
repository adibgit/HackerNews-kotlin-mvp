package com.adibsurani.hackernews.view

import com.adibsurani.base.mvp.BaseView
import com.adibsurani.hackernews.model.Story

interface HomeView : BaseView {
    fun getStoriesID(storiesID : ArrayList<Int>)
    fun getStory(story: Story)
}