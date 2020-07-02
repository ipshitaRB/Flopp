package com.droidrbi.flopp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.droidrbi.flopp.network.MovieApi
import com.droidrbi.flopp.network.models.PopularMovies
import com.droidrbi.flopp.network.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Singleton

@Singleton
class MovieRepository {

    fun getMovies(apiKey: String): MutableLiveData<List<Result>> {
        val data = MutableLiveData<List<Result>>()
        MovieApi.retrofitService.getMovies(apiKey).enqueue(
            object : Callback<PopularMovies> {
                override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                    Log.d("ViewModel", "${t.message}")
                }

                override fun onResponse(
                    call: Call<PopularMovies>,
                    response: Response<PopularMovies>
                ) {
                    Log.i("ViewModel", "data received")
                    data.value = response.body()!!.results
                }
            })

        return data
    }
}
