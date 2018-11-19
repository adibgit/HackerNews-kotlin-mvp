package com.adibsurani.hackernews.presenter

import com.adibsurani.base.mvp.BasePresenter
import com.adibsurani.hackernews.helper.Endpoints
import com.adibsurani.hackernews.model.Comment
import com.adibsurani.hackernews.model.Story
import com.adibsurani.hackernews.view.DetailView
import com.adibsurani.hackernews.view.HomeView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DetailPresenter
@Inject
constructor(var api: Endpoints) : BasePresenter<DetailView>() {

    fun getComment(commentID : Int) {

        api.getComment(commentID).enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                view?.getComment(response.body()!!)
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                view?.onError()
            }
        })
    }

    fun getChildComment(commentID : Int) {

        api.getComment(commentID).enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                view?.getChildComment(response.body()!!)
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                view?.onError()
            }
        })
    }
}