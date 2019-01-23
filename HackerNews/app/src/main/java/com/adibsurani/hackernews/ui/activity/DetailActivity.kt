package com.adibsurani.hackernews.ui.activity

import android.graphics.Bitmap
import android.os.Handler
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.helper.Constants.Companion.COMMENT
import com.adibsurani.hackernews.helper.Constants.Companion.NEWS
import com.adibsurani.hackernews.networking.data.Story
import com.adibsurani.hackernews.ui.adapter.ViewPagerAdapter
import com.adibsurani.hackernews.ui.base.BaseActivity
import com.adibsurani.hackernews.ui.fragment.CommentFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity :
    BaseActivity() {

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var story: Story
    private var clickCountComment: Int = 0
    private val loadHandler = Handler()

    override fun initLayout(): Int {
        return R.layout.activity_detail
    }

    override fun initInject() {}

    override fun initCreate() {
        setupData()
        setupWebView()
        setupView()
    }

    private fun setupData() {
        val gson = Gson()
        val storyObj = intent.getStringExtra("story")
        story = gson.fromJson<Story>(storyObj, Story::class.java)
        Log.d("StoriesID", "$story")
    }

    private fun setupView() {
        text_title.text = story.title
        image_refresh.bringToFront()
        setupViewPager()
        setupClick()
    }

    private fun setupWebView() {
        progressBar.visibility = VISIBLE
        progressBar.bringToFront()
        webview_news.settings.javaScriptEnabled = true
        webview_news.settings.supportZoom()
        webview_news.settings.builtInZoomControls = true
        webview_news.settings.displayZoomControls = false
        webview_news.clearSslPreferences()
        webview_news.clearCache(true)
        webview_news.loadUrl(story.url)
        webview_news.webChromeClient = (object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
            }
        })
        webview_news.webViewClient =  (object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                webview_news.visibility = VISIBLE
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                webview_news.visibility = VISIBLE
                progressBar.visibility = GONE
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
            webview_news.visibility = VISIBLE
            viewpager.visibility = GONE
            viewPagerAdapter.initClickTab(NEWS,image_news,image_comment,layout_webview,viewpager)
        }

        layout_tab_comment.setOnClickListener {
            webview_news.visibility = GONE
            viewpager.visibility = VISIBLE
            viewPagerAdapter.initClickTab(COMMENT,image_news,image_comment,layout_webview,viewpager)
            viewpager.currentItem = 0
            if (clickCountComment == 0) {
                clickCountComment = 1
                loadHandler.postDelayed({
                    (viewPagerAdapter.getItem(0) as CommentFragment).setupCommentRequest(story.kids)
                }, 200)
            }
        }

        image_refresh.setOnClickListener {
            when (webview_news.visibility) {
                VISIBLE -> {
                    reloadWebView()
                }
                GONE -> {
                    (viewPagerAdapter.getItem(0) as CommentFragment).reloadComments()
                }
            }
        }
    }

}