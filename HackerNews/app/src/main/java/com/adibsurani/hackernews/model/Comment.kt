package com.adibsurani.hackernews.model

import com.google.gson.annotations.SerializedName

data class Comment (@SerializedName("by")
                    var author : String,
                    @SerializedName("id")
                    var id : Int,
                    @SerializedName("kids")
                    var kids : ArrayList<Int>,
                    @SerializedName("parent")
                    var parent : Int,
                    @SerializedName("text")
                    var text : String,
                    @SerializedName("time")
                    var time : Long,
                    @SerializedName("type")
                    var type : String,
                    @SerializedName("comment")
                    var comment : ArrayList<Comment>)