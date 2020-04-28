package com.eros.moviesdb.db

import androidx.room.Room
import com.eros.moviesdb.application.MyApp

object DBHelper {

    private var dbInstance_ : TMdbDatabase? = null

    fun getInstance() : TMdbDatabase {
        if (dbInstance_ == null) {
            dbInstance_ = Room.databaseBuilder(MyApp.INSTANCE_.applicationContext,
                TMdbDatabase::class.java,
                "tmdb_db.db")
                .allowMainThreadQueries()
                .build()
        }
        return dbInstance_ as TMdbDatabase
    }
}