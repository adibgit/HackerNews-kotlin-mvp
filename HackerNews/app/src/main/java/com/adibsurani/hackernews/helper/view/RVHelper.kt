package com.adibsurani.hackernews.helper.view

import android.content.Context
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.nshmura.snappysmoothscroller.LinearLayoutScrollVectorDetector
import com.nshmura.snappysmoothscroller.SnappySmoothScroller

class RVHelper {

    companion object {

        fun setupVertical(recyclerView: RecyclerView, context: Context): RecyclerView {

            recyclerView.setHasFixedSize(false)
            ViewCompat.setNestedScrollingEnabled(recyclerView, false)
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            val layoutManager = object : LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
                override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
                    val scroller = SnappySmoothScroller.Builder()
                        .setPosition(position)
                        .setScrollVectorDetector(LinearLayoutScrollVectorDetector(this))
                        .build(recyclerView.context)
                    startSmoothScroll(scroller)
                }
            }
            recyclerView.layoutManager = layoutManager
            return recyclerView
        }

        fun setupHorizontal(recyclerView: RecyclerView, context: Context): RecyclerView {

            recyclerView.setHasFixedSize(true)
            recyclerView.isNestedScrollingEnabled = false
            val layoutManager = object : LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) {
                override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
                    val scroller = SnappySmoothScroller.Builder()
                        .setPosition(position)
                        .setScrollVectorDetector(LinearLayoutScrollVectorDetector(this))
                        .build(recyclerView.context)
                    startSmoothScroll(scroller)
                }
            }
            recyclerView.layoutManager = layoutManager
            return recyclerView
        }
    }

}