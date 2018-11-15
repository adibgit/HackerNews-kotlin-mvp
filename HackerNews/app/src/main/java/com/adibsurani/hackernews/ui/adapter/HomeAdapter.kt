package com.adibsurani.hackernews.ui.adapter

import android.view.View
import com.adibsurani.hackernews.model.Story
import com.burakeregar.easiestgenericrecycleradapter.base.GenericViewHolder
import kotlinx.android.synthetic.main.row_story.view.*
import android.net.Proxy.getHost
import java.net.MalformedURLException
import java.net.URL


class HomeAdapter (itemView: View?) : GenericViewHolder<Any>(itemView) {
    lateinit var item: Story

    override fun bindData(p0: Any?) {
        item = p0 as Story
        with(itemView) {
            text_title?.text = item.title
            text_source?.text = getHostName(item.url)
            text_point?.text = "${item.score}"
            item.kids?.let {
                text_comment?.text = "${it.size}"
            }
        }
    }

    private fun getSource(url : String) : String {
        val firstCut : String = url.replace("http://","")
        return firstCut
    }

    private fun getHostName(urlInput: String): String {
        var urlInput = urlInput
        urlInput = urlInput.toLowerCase()
        var hostName = urlInput
        if (urlInput != "") {
            if (urlInput.startsWith("http") || urlInput.startsWith("https")) {
                try {
                    val netUrl = URL(urlInput)
                    val host = netUrl.getHost()
                    if (host.startsWith("www")) {
                        hostName = host.substring("www".length + 1)
                    } else {
                        hostName = host
                    }
                } catch (e: MalformedURLException) {
                    hostName = urlInput
                }

            } else if (urlInput.startsWith("www")) {
                hostName = urlInput.substring("www".length + 1)
            }
            return hostName
        } else {
            return ""
        }
    }
}