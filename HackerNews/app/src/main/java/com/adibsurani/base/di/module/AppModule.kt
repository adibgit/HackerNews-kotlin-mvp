package com.adibsurani.base.di.module

import android.app.Application
import android.content.Context
import com.adibsurani.base.manager.PrefManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(val application: Application) {

    @Provides
    @Singleton
    fun providesGson() = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()

    @Provides
    @Singleton
    fun providesApplication() = application

    @Provides
    @Singleton
    fun providesResources() = application.resources

    @Provides
    @Singleton
    fun providesSharedPref(gson: Gson) = PrefManager(application.getSharedPreferences("PrefManager", Context.MODE_PRIVATE), gson)

}