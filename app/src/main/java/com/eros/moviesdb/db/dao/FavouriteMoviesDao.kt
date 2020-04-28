package com.eros.moviesdb.db.dao

import androidx.room.*
import com.eros.moviesdb.db.entities.FavouriteMoviesEntity

@Dao
abstract class FavouriteMoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(favouriteMoviesEntity : FavouriteMoviesEntity)

    @Delete
    abstract fun delete(favouriteMoviesEntity : FavouriteMoviesEntity)

    @Query("select * from favourite")
    abstract fun getAllFavouriteMovies() : List<FavouriteMoviesEntity>?

}