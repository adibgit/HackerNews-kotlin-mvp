package com.adibsurani.hackernews.ui.activity

import android.content.Intent
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.controller.contract.HomeContract
import com.adibsurani.hackernews.dagger.component.DaggerActivityComponent
import com.adibsurani.hackernews.dagger.module.ActivityModule
import com.adibsurani.hackernews.helper.view.RVHelper
import com.adibsurani.hackernews.networking.data.Story
import com.adibsurani.hackernews.ui.adapter.NewsAdapter
import com.adibsurani.hackernews.ui.adapter.StoryTypeAdapter
import com.adibsurani.hackernews.ui.base.BaseActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity :
    BaseActivity(),
    HomeContract.View {

    @Inject
    lateinit var homePresenter : HomeContract.Presenter
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var storyTypeAdapter: StoryTypeAdapter
    private var storyList = ArrayList<Story>()
    private var typeList = ArrayList<String>()

    override fun initLayout(): Int {
        return R.layout.activity_home
    }

    override fun initInject() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()
        activityComponent.inject(this)
        homePresenter.attach(this)
    }

    override fun initCreate() {
        initView()
        initClick()
        initRecycler()
        initTypeList()
    }

    override fun showProgress(show: Boolean) {}

    override fun showErrorMessage(error: String) {
        Log.e("HomePresenter E",error)
    }

    override fun getStoriesIDSuccess(storiesID: ArrayList<Int>) {
        Log.e("StoriesID OK", "$storiesID")
        for (i in 0 until 25) {
            val story = storiesID[i]
            homePresenter.getStory(story)
        }
    }

    override fun getStorySuccess(story: Story) {
        storyList.add(story)
        storyList.sortBy {
            it.score
        }
        storyList.reverse()
        if (storyList.size == 25) {
            newsAdapter.setDataSource(storyList)
            stopShimmer()
        }
    }

    private fun initView() {
        homePresenter.getStoriesID()
        startShimmer()
    }

    private fun initClick() {
        image_refresh.setOnClickListener {
            storyList.clear()
            newsAdapter.clearAdapter()
            homePresenter.getStoriesID()
            startShimmer()
        }
    }

    private fun initRecycler() {
        RVHelper.setupVertical(recycler_story,this)
        newsAdapter = NewsAdapter(this,this)
        recycler_story.adapter = newsAdapter

        RVHelper.setupHorizontal(recycler_type, this)
        storyTypeAdapter = StoryTypeAdapter(this,this)
        recycler_type.adapter = storyTypeAdapter
    }

    private fun initTypeList() {
        typeList.add("Top")
        typeList.add("Best")
        typeList.add("New")
        storyTypeAdapter.setDataSource(typeList)
    }

    private fun startShimmer() {
        recycler_story.visibility = GONE
        layout_shimmer_story.visibility = VISIBLE
        layout_shimmer_story.startShimmer()
        image_refresh.bringToFront()
    }

    private fun stopShimmer() {
        layout_shimmer_story.stopShimmer()
        layout_shimmer_story.visibility = GONE
        recycler_story.visibility = VISIBLE
    }

    fun dataClicked(data : Story) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("story",Gson().toJson(data))
        startActivity(intent)
    }

    fun typeClicked(position: Int) {
        when (position) {
            0 -> { }
            1 -> { }
            2 -> { }
        }
    }

}