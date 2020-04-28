package com.eros.moviesdb.repository

import com.eros.moviesdb.db.DBHelper
import com.eros.moviesdb.db.entities.FavouriteMoviesEntity
import com.eros.moviesdb.model.response.Movies

class FavouriteMoviesRepository {

    fun addFavouriteMovieToDB(movieObj: Movies) {
        var movieEntity = FavouriteMoviesEntity()
        //movieObj.id?.let { movieEntity.movieId = it }
        movieEntity.movieId = movieObj.id
        movieEntity.movieTitle = movieObj.title
        movieEntity.posterPath = movieObj.poster_path
        movieEntity.releaseDate = movieObj.release_date

        DBHelper.getInstance().getFavouriteMoviesDao().insert(movieEntity)
    }

    fun removeFavouriteMovieFromDB(movieObj: Movies) {
        var movieEntity = FavouriteMoviesEntity()
        movieEntity.movieId = movieObj.id
        /*movieEntity.movieTitle = movieObj.title
        movieEntity.posterPath = movieObj.poster_path
        movieEntity.releaseDate = movieObj.release_date*/

        DBHelper.getInstance().getFavouriteMoviesDao().delete(movieEntity)
    }

    fun getAllFavouriteMovies() : ArrayList<FavouriteMoviesEntity>? {
        return DBHelper.getInstance().getFavouriteMoviesDao().getAllFavouriteMovies() as ArrayList<FavouriteMoviesEntity>
    }
}