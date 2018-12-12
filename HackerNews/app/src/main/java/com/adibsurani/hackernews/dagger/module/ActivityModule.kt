package com.adibsurani.hackernews.dagger.module

import android.app.Activity
import com.adibsurani.hackernews.controller.contract.HomeContract
import com.adibsurani.hackernews.controller.presenter.HomePresenter
import com.adibsurani.hackernews.networking.api.ApiServiceInterface
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private  var activity: Activity) {

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun provideApiService(): ApiServiceInterface {
        return ApiServiceInterface.create()
    }

    @Provides
    fun provideHomePresenter(): HomeContract.Presenter {
        return HomePresenter()
    }

}