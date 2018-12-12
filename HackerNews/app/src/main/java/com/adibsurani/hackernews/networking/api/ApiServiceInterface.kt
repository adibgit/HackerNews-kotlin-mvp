package com.adibsurani.hackernews.networking.api

import com.adibsurani.hackernews.BuildConfig.BASE_URL
import com.adibsurani.hackernews.networking.data.Comment
import com.adibsurani.hackernews.networking.data.Story
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*
import kotlin.collections.ArrayList

interface ApiServiceInterface {

    companion object Factory {
        fun create(): ApiServiceInterface {
            val retrofit = retrofit2.Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
            return retrofit.create(ApiServiceInterface::class.java)
        }
    }

    @GET("topstories.json?print=pretty")
    fun getTopStoriesID() : Observable<ArrayList<Int>>

    @GET("item/{storyID}.json?print=pretty")
    fun getStory(@Path("storyID") storyID : Int) : Observable<Story>

    @GET("item/{commentID}.json?print=pretty")
    fun getComment(@Path("commentID") commentID : Int) : Observable<Comment>

}