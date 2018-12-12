package com.adibsurani.hackernews.dagger.module

import com.adibsurani.hackernews.controller.contract.CommentContract
import com.adibsurani.hackernews.controller.presenter.CommentPresenter
import com.adibsurani.hackernews.networking.api.ApiServiceInterface
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {

    @Provides
    fun provideCommentPresenter(): CommentContract.Presenter {
        return CommentPresenter()
    }

    @Provides
    fun provideApiService(): ApiServiceInterface {
        return ApiServiceInterface.create()
    }
}