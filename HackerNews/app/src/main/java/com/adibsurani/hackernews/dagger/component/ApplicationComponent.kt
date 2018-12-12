package com.adibsurani.hackernews.dagger.component

import com.adibsurani.hackernews.application.BaseApp
import com.adibsurani.hackernews.dagger.module.ApplicationModule
import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(application: BaseApp)
}