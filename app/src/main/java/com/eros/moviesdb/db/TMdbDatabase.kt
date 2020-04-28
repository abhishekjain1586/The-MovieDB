package com.eros.moviesdb.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eros.moviesdb.db.dao.FavouriteMoviesDao
import com.eros.moviesdb.db.entities.FavouriteMoviesEntity

@Database(entities = arrayOf(FavouriteMoviesEntity::class), version = 1, exportSchema = false)
abstract class TMdbDatabase : RoomDatabase() {

    abstract fun getFavouriteMoviesDao() : FavouriteMoviesDao
}