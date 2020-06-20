package com.droidrbi.flopp.network.models

import com.squareup.moshi.Json

data class PopularMovies(
    val results: List<Result>
)

data class Result(

    val id: Int,

    val overview: String,

    @Json(name = "poster_path")
    val posterUrl: String,

    @Json(name= "release_date")
    val releaseDateString: String,

    val title: String,

    @Json(name = "vote_average")
    val voteAverage: Double,

    @Json(name = "vote_count")
    val voteCount: Int

)