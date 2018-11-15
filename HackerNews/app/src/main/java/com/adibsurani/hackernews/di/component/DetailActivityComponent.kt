package com.adibsurani.hackernews.di.component

import com.adibsurani.base.di.ActivityScope
import com.adibsurani.base.di.component.AppComponent
import com.adibsurani.hackernews.di.module.DetailActivityModule
import com.adibsurani.hackernews.di.module.HomeActivityModule
import com.adibsurani.hackernews.ui.activity.DetailActivity
import com.adibsurani.hackernews.ui.activity.HomeActivity
import dagger.Component


@ActivityScope
@Component(dependencies = [AppComponent::class],
    modules = [DetailActivityModule::class])

interface DetailActivityComponent {
    fun inject(detailActivity : DetailActivity)
}