package com.eros.moviesdb.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eros.moviesdb.R
import com.eros.moviesdb.application.MyApp
import com.eros.moviesdb.model.response.TopRatedMoviesRes
import com.eros.moviesdb.service.ServiceHelper
import com.eros.moviesdb.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopRatedMoviesRepository {

    private var mListener : OnTopRatedMoviesListener? = null

    interface OnTopRatedMoviesListener {
        fun onTopRatedMoviesSuccess(topRatedMoviesRes : TopRatedMoviesRes)
        fun onTopRatedMoviesFailure(errorMsg : String)
    }

    fun setListener(listner : OnTopRatedMoviesListener) {
        mListener = listner
    }

    fun getTopRatedMovies(page : Int) {
        if (NetworkUtil.isNetworkConnected()) {
            val queryParams = HashMap<String, String>()
            queryParams.put("api_key", MyApp.INSTANCE_.resources.getString(R.string.tmdb_api_key))
            queryParams.put("language", "en-US")
            queryParams.put("page", ""+page)

            var callback = ServiceHelper.getRetrofitClient().getTopRatedMovies(queryParams)
            callback.enqueue(object : Callback<TopRatedMoviesRes> {
                override fun onResponse(call: Call<TopRatedMoviesRes>, response: Response<TopRatedMoviesRes>) {
                    if (response.body() != null) {
                        mListener?.onTopRatedMoviesSuccess(response.body() as TopRatedMoviesRes)
                    } else {
                        mListener?.onTopRatedMoviesFailure(MyApp.INSTANCE_.resources.getString(R.string.error_network))
                    }
                }

                override fun onFailure(call: Call<TopRatedMoviesRes>, t: Throwable) {
                    mListener?.onTopRatedMoviesFailure(MyApp.INSTANCE_.resources.getString(R.string.server_error))
                }
            })
        } else {
            mListener?.onTopRatedMoviesFailure(MyApp.INSTANCE_.resources.getString(R.string.network_error))
        }
    }
}