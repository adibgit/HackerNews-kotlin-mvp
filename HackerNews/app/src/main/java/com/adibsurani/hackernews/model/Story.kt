package com.adibsurani.hackernews.model

import com.google.gson.annotations.SerializedName

data class Story (@SerializedName("by")
                  var author : String,
                  @SerializedName("descendants")
                  var descendants : String,
                  @SerializedName("id")
                  var id : String,
                  @SerializedName("kids")
                  var kids : ArrayList<String>,
                  @SerializedName("score")
                  var score: Int,
                  @SerializedName("time")
                  var time : Int,
                  @SerializedName("title")
                  var title : String,
                  @SerializedName("type")
                  var type : String,
                  @SerializedName("url")
                  var url : String)