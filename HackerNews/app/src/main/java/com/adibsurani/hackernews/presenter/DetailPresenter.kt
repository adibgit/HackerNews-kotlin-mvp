package com.adibsurani.hackernews.presenter

import com.adibsurani.base.mvp.BasePresenter
import com.adibsurani.hackernews.helper.Endpoints
import com.adibsurani.hackernews.view.DetailView
import com.adibsurani.hackernews.view.HomeView
import javax.inject.Inject

class DetailPresenter
@Inject
constructor(var api: Endpoints) : BasePresenter<DetailView>() {
}