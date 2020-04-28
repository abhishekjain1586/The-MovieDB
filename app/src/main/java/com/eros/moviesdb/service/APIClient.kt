package com.eros.moviesdb.service

import com.eros.moviesdb.model.response.MovieDetailRes
import com.eros.moviesdb.model.response.TopRatedMoviesRes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface APIClient {

    // Top Rated Movies
    @GET("movie/top_rated")
    fun getTopRatedMovies(@QueryMap userDetailReq: Map<String, String>) : Call<TopRatedMoviesRes>

    // Movie Detail
    @GET("movie/{id}")
    fun getMovieDetail(@Path ("id") id:Int, @QueryMap userDetailReq: Map<String, String>) : Call<MovieDetailRes>

    // Search Movies
    @GET("search/movie")
    fun getSearchedMovies(@QueryMap userDetailReq: Map<String, String>) : Call<TopRatedMoviesRes>
}