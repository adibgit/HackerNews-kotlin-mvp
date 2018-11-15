package com.adibsurani.base.di.module

import com.adibsurani.hackernews.helper.Endpoints
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun providesEndpoints(retrofit: Retrofit) : Endpoints = retrofit.create(Endpoints::class.java)

}