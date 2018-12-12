package com.adibsurani.hackernews.ui.activity

import android.content.Intent
import android.os.Handler
import com.adibsurani.hackernews.R
import com.adibsurani.hackernews.ui.base.BaseActivity

class SplashActivity : BaseActivity() {

    private val loadHandler = Handler()

    override fun initLayout(): Int {
        return R.layout.activity_splash
    }

    override fun initInject() {}

    override fun initCreate() {
        gotoHome()
    }

    private fun gotoHome() {
        loadHandler.postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 500)
    }
}