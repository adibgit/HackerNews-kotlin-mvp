package com.adibsurani.base.mvp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.adibsurani.base.App
import com.adibsurani.base.di.component.AppComponent
import com.adibsurani.base.event.DefaultEvent
import com.adibsurani.base.manager.PrefManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import javax.inject.Inject

abstract class BaseActivity :
    AppCompatActivity(),
    BaseView,
    AnkoLogger {

    private var presenter: BasePresenter<*>? = null
    @Inject
    lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onActivityInject()
    }

    protected abstract fun onActivityInject()

    fun getAppcomponent(): AppComponent = App.appComponent

    override fun setPresenter(presenter: BasePresenter<*>) {
        this.presenter = presenter
    }

    override fun onError() {
        toast("Something went wrong")
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
        presenter = null
    }

    @Subscribe
    fun defaultSubscribe(event: DefaultEvent){}
}