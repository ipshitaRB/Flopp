package com.droidrbi.flopp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.droidrbi.flopp.database.model.Movie

@Dao
interface MovieDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPopularMovies(movies: List<Movie>)

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_NAME}")
    fun getAllMovies(): LiveData<List<Movie>>

}