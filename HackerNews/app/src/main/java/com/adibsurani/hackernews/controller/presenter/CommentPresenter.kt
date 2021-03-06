package com.adibsurani.hackernews.controller.presenter

import com.adibsurani.hackernews.controller.contract.CommentContract
import com.adibsurani.hackernews.networking.api.ApiServiceInterface
import com.adibsurani.hackernews.networking.data.Comment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CommentPresenter: CommentContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: ApiServiceInterface = ApiServiceInterface.create()
    private lateinit var view: CommentContract.View

    override fun subscribe() {}

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: CommentContract.View) {
        this.view = view
    }

    override fun getComment(commentID: Int) {
        var subscribe = api
            .getComment(commentID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ comment: Comment ->
                view.getCommentSuccess(comment)
            }, { error ->
                view.showErrorMessage(error.localizedMessage)
            })
        subscriptions.add(subscribe)
    }
}