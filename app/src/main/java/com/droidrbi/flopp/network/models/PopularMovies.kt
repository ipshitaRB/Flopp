package com.droidrbi.flopp.network.models

import android.os.Parcelable
import com.droidrbi.flopp.database.model.Movie
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class PopularMovies(
    var results: List<Result>
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

/**
 * Convert Network results to database objects
 */
fun PopularMovies.asDatabaseModel(): List<Movie> {
    return results.map {
        Movie(
            id = it.id,
            overview = it.overview,
            posterUrl = it.posterUrl,
            releaseDateString = it.releaseDateString,
            title = it.title,
            voteAverage = it.voteAverage,
            voteCount = it.voteCount
        )
    }
}