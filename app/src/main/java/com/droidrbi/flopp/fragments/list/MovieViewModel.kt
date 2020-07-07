package com.droidrbi.flopp.fragments.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.droidrbi.flopp.R
import com.droidrbi.flopp.database.MovieDatabase
import com.droidrbi.flopp.network.models.Result
import com.droidrbi.flopp.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

class MovieViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val movieRepository = MovieRepository(MovieDatabase.getDatabase(application))

    val popularMovies: LiveData<List<Result>> = movieRepository.movies

    private val viewModelJob = SupervisorJob()


    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    /**
     * Event triggered for network error. Views should use this to get access
     * to the data.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    /**
     * Flag to display the error message. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    /**
     * Flag to display the error message. Views should use this to get access
     * to the data.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        Log.i(TAG, "ViewModel initialized")
        val apiKey = application.getString(R.string.api_key)
        refreshDataFromRepository(apiKey)

    }

    private fun refreshDataFromRepository(apiKey: String) {
        viewModelScope.launch {
            try {
                movieRepository.refreshMovies(apiKey)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if (popularMovies.value.isNullOrEmpty())
                    _eventNetworkError.value = true
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel destroyed")
        viewModelJob.cancel()
    }

    companion object {
        const val TAG = "MovieViewModel"
    }
}