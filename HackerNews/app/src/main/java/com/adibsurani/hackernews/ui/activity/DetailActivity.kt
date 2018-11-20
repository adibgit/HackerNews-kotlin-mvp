package com.adibsurani.hackernews.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.View.*
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
    private var parentCommentList = ArrayList<Comment>()
    private var childCommentList = ArrayList<Comment>()
    private var commentList = ArrayList<Comment>()
    private lateinit var story : Story
    private val loadHandler = Handler()
    private var clickCountComment : Int = 0

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
        parentCommentList.add(comment)

        for (i in 0 until parentCommentList.size) {
            val parentComment = parentCommentList[i]
            parentComment.kids?.let {
                for (j in 0 until parentComment.kids.size) {
                    val commentID = parentComment.kids[j]
                    detailPresenter.getChildComment(commentID)
                }
            }
        }
    }

    override fun getChildComment(comment: Comment) {

        for (i in 0 until parentCommentList.size) {
            val parentComment = parentCommentList[i]

            parentComment.kids?.let {
                Log.e("PARENT KIDS::", "${parentComment.kids}")
                if (comment.parent == parentComment.id) {
                    commentList.add(comment)
                    parentComment.comment = commentList

                    childCommentList.add(parentComment)
                }
            }
        }

        (viewPagerAdapter.getItem(0) as CommentFragment).setupChildComments(childCommentList)

    }

    private fun setupData() {
        val gson = Gson()
        val storyObj = intent.getStringExtra("story")
        story = gson.fromJson<Story>(storyObj, Story::class.java)
        Log.d("STORY ::", "$story")
    }

    private fun setupView() {
        text_title.text = story.title
        image_refresh.bringToFront()

        setupViewPager()
        setupClick()
    }

    private fun setupWebView() {
        progress_web.visibility = VISIBLE
        progress_web.bringToFront()
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
                progress_web.visibility = GONE
            }
        })
    }

    private fun reloadWebView() {
        webview_news.reload()
        setupWebView()
    }

    private fun setupViewPager() {
        viewPagerAdapter = ViewPagerAdapter(this,supportFragmentManager)
        viewPagerAdapter.addFragment(CommentFragment(), "Comments")
        viewpager.adapter = viewPagerAdapter
        viewpager.offscreenPageLimit = 1
        viewpager.currentItem = 0
        viewPagerAdapter.initClickTab(NEWS,image_news,image_comment,layout_webview,viewpager)

    }

    private fun setupClick() {
        layout_tab_news.setOnClickListener {
            viewPagerAdapter.initClickTab(NEWS,image_news,image_comment,layout_webview,viewpager)
        }

        layout_tab_comment.setOnClickListener {
            viewPagerAdapter.initClickTab(COMMENT,image_news,image_comment,layout_webview,viewpager)
            viewpager.currentItem = 0

            if (clickCountComment == 0) {
                clickCountComment = 1
                setupFragmentData()
            }
        }

        image_refresh.setOnClickListener {
            parentCommentList.clear()
            setupFragmentData()
            (viewPagerAdapter.getItem(0) as CommentFragment).setupLoad()
            reloadWebView()
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

}