package com.adibsurani.hackernews.networking.api

class RestAPI {

    companion object {

        const val BASE_URL = "https://hacker-news.firebaseio.com/v0/"

        // GET
        fun getComment(commentID: String): String {
            return BASE_URL + "item/" + commentID + ".json?print=pretty"
        }
    }

}