package com.adibsurani.hackernews.controller.presenter

import com.adibsurani.hackernews.controller.contract.HomeContract
import com.adibsurani.hackernews.networking.api.ApiServiceInterface
import com.adibsurani.hackernews.networking.data.Story
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomePresenter: HomeContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: ApiServiceInterface = ApiServiceInterface.create()
    private lateinit var view: HomeContract.View

    override fun subscribe() {}

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: HomeContract.View) {
        this.view = view
    }

    override fun getStoriesID() {
        var subscribe = api
            .getTopStoriesID()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ storiesID: ArrayList<Int> ->
                view.getStoriesIDSuccess(storiesID)
            }, { error ->
                view.showErrorMessage(error.localizedMessage)
            })
        subscriptions.add(subscribe)
    }

    override fun getStory(storyID: Int) {
        var subscribe = api
            .getStory(storyID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ story: Story ->
                view.getStorySuccess(story)
            }, { error ->
                view.showErrorMessage(error.localizedMessage)
            })
        subscriptions.add(subscribe)
    }
}