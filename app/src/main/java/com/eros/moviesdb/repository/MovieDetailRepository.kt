package com.eros.moviesdb.repository

import com.eros.moviesdb.R
import com.eros.moviesdb.application.MyApp
import com.eros.moviesdb.model.response.MovieDetailRes
import com.eros.moviesdb.service.ServiceHelper
import com.eros.moviesdb.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailRepository {

    private var mListener : OnMovieDetailListener? = null

    interface OnMovieDetailListener {
        fun onMovieDetailSuccess(movieDetailRes : MovieDetailRes)
        fun onMovieDetailFailure(errorMsg : String)
    }

    fun setListener(listner : OnMovieDetailListener) {
        mListener = listner
    }

    fun getMovieDetail(id : Int) {
        if (NetworkUtil.isNetworkConnected()) {
            val queryParams = HashMap<String, String>()
            queryParams.put("api_key", MyApp.INSTANCE_.resources.getString(R.string.tmdb_api_key))
            queryParams.put("language", "en-US")

            var callback = ServiceHelper.getRetrofitClient().getMovieDetail(id, queryParams)
            callback.enqueue(object : Callback<MovieDetailRes> {
                override fun onResponse(
                    call: Call<MovieDetailRes>,
                    response: Response<MovieDetailRes>
                ) {
                    if (response.body() != null) {
                        mListener?.onMovieDetailSuccess(response.body() as MovieDetailRes)
                    } else {
                        mListener?.onMovieDetailFailure(MyApp.INSTANCE_.resources.getString(R.string.error_network))
                    }
                }

                override fun onFailure(call: Call<MovieDetailRes>, t: Throwable) {
                    mListener?.onMovieDetailFailure(MyApp.INSTANCE_.resources.getString(R.string.server_error))
                }
            })
        } else {
            mListener?.onMovieDetailFailure(MyApp.INSTANCE_.resources.getString(R.string.network_error))
        }
    }
}