package com.eros.moviesdb.utils

import android.content.Context
import android.net.ConnectivityManager
import com.eros.moviesdb.application.MyApp

object NetworkUtil {

    fun isNetworkConnected() : Boolean {
        var cm = MyApp.INSTANCE_.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected;
    }

}