package com.eros.moviesdb.repository

import com.eros.moviesdb.R
import com.eros.moviesdb.application.MyApp
import com.eros.moviesdb.model.response.TopRatedMoviesRes
import com.eros.moviesdb.service.ServiceHelper
import com.eros.moviesdb.utils.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchedMoviesRepository {

    private var mListener : OnSearchedMoviesListener? = null

    interface OnSearchedMoviesListener {
        fun onSearchedMoviesSuccess(topRatedMoviesRes : TopRatedMoviesRes)
        fun onSearchedMoviesFailure(errorMsg : String)
    }

    fun setListener(listner : OnSearchedMoviesListener) {
        mListener = listner
    }

    fun getSearchedMovies(page : Int, searchQuery : String) {
        if (NetworkUtil.isNetworkConnected()) {
            val queryParams = HashMap<String, String>()
            queryParams.put("api_key", MyApp.INSTANCE_.resources.getString(R.string.tmdb_api_key))
            queryParams.put("language", "en-US")
            queryParams.put("query", searchQuery)
            queryParams.put("page", ""+page)

            var callback = ServiceHelper.getRetrofitClient().getSearchedMovies(queryParams)
            callback.enqueue(object : Callback<TopRatedMoviesRes> {
                override fun onResponse(
                    call: Call<TopRatedMoviesRes>,
                    response: Response<TopRatedMoviesRes>
                ) {
                    if (response.body() != null) {
                        mListener?.onSearchedMoviesSuccess(response.body() as TopRatedMoviesRes)
                    } else {
                        mListener?.onSearchedMoviesFailure(MyApp.INSTANCE_.resources.getString(R.string.error_network))
                    }
                }

                override fun onFailure(call: Call<TopRatedMoviesRes>, t: Throwable) {
                    mListener?.onSearchedMoviesFailure(MyApp.INSTANCE_.resources.getString(R.string.server_error))
                }
            })
        } else {
            mListener?.onSearchedMoviesFailure(MyApp.INSTANCE_.resources.getString(R.string.network_error))
        }
    }
}