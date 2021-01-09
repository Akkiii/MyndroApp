package com.myndrotest.app

import android.app.Application

class MyndroApp : Application() {
    companion object {
        lateinit var instance: MyndroApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}