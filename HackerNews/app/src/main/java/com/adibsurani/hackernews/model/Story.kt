package com.adibsurani.hackernews.model

import com.google.gson.annotations.SerializedName

data class Story (@SerializedName("by")
                  var author : String,
                  @SerializedName("descendants")
                  var descendants : String,
                  @SerializedName("id")
                  var id : String,
                  @SerializedName("kids")
                  var kids : ArrayList<Int>,
                  @SerializedName("score")
                  var score: Int,
                  @SerializedName("time")
                  var time : Long,
                  @SerializedName("title")
                  var title : String,
                  @SerializedName("type")
                  var type : String,
                  @SerializedName("url")
                  var url : String)