package com.adibsurani.hackernews.model

import com.google.gson.annotations.SerializedName

data class Comment (@SerializedName("by")
                    var author : String,
                    @SerializedName("descendants")
                    var descendants : String,
                    @SerializedName("id")
                    var id : String)