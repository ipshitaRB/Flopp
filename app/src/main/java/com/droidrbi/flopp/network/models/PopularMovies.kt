package com.droidrbi.flopp.network.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class PopularMovies(
    val results: List<Result>
)

@Parcelize
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

) : Parcelable