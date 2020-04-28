package com.eros.moviesdb.service

interface OnAPICallbackListener {

    fun onSuccess()

    fun onFailure()

    fun onException()
}