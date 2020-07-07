package com.droidrbi.flopp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.droidrbi.flopp.database.MovieDatabase
import com.droidrbi.flopp.database.model.asDomainModel
import com.droidrbi.flopp.network.MovieApi
import com.droidrbi.flopp.network.models.Result
import com.droidrbi.flopp.network.models.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Singleton
class MovieRepository(private val database: MovieDatabase) {

    val movies: LiveData<List<Result>> =
        Transformations.map(database.movieDatabaseDao().getAllMovies()) {
            it.asDomainModel()
        }

    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     */
    suspend fun refreshMovies(apiKey: String) {
        withContext(Dispatchers.IO) {
            Log.d("MovieRepository", "refresh videos is called")
            val popularMovies = MovieApi.retrofitService.getMovies(apiKey).await()
            Log.d("MovieRepository", "Number of movies = ${popularMovies.results.size}")
            database.movieDatabaseDao().insertPopularMovies(popularMovies.asDatabaseModel())

        }
    }
}
