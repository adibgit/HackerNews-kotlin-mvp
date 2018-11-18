package com.adibsurani.hackernews.model

import com.google.gson.annotations.SerializedName

data class Comment (@SerializedName("by")
                    var author : String,
                    @SerializedName("id")
                    var id : String,
                    @SerializedName("kids")
                    var kids : ArrayList<Int>,
                    @SerializedName("parent")
                    var parent : Int,
                    @SerializedName("text")
                    var text : String,
                    @SerializedName("time")
                    var time : Int,
                    @SerializedName("type")
                    var type : String)