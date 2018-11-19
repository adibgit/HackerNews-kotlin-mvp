package com.adibsurani.hackernews.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
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
    DetailView,
    DetailPresenter.OnChildComment {

    @Inject
    lateinit var detailPresenter : DetailPresenter
    private lateinit var viewPagerAdapter : ViewPagerAdapter
    private var parentCommentList = ArrayList<Comment>()
    private lateinit var story : Story
    private val loadHandler = Handler()

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
        Log.d("CHILD COMMENTS ::", "$comment")
        val childCommentList = ArrayList<Comment>()
        childCommentList.add(comment)
        (viewPagerAdapter.getItem(0) as CommentFragment).setupChildComments(parentCommentList)
    }

    override fun getMoreComment(comment : Comment) {
        Log.d("CHILD COMMENTS ::", "$comment")
        val childCommentList = ArrayList<Comment>()
        childCommentList.add(comment)
    }

    private fun setupData() {
        val gson = Gson()
        val storyObj = intent.getStringExtra("story")
        story = gson.fromJson<Story>(storyObj, Story::class.java)
        Log.d("STORY ::", "$story")
    }

    private fun setupView() {
        text_title.text = story.title

        setupViewPager()
        setupClick()
    }

    private fun setupWebView() {
        layout_shimmer_news.startShimmerAnimation()

        webview_news.settings.javaScriptEnabled = true
        webview_news.settings.supportZoom()
        webview_news.loadUrl(story.url)
        webview_news.webViewClient =  (object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                webview_news.visibility = VISIBLE
            }

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
        viewpager.currentItem = 0
        viewPagerAdapter.initClickTab(NEWS,image_news,image_comment,layout_webview,viewpager)
        setupFragmentData()
    }

    private fun setupClick() {
        layout_tab_news.setOnClickListener {
            viewPagerAdapter.initClickTab(NEWS,image_news,image_comment,layout_webview,viewpager)
        }

        layout_tab_comment.setOnClickListener {
            viewPagerAdapter.initClickTab(COMMENT,image_news,image_comment,layout_webview,viewpager)
            viewpager.currentItem = 0

            loadHandler.postDelayed({
                (viewPagerAdapter.getItem(0) as CommentFragment).setupComments(parentCommentList)
            }, 1000)
        }
    }

    private fun setupFragmentData() {
        story.kids?.let {
            for (i in 0 until it.size) {
                val comment = it[i]
                detailPresenter.getComment(comment)
            }
        }
    }

    fun getChildCommentID(childCommentID : ArrayList<Int>) {
        for (i in 0 until childCommentID.size) {
            val childComment = childCommentID[i]
            detailPresenter.getChildComment(this,childComment)
        }
    }


}