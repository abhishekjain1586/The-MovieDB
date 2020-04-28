package com.eros.moviesdb.application

import android.app.Application

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE_ = this
    }

    companion object {
        lateinit var INSTANCE_ : MyApp
    }
}