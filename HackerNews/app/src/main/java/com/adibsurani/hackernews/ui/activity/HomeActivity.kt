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
import com.adibsurani.hackernews.helper.Constants.Companion.COMMENT
import com.adibsurani.hackernews.helper.Constants.Companion.NEWS
import com.adibsurani.hackernews.helper.RVHelper
import com.adibsurani.hackernews.helper.Util
import com.adibsurani.hackernews.networking.data.Comment
import com.adibsurani.hackernews.networking.data.Story
import com.adibsurani.hackernews.ui.adapter.CommentListAdapter
import com.adibsurani.hackernews.ui.adapter.NewsAdapter
import com.adibsurani.hackernews.ui.base.BaseActivity
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_dropdown.*
import kotlinx.android.synthetic.main.layout_news_bottom.*
import kotlinx.android.synthetic.main.layout_news_comment.*
import kotlinx.android.synthetic.main.layout_news_web.*
import qiu.niorgai.StatusBarCompat
import javax.inject.Inject


class HomeActivity :
    BaseActivity(),
    HomeContract.View {

    @Inject
    lateinit var homePresenter : HomeContract.Presenter
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var commentListAdapter: CommentListAdapter
    private lateinit var currentStory: Story
    private var storyList = ArrayList<Story>()
    private val loadHandler = Handler()
    private var kidsCount: Int = 0
    private var kidsList =  ArrayList<Int>()
    private var commentList = ArrayList<Comment>()
    private var clickCountComment: Int = 0


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
    }

    override fun showProgress(show: Boolean) {

    }

    override fun onBackPressed() {
        if (layout_header.visibility == GONE && layout_comment.visibility == GONE) {
            animateNewsToHome()
            showBottom(false)
        } else if (layout_header.visibility == GONE && layout_web.visibility == GONE){
            animateCommentToNews()

        } else {
            super.onBackPressed()

        }
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
        layout_tab_news.setOnClickListener {
            image_news.setColorFilter(resources.getColor(R.color.orange_500))
            image_comment.setColorFilter(resources.getColor(R.color.colorPaper))
            animateCommentToNews()
        }
        layout_tab_comment.setOnClickListener {
            image_news.setColorFilter(resources.getColor(R.color.colorPaper))
            image_comment.setColorFilter(resources.getColor(R.color.orange_500))
            animateNewsToComment()
            if (clickCountComment == 0) {
                clickCountComment = 1
                setupCommentRequest(currentStory.kids)
            }
        }
    }

    private fun initRecycler() {
        RVHelper.setupVertical(recycler_story,this)
        newsAdapter = NewsAdapter(this,this)
        recycler_story.adapter = newsAdapter
        RVHelper.setupVertical(recycler_comment,this)
        commentListAdapter = CommentListAdapter(this)
        recycler_comment.adapter = commentListAdapter
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

    fun dataClicked(data : Story) {
        currentStory = data
        showWeb(currentStory)
    }

    fun refreshClicked(type: Int) {
        when (type) {
            NEWS -> {
                webview_news.reload()
            }
            COMMENT -> {
                reloadComments()
            }
        }
    }

    // WEB
    private fun showWeb(story: Story) {
        Log.e("STORY WEB:", "$story")
        animateShowWeb()
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

    // COMMENT
    override fun getCommentSuccess(comment: Comment) {
        Log.e("CommentID ID", "${comment.id}")
        comment.kids?.let {
            Log.e("CommentID kids", "${comment.kids.size}")
        }

        commentList.add(comment)
        if (commentList.size == kidsCount) {
            commentListAdapter.setDataSource(commentList)
            doneLoadComment()
        }
    }

    private fun setupCommentRequest(kids: ArrayList<Int>) {
        if (kids != null) {
            kidsCount = kids.size
            kidsList = kids
            for (comment in kids) {
                homePresenter.getComment(comment)
            }
        }
    }

    private fun reloadComments() {
        setupLoadComment()
        commentListAdapter.clearAdapter()
        commentList.clear()
        for (comment in kidsList) {
            homePresenter.getComment(comment)
        }
    }

    private fun setupLoadComment() {
        recycler_comment.visibility = GONE
        layout_shimmer_comment.visibility = VISIBLE
        layout_shimmer_comment.startShimmer()
    }

    private fun doneLoadComment() {
        recycler_comment.visibility = VISIBLE
        layout_shimmer_comment.stopShimmer()
        layout_shimmer_comment.visibility = GONE
    }

    // ANIMATION
    private fun animateShowWeb() {
        StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.colorPrimary))
        StatusBarUtil.setDarkMode(this)
        layout_web.visibility = VISIBLE
        layout_web.startAnimation(AnimationUtil.inFromRightAnimation())
        layout_story.startAnimation(AnimationUtil.outToLeftAnimation())
        layout_header.startAnimation(AnimationUtil.outToLeftAnimation())
        loadHandler.postDelayed({
            layout_story.visibility = GONE
            layout_header.visibility = GONE
        }, 150)
        showBottom(true)
    }

    private fun animateNewsToHome() {
        StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.colorAccent))
        StatusBarUtil.setLightMode(this)
        layout_story.visibility = VISIBLE
        layout_header.visibility = VISIBLE
        layout_story.startAnimation(AnimationUtil.inFromLeftAnimation())
        layout_story.startAnimation(AnimationUtil.inFromLeftAnimation())
        layout_web.startAnimation(AnimationUtil.outToRightAnimation())
        loadHandler.postDelayed({
            webview_news.loadUrl("about:blank")
            layout_web.visibility = GONE
        }, 150)
    }

    private fun animateCommentToNews() {
        StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.colorPrimary))
        StatusBarUtil.setDarkMode(this)
        layout_web.visibility = VISIBLE
        layout_web.startAnimation(AnimationUtil.inFromLeftAnimation())
        layout_comment.startAnimation(AnimationUtil.outToRightAnimation())
        loadHandler.postDelayed({
            layout_comment.visibility = GONE
        }, 150)
    }

    private fun animateNewsToComment() {
        StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.colorPrimary))
        StatusBarUtil.setDarkMode(this)
        layout_comment.visibility = VISIBLE
        layout_comment.startAnimation(AnimationUtil.inFromRightAnimation())
        layout_web.startAnimation(AnimationUtil.outToLeftAnimation())
        loadHandler.postDelayed({
            layout_web.visibility = GONE
        }, 150)
    }

    private fun showBottom(status: Boolean) {
        if (status) {
            layout_bottom.visibility = VISIBLE
            layout_bottom.startAnimation(AnimationUtil.inFromBottomAnimation())
        } else {
            layout_bottom.visibility = GONE
        }
    }
}