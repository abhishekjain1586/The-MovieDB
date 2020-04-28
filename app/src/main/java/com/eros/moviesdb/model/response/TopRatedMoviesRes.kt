package com.eros.moviesdb.model.response

import com.google.gson.annotations.SerializedName

class TopRatedMoviesRes {

    @SerializedName("page")
    var page : Int? = null

    @SerializedName("total_results")
    var total_results : Int? = null

    @SerializedName("total_pages")
    var total_pages : Int? = null

    @SerializedName("results")
    var moviesLst : ArrayList<Movies>? = null

}