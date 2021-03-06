package com.droidrbi.flopp.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.droidrbi.flopp.database.DatabaseConstants
import com.droidrbi.flopp.network.models.Result

@Entity(tableName = DatabaseConstants.TABLE_NAME)
data class Movie(

    @PrimaryKey(autoGenerate = true)
    val dbId: Long = 0L,

    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "overview")
    val overview: String,

    @ColumnInfo(name = "poster_path")
    val posterUrl: String,

    @ColumnInfo(name = "release_date")
    val releaseDateString: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,

    @ColumnInfo(name = "vote_count")
    val voteCount: Int

)

/**
 * Map DatabaseVideos to domain entities
 */
fun List<Movie>.asDomainModel(): List<Result> {
    return map {
        Result(
            it.id,
            it.overview,
            it.posterUrl,
            it.releaseDateString,
            it.title,
            it.voteAverage,
            it.voteCount
        )
    }
}