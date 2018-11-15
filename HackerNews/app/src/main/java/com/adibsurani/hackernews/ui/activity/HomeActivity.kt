package com.adibsurani.hackernews.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View.GONE
import com.adibsurani.base.mvp.BaseActivity
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.di.component.DaggerHomeActivityComponent
import com.adibsurani.hackernews.di.module.HomeActivityModule
import com.adibsurani.hackernews.model.Story
import com.adibsurani.hackernews.presenter.HomePresenter
import com.adibsurani.hackernews.ui.adapter.HomeAdapter
import com.adibsurani.hackernews.view.HomeView
import com.burakeregar.easiestgenericrecycleradapter.base.GenericAdapterBuilder
import com.burakeregar.easiestgenericrecycleradapter.base.GenericRecyclerAdapter
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.info
import javax.inject.Inject

class HomeActivity :
    BaseActivity(),
    HomeView {

    @Inject
    lateinit var homePresenter : HomePresenter
    private lateinit var homeAdapter : GenericRecyclerAdapter
    private var storyList = ArrayList<Story>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
        initAdapter()
    }

    override fun onActivityInject() {
        DaggerHomeActivityComponent
            .builder()
            .appComponent(getAppcomponent())
            .homeActivityModule(HomeActivityModule())
            .build()
            .inject(this)

        homePresenter.attachView(this)
    }

    override fun getStoriesID(storiesID: ArrayList<Int>) {
        info { storiesID }
        getTopStory(storiesID)
    }

    override fun getStory(story: Story) {
        info { story }
        storyList.add(story)
        homeAdapter.setList(storyList)
        doneLoadStory()
    }

    private fun initView() {
        homePresenter.getStoriesID()
        layout_shimmer_story.startShimmerAnimation()

        layout_swipe_refresh.setOnRefreshListener {
            storyList.clear()
            homePresenter.getStoriesID()
        }
    }

    private fun getTopStory(storyList : ArrayList<Int>) {
        for (i in 0 until 25) {
            val story = storyList[i]
            homePresenter.getStory(story)
        }
    }

    private fun initAdapter() {
        homeAdapter = GenericAdapterBuilder().addModel(
            R.layout.row_story,
            HomeAdapter::class.java,
            Story::class.java)
            .execute()
        recycler_story.layoutManager = LinearLayoutManager(this)
        recycler_story.adapter = homeAdapter
    }

    private fun doneLoadStory() {
        layout_shimmer_story.stopShimmerAnimation()
        layout_shimmer_story.visibility = GONE
        layout_swipe_refresh.isRefreshing = false
    }

}