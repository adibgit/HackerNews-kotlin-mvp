package com.adibsurani.hackernews.dagger.module

import android.app.Application
import com.adibsurani.hackernews.application.BaseApp
import com.adibsurani.hackernews.dagger.scope.PerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val baseApp: BaseApp) {
    @Provides
    @Singleton
    @PerApplication
    fun provideApplication(): Application {
        return baseApp
    }
}