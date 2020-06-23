package com.droidrbi.flopp.database

import com.droidrbi.flopp.database.model.Movie
import com.droidrbi.flopp.network.models.Result

class DatabaseUtil {

    companion object {


        fun convertToMovieList(resultList: List<Result>): List<Movie> {

            val movieList = resultList.map {
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

            return movieList
        }

        fun convertToResultList(movieList: List<Movie>): List<Result> {
            val resultList = movieList.map {
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

            return resultList
        }
    }
}