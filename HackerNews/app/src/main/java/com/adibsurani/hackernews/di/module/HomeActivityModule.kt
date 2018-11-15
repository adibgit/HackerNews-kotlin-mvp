package com.adibsurani.hackernews.di.module

import com.adibsurani.base.di.ActivityScope
import com.adibsurani.hackernews.helper.Endpoints
import com.adibsurani.hackernews.presenter.HomePresenter
import dagger.Module
import dagger.Provides

@Module
class HomeActivityModule {

    @Provides
    @ActivityScope
    internal fun providesHomePresenter(api: Endpoints): HomePresenter {
        return HomePresenter(api)
    }
}