package com.adibsurani.base.mvp

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adibsurani.base.event.DefaultEvent
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.AnkoLogger

abstract class BaseFragment :
    Fragment(),
    AnkoLogger {

    protected var viewFragment : View? = null
    protected var layoutInflaterFragment : LayoutInflater? = null

    protected abstract fun initLayout(): Int
    protected abstract fun initView()
    protected abstract fun initData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewFragment = inflater.inflate(initLayout(), container, false)
        layoutInflaterFragment = LayoutInflater.from(context!!)

        return viewFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    @Subscribe
    fun defaultSubscribe(event: DefaultEvent){}

}