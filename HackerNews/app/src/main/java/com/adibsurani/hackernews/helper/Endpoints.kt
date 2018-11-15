package com.adibsurani.hackernews.helper

import com.adibsurani.hackernews.model.Story
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Endpoints {

    /*GET*/
    @GET("topstories.json?print=pretty")
    fun getTopStoriesID() : Call<ArrayList<Int>>

    @GET("item/{storyID}.json?print=pretty")
    fun getStory(@Path("storyID") storyID : Int) : Call<Story>

}