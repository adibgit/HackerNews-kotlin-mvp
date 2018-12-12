package com.adibsurani.hackernews.dagger.component

import com.adibsurani.hackernews.dagger.module.ActivityModule
import com.adibsurani.hackernews.ui.activity.DetailActivity
import com.adibsurani.hackernews.ui.activity.HomeActivity
import dagger.Component


@Component(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(homeActivity: HomeActivity)
    fun inject(detailActivity: DetailActivity)
}