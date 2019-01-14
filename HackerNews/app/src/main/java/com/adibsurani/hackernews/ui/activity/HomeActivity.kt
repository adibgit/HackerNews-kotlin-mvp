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
import com.adibsurani.hackernews.controller.contract.HomeContract
import com.adibsurani.hackernews.dagger.component.DaggerActivityComponent
import com.adibsurani.hackernews.dagger.module.ActivityModule
import com.adibsurani.hackernews.helper.AnimationUtil
import com.adibsurani.hackernews.helper.RVHelper
import com.adibsurani.hackernews.helper.Util
import com.adibsurani.hackernews.networking.data.Story
import com.adibsurani.hackernews.ui.adapter.NewsAdapter
import com.adibsurani.hackernews.ui.base.BaseActivity
import com.jaeger.library.StatusBarUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_dropdown.*
import kotlinx.android.synthetic.main.layout_news_web.*
import qiu.niorgai.StatusBarCompat
import javax.inject.Inject

class HomeActivity :
    BaseActivity(),
    HomeContract.View {

    @Inject
    lateinit var homePresenter : HomeContract.Presenter
    private lateinit var newsAdapter: NewsAdapter
    private var storyList = ArrayList<Story>()
    private var typeList = ArrayList<String>()
    val loadHandler = Handler()

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
        StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.colorAccent))
        StatusBarUtil.setLightMode(this)
        initView()
        initClick()
        initRecycler()
        initTypeList()
    }

    override fun showProgress(show: Boolean) {

    }

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
        image_dropdown.setOnClickListener {
            StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.colorPrimary))
            StatusBarUtil.setDarkMode(this)
            layout_menu.visibility = VISIBLE
            layout_menu.bringToFront()
            layout_menu.startAnimation(AnimationUtil.inFromTopAnimation())
            layout_story.visibility = GONE
        }
        image_dismiss.setOnClickListener {
            StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.colorAccent))
            StatusBarUtil.setLightMode(this)
            layout_story.visibility = VISIBLE
            layout_menu.startAnimation(AnimationUtil.outToTopAnimation())
            loadHandler.postDelayed({
                layout_menu.visibility = GONE
            }, 150)
        }
    }

    private fun initRecycler() {
        RVHelper.setupVertical(recycler_story,this)
        newsAdapter = NewsAdapter(this,this)
        recycler_story.adapter = newsAdapter
    }

    private fun initTypeList() {
        typeList.add("Top")
        typeList.add("Best")
        typeList.add("New")
    }

    private fun startShimmer() {
        recycler_story.visibility = GONE
        layout_shimmer_story.visibility = VISIBLE
        image_refresh.bringToFront()
    }

    private fun stopShimmer() {
        layout_shimmer_story.visibility = GONE
        recycler_story.visibility = VISIBLE
    }

    fun dataClicked(data : Story,
                    image: String) {
//        val intent = Intent(this, DetailActivity::class.java)
//        intent.putExtra("story",Gson().toJson(data))
//        startActivity(intent)

        showWeb(data, image)

    }

    fun typeClicked(position: Int) {
        when (position) {
            0 -> { }
            1 -> { }
            2 -> { }
        }
    }

    private fun showWeb(story: Story,
                        imageURL: String) {
        Log.e("STORY WEB:", "$story")
        Log.e("STORY WEB IMAGE:", imageURL)
        StatusBarUtil.setTransparent(this)
        layout_web.visibility = VISIBLE
        layout_web.bringToFront()
        layout_web.startAnimation(AnimationUtil.inFromRightAnimation())
        layout_story.startAnimation(AnimationUtil.outToLeftAnimation())
        layout_header.startAnimation(AnimationUtil.outToLeftAnimation())
        loadHandler.postDelayed({
            layout_story.visibility = GONE
            layout_header.visibility = GONE
        }, 300)
        if (imageURL != "no image") {
            Picasso
                .get()
                .load(imageURL)
                .into(img_preview_web)
        }
        text_title_web.text = story.title
        text_source_web.text = Util.getHostName(story.url)
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

}