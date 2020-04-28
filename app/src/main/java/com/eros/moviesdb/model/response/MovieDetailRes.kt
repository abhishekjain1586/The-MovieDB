package com.eros.moviesdb.model.response

import com.google.gson.annotations.SerializedName

class MovieDetailRes {

    @SerializedName("id")
    var id : Int? = null

    @SerializedName("title")
    var title : String? = null

    @SerializedName("overview")
    var overview : String? = null

    @SerializedName("poster_path")
    var poster_path : String? = null

    @SerializedName("release_date")
    var release_date : String? = null

    @SerializedName("genres")
    var genreLst : ArrayList<Genre>? = null

}