package com.droidrbi.flopp.fragments.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.droidrbi.flopp.R
import com.droidrbi.flopp.network.MovieApi
import com.droidrbi.flopp.network.NetworkUtil.Companion.isConnectedToNetwork
import com.droidrbi.flopp.network.models.PopularMovies
import com.droidrbi.flopp.network.models.Result
import com.droidrbi.flopp.repository.MovieRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel(
    context: Application
) : ViewModel() {

    private var _popularMovies = MutableLiveData<List<Result>>()

    val popularMovies: LiveData<List<Result>>
        get() = _popularMovies


    private var _isNetworkAvailable = MutableLiveData<Boolean>()

    val isNetworkAvailable: LiveData<Boolean>
        get() = _isNetworkAvailable

    private val movieRepository: MovieRepository
        get() = MovieRepository()


    init {
        Log.i(TAG, "ViewModel initialized")
        _isNetworkAvailable.value = true
        val apiKey = context.getString(R.string.api_key)
        if (isConnectedToNetwork(context)) {
            //getMovies(apiKey)
            _popularMovies = movieRepository.getMovies(apiKey)
            _isNetworkAvailable.value = true
        } else {
            _isNetworkAvailable.value = false
        }

        //_popularMovies.value = MovieRepository.getMovies()
    }


    private fun getMovies(apiKey: String) {

        MovieApi.retrofitService.getMovies(apiKey).enqueue(
            object : Callback<PopularMovies> {
                override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                    Log.d("ViewModel", "${t.message}")
                }

                override fun onResponse(
                    call: Call<PopularMovies>,
                    response: Response<PopularMovies>
                ) {
                    Log.i("ViewModel", _popularMovies.value.toString())
                    _popularMovies.value = response.body()!!.results
                }
            })
    }


    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel destroyed")
    }

    companion object {
        const val TAG = "MovieViewModel"
    }
}