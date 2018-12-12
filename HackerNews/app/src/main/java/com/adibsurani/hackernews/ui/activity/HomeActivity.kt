package com.adibsurani.hackernews.ui.activity

import android.content.Intent
import android.util.Log
import android.view.View.GONE
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.controller.contract.HomeContract
import com.adibsurani.hackernews.dagger.component.DaggerActivityComponent
import com.adibsurani.hackernews.dagger.module.ActivityModule
import com.adibsurani.hackernews.helper.view.RVHelper
import com.adibsurani.hackernews.networking.data.Story
import com.adibsurani.hackernews.ui.adapter.HomeAdapter
import com.adibsurani.hackernews.ui.base.BaseActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity :
    BaseActivity(),
    HomeContract.View {

    @Inject
    lateinit var homePresenter : HomeContract.Presenter
    private lateinit var homeAdapter : HomeAdapter
    private var storyList = ArrayList<Story>()

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
        homeAdapter = HomeAdapter(this,storyList,{ partItem : Story -> dataClicked(partItem) })
        recycler_story.adapter = homeAdapter
        stopShimmer()
    }

    private fun initView() {
        homePresenter.getStoriesID()
        startShimmer()
    }

    private fun initClick() {
        image_refresh.setOnClickListener {
            storyList.clear()
            homePresenter.getStoriesID()
        }
    }

    private fun initRecycler() {
        RVHelper.setupVertical(recycler_story,this)
    }

    private fun startShimmer() {
        layout_shimmer_story.startShimmer()
        image_refresh.bringToFront()
    }

    private fun stopShimmer() {
        layout_shimmer_story.stopShimmer()
        layout_shimmer_story.visibility = GONE
    }

    private fun dataClicked(data : Story) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("story",Gson().toJson(data))
        startActivity(intent)
    }



}