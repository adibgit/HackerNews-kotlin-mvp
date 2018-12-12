package com.adibsurani.hackernews.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {

    protected abstract fun initLayout(): Int
    protected abstract fun initInject()
    protected abstract fun initCreate()

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate (savedInstanceState)
        setContentView (initLayout())
        initInject()
        initCreate()
    }

}