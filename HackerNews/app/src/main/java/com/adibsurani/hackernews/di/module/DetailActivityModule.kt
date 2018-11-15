package com.adibsurani.hackernews.di.module

import com.adibsurani.base.di.ActivityScope
import com.adibsurani.hackernews.helper.Endpoints
import com.adibsurani.hackernews.presenter.DetailPresenter
import com.adibsurani.hackernews.presenter.HomePresenter
import dagger.Module
import dagger.Provides

@Module
class DetailActivityModule {

    @Provides
    @ActivityScope
    internal fun providesDetailPresenter(api: Endpoints): DetailPresenter {
        return DetailPresenter(api)
    }
}