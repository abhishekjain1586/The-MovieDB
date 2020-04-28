package com.eros.moviesdb.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Movies {

    @SerializedName("id")
    var id : Int? = null

    @SerializedName("title")
    var title : String? = null

    @SerializedName("poster_path")
    var poster_path : String? = null

    @SerializedName("release_date")
    var release_date : String? = null

    @Expose
    var isFavourite : Boolean = false
}