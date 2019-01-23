package com.adibsurani.hackernews.application

import android.app.Application
import com.adibsurani.hackernews.dagger.component.ApplicationComponent
import com.adibsurani.hackernews.dagger.component.DaggerApplicationComponent
import com.adibsurani.hackernews.dagger.module.ApplicationModule
import io.realm.Realm
import io.realm.Realm.setDefaultConfiguration
import io.realm.RealmConfiguration




class BaseApp: Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        instance = this
        setupRealm()
        setupApplication()
    }

    private fun setupRealm() {
        val realmConfiguration = RealmConfiguration.Builder(this)
            .name(Realm.DEFAULT_REALM_NAME)
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }

    private fun setupApplication() {
        component = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
        component
                .inject(this)
    }

    fun getApplicationComponent(): ApplicationComponent {
        return component
    }

    companion object {
        lateinit var instance: BaseApp private set
    }
}