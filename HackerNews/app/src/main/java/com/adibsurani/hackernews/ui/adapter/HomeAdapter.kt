package com.adibsurani.hackernews.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.helper.TimeHelper
import com.adibsurani.hackernews.helper.Util
import com.adibsurani.hackernews.model.Story
import kotlinx.android.synthetic.main.row_story.view.*
import java.text.SimpleDateFormat
import java.util.*


class HomeAdapter (private var context: Context,
                   private var dataList: List<Story>,
                   val clickListener : (Story) -> Unit) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_story, parent, false)
        return ViewHolder(context, itemView)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(dataList[position],clickListener)
    }

    inner class ViewHolder(private val context: Context,
                           itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(data: Story,  clickListener: (Story) -> Unit) {

            itemView.text_title?.text = data.title

            itemView.text_time?.text = TimeHelper.getTimeAgo(data.time)

            data.url?.let {
                itemView.text_source?.text = Util.getHostName(data.url)
            }

            itemView.text_point?.text = "${data.score}"

            data.kids?.let {
                itemView.text_comment?.text = "${it.size}"
            }

            itemView.layout_root.setOnClickListener {
                clickListener(data)
            }
        }

        fun getDate(milliseconds : Long, format : String) : String {
            val formatter = SimpleDateFormat(format, Locale.US)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = milliseconds
            return formatter.format(calendar.time)
        }


    }
}