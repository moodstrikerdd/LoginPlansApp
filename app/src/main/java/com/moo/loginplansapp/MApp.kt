package com.moo.loginplansapp

import android.app.Application

class MApp :Application() {
    companion object{
        @JvmStatic
        var application: Application? = null
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}