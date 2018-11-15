package com.adibsurani.hackernews.presenter

import com.adibsurani.base.mvp.BasePresenter
import com.adibsurani.hackernews.helper.Endpoints
import com.adibsurani.hackernews.model.Story
import com.adibsurani.hackernews.view.HomeView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class HomePresenter
@Inject
constructor(var api: Endpoints) : BasePresenter<HomeView>() {

    fun getStoriesID() {

        api.getTopStoriesID().enqueue(object : Callback<ArrayList<Int>> {
                override fun onResponse(call: Call<ArrayList<Int>>, response: Response<ArrayList<Int>>) {
                    view?.getStoriesID(response.body()!!)
                }

                override fun onFailure(call: Call<ArrayList<Int>>, t: Throwable) {
                    view?.onError()
                }
            })
    }

    fun getStory(storyID : Int) {

        api.getStory(storyID).enqueue(object : Callback<Story> {
            override fun onResponse(call: Call<Story>, response: Response<Story>) {
                view?.getStory(response.body()!!)
            }

            override fun onFailure(call: Call<Story>, t: Throwable) {
                view?.onError()
            }
        })
    }


}
