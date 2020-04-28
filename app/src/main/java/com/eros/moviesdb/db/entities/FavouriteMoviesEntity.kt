package com.eros.moviesdb.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "favourite")
class FavouriteMoviesEntity {

    @ColumnInfo(name = "movie_id")
    @PrimaryKey
    var movieId : Int? = null

    @ColumnInfo(name = "movie_title")
    var movieTitle : String? = null

    @ColumnInfo(name = "poster_path")
    var posterPath : String? = null

    @ColumnInfo(name = "release_date")
    var releaseDate : String? = null
}