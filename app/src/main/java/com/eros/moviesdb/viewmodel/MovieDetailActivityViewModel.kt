package com.eros.moviesdb.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eros.moviesdb.model.response.MovieDetailRes
import com.eros.moviesdb.repository.MovieDetailRepository

class MovieDetailActivityViewModel : ViewModel(), MovieDetailRepository.OnMovieDetailListener {

    private lateinit var loader : MutableLiveData<Boolean>
    private lateinit var displayMessage : MutableLiveData<String>

    private var movieDetailRepository = MovieDetailRepository()
    private lateinit var movieDetailLiveData : MutableLiveData<MovieDetailRes>

    fun showProgressDialog() : MutableLiveData<Boolean> {
        if (!::loader.isInitialized) {
            loader = MutableLiveData<Boolean>()
        }
        return loader
    }

    fun showError() : MutableLiveData<String> {
        if (!::displayMessage.isInitialized) {
            displayMessage = MutableLiveData<String>()
        }
        return displayMessage
    }

    fun getMovieDetail(id : Int) : MutableLiveData<MovieDetailRes> {
        loader.value = true
        if (!::movieDetailLiveData.isInitialized) {
            movieDetailLiveData = MutableLiveData<MovieDetailRes>()
            movieDetailRepository.setListener(this)
        }

        movieDetailRepository.getMovieDetail(id)

        return movieDetailLiveData
    }

    override fun onMovieDetailSuccess(movieDetailRes: MovieDetailRes) {
        loader.value = false
        movieDetailLiveData.value = movieDetailRes
    }

    override fun onMovieDetailFailure(errorMsg: String) {
        loader.value = false
        displayMessage.value = errorMsg
    }
}