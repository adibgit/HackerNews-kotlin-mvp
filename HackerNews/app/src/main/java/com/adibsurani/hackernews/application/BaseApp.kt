package com.adibsurani.hackernews.application

import android.app.Application
import com.adibsurani.hackernews.dagger.component.ApplicationComponent
import com.adibsurani.hackernews.dagger.component.DaggerApplicationComponent
import com.adibsurani.hackernews.dagger.module.ApplicationModule


class BaseApp: Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        instance = this
        setupApplication()
    }

    private fun setupApplication() {
        component = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
        component
                .inject(this)
    }

    fun getApplicationComponent(): ApplicationComponent {
        return component
    }

    companion object {
        lateinit var instance: BaseApp private set
    }
}