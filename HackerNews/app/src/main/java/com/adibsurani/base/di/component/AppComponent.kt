package com.adibsurani.base.di.component

import android.app.Application
import android.content.res.Resources
import com.adibsurani.base.di.module.ApiModule
import com.adibsurani.base.di.module.AppModule
import com.adibsurani.base.di.module.OkHttpModule
import com.adibsurani.base.di.module.RetrofitModule
import com.adibsurani.base.manager.PrefManager
import com.adibsurani.hackernews.helper.Endpoints
import com.google.gson.Gson
import dagger.Component
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, RetrofitModule::class, ApiModule::class, OkHttpModule::class])
interface AppComponent {

    fun application(): Application
    fun gson(): Gson
    fun resources(): Resources
    fun retrofit(): Retrofit
    fun endpoints(): Endpoints
    fun cache(): Cache
    fun client(): OkHttpClient
    fun loggingInterceptor(): HttpLoggingInterceptor
    fun prefManager(): PrefManager
}
