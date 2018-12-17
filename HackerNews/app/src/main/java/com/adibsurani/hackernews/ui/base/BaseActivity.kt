package com.adibsurani.hackernews.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adibsurani.hackernews.networking.local.LocalDB

abstract class BaseActivity: AppCompatActivity() {

    protected abstract fun initLayout(): Int
    protected abstract fun initInject()
    protected abstract fun initCreate()

    protected lateinit var localDB: LocalDB

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate (savedInstanceState)
        setContentView (initLayout())
        localDB = LocalDB(this)
        initInject()
        initCreate()
    }

}