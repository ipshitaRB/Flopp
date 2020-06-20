package com.droidrbi.flopp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.droidrbi.flopp.network.MovieApi
import com.droidrbi.flopp.network.models.PopularMovies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPopularMovies()
    }

    private fun getPopularMovies() {
        MovieApi.retrofitService.getMovies(getString(R.string.api_key)).enqueue(
            object: Callback<PopularMovies> {
                override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                    Log.d("MainActivity", "Failure: " + t.message)
                }

                override fun onResponse(call: Call<PopularMovies>, response: Response<PopularMovies>) {
                    Log.d("MainActivity","Success: ${response.body()?.results?.size} movies retrieved")
                }
            })

    }
}