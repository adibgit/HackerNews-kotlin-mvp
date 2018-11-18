package com.adibsurani.hackernews.ui.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebView
import android.webkit.WebViewClient
import com.adibsurani.base.mvp.BaseActivity
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.di.component.DaggerDetailActivityComponent
import com.adibsurani.hackernews.di.module.DetailActivityModule
import com.adibsurani.hackernews.helper.Constants.Companion.COMMENT
import com.adibsurani.hackernews.helper.Constants.Companion.NEWS
import com.adibsurani.hackernews.model.Comment
import com.adibsurani.hackernews.model.Story
import com.adibsurani.hackernews.presenter.DetailPresenter
import com.adibsurani.hackernews.ui.adapter.ViewPagerAdapter
import com.adibsurani.hackernews.ui.fragment.CommentFragment
import com.adibsurani.hackernews.view.DetailView
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject
import com.google.gson.Gson




class DetailActivity :
    BaseActivity(),
    DetailView {

    @Inject
    lateinit var detailPresenter : DetailPresenter
    private lateinit var viewPagerAdapter : ViewPagerAdapter
    private lateinit var onGetMoreComments : OnGetMoreComments
    private var parentCommentList = ArrayList<Comment>()
    private lateinit var story : Story

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setupData()
        setupWebView()
        setupView()
    }

    override fun onActivityInject() {
        DaggerDetailActivityComponent
            .builder()
            .appComponent(getAppcomponent())
            .detailActivityModule(DetailActivityModule())
            .build()
            .inject(this)

        detailPresenter.attachView(this)
    }

    override fun getComment(comment: Comment) {
        Log.d("NEWS COMMENT ::", "$comment")
        parentCommentList.add(comment)
    }

    override fun getChildComment(comment: Comment) {
        val childCommentList = ArrayList<Comment>()
        childCommentList.add(comment)
        onGetMoreComments.getMoreComment(childCommentList)
    }

    private fun setupData() {
        val gson = Gson()
        val storyObj = intent.getStringExtra("story")
        story = gson.fromJson<Story>(storyObj, Story::class.java)
        Log.d("STORY ::", "$story")
    }

    private fun setupView() {
        setupViewPager()
        setupClick()
    }

    private fun setupWebView() {
        layout_shimmer_news.startShimmerAnimation()

        webview_news.settings.javaScriptEnabled = true
        webview_news.settings.supportZoom()
        webview_news.loadUrl(story.url)
        webview_news.webViewClient =  (object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                webview_news.visibility = VISIBLE
                layout_shimmer_news.stopShimmerAnimation()
                layout_shimmer_news.visibility = GONE
            }
        })
    }

    private fun setupViewPager() {
        viewPagerAdapter = ViewPagerAdapter(this,supportFragmentManager)
        viewPagerAdapter.addFragment(CommentFragment(), "Comments")
        viewpager.adapter = viewPagerAdapter
        viewpager.offscreenPageLimit = 1
        viewPagerAdapter.initClickTab(NEWS,image_news,image_comment,layout_webview,viewpager)
        setupFragmentData()
    }

    private fun setupClick() {
        layout_tab_news.setOnClickListener {
            viewPagerAdapter.initClickTab(NEWS,image_news,image_comment,layout_webview,viewpager)
        }

        layout_tab_comment.setOnClickListener {
            viewPagerAdapter.initClickTab(COMMENT,image_news,image_comment,layout_webview,viewpager)
            viewpager.currentItem = 1
        }
    }

    private fun setupFragmentData() {

        for (i in 0 until story.kids.size) {
            val comment = story.kids[i]
            detailPresenter.getComment(comment)
        }
        (viewPagerAdapter.getItem(0) as CommentFragment).setupComments(parentCommentList)
    }

    fun getChildComment(childCommentID : ArrayList<Int>) {
        for (i in 0 until childCommentID.size) {
            val childComment = childCommentID[i]
            detailPresenter.getComment(childComment)
        }
    }

    interface OnGetMoreComments {
        fun getMoreComment(commentList : ArrayList<Comment>)
    }


}