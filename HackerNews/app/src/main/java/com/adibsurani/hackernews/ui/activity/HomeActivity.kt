package com.adibsurani.hackernews.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import com.adibsurani.base.mvp.BaseActivity
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.di.component.DaggerHomeActivityComponent
import com.adibsurani.hackernews.di.module.HomeActivityModule
import com.adibsurani.hackernews.helper.RVHelper
import com.adibsurani.hackernews.model.Story
import com.adibsurani.hackernews.presenter.HomePresenter
import com.adibsurani.hackernews.ui.adapter.HomeAdapter
import com.adibsurani.hackernews.view.HomeView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class HomeActivity :
    BaseActivity(),
    HomeView {

    @Inject
    lateinit var homePresenter : HomePresenter
    private lateinit var homeAdapter : HomeAdapter
    private var storyList = ArrayList<Story>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
        initClick()
        initRecycler()
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
        Log.d("TOP STORY ID ::", "$storiesID")
        getTopStory(storiesID)
    }

    override fun getStory(story: Story) {
        info { story }
        storyList.add(story)
        homeAdapter = HomeAdapter(this,storyList,{ partItem : Story -> dataClicked(partItem) })
        recycler_story.adapter = homeAdapter
        doneLoadStory()
    }

    private fun initView() {
        homePresenter.getStoriesID()
        layout_shimmer_story.startShimmerAnimation()
        image_refresh.bringToFront()
    }

    private fun initClick() {
        image_refresh.setOnClickListener {
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

    private fun initRecycler() {
        RVHelper.setupVertical(recycler_story,this)
    }

    private fun doneLoadStory() {
        layout_shimmer_story.stopShimmerAnimation()
        layout_shimmer_story.visibility = GONE
    }

    private fun dataClicked(data : Story) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("story",Gson().toJson(data))
        startActivity(intent)
    }

}