package com.adibsurani.hackernews.ui.base

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {

    //abstract implement in order
    protected abstract fun initLayout(): Int
    protected abstract fun initInject()
    protected abstract fun initData()
    protected abstract fun initPresenter()
    protected abstract fun initView()

    //always check view-let when call method
    private lateinit var viewFragment: View
    private lateinit var layoutInflaterFragment: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
        initData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewFragment = inflater.inflate(initLayout(), container, false)
        layoutInflaterFragment = LayoutInflater.from(activity)
        return viewFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPresenter()
        initView()
    }

}