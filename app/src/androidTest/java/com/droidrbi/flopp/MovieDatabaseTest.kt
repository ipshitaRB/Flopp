package com.droidrbi.flopp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.droidrbi.flopp.database.MovieDatabase
import com.droidrbi.flopp.database.MovieDatabaseDao
import com.droidrbi.flopp.database.model.Movie
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class MovieDatabaseTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var movieDao: MovieDatabaseDao
    private lateinit var db: MovieDatabase


    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        movieDao = db.movieDatabaseDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertPopularMoviesSavesData() = runBlocking {
        val movie1 = Movie(
            id = 125,
            overview = "overview",
            posterUrl = "",
            releaseDateString = "2014-12-23",
            title = "Flubber",
            voteAverage = 3.5,
            voteCount = 5
        )
        val movie2 = Movie(
            id = 100,
            overview = "overview2",
            posterUrl = "asdfaf",
            releaseDateString = "2014-12-23",
            title = "Flubber2",
            voteAverage = 3.5,
            voteCount = 5
        )

        val cachedMovies = listOf(movie1, movie2)

        movieDao.insertPopularMovies(cachedMovies)

        val retrievedMovies = movieDao.getAllMovies().getOrAwaitValue()

        assertThat(retrievedMovies, `is`(not(emptyList())))
    }

    @Test
    fun insertPopularMovies_listOfTwoMovies_listWithSameMovies() = runBlocking {

        val movie1 = Movie(
            id = 125,
            overview = "overview",
            posterUrl = "",
            releaseDateString = "2014-12-23",
            title = "Flubber",
            voteAverage = 3.5,
            voteCount = 5
        )
        val movie2 = Movie(
            id = 100,
            overview = "overview2",
            posterUrl = "asdfaf",
            releaseDateString = "2014-12-23",
            title = "Flubber2",
            voteAverage = 3.5,
            voteCount = 5
        )

        val cachedMovies = listOf(movie1, movie2)

        movieDao.insertPopularMovies(cachedMovies)

        val retrievedMovies = movieDao.getAllMovies().getOrAwaitValue()

        // assert list size is same
        assertThat(retrievedMovies.size, `is`(cachedMovies.size))

        // assert list content and order is same
        assertThat(retrievedMovies[0].title, `is`(cachedMovies[0].title))
        assertThat(retrievedMovies[0].releaseDateString, `is`(cachedMovies[0].releaseDateString))
        assertThat(retrievedMovies[0].posterUrl, `is`(cachedMovies[0].posterUrl))
        assertThat(retrievedMovies[0].overview, `is`(cachedMovies[0].overview))
        assertThat(retrievedMovies[0].id, `is`(cachedMovies[0].id))
        assertThat(retrievedMovies[0].voteAverage, `is`(cachedMovies[0].voteAverage))
        assertThat(retrievedMovies[0].voteCount, `is`(cachedMovies[0].voteCount))
        assertThat(retrievedMovies[1].title, `is`(cachedMovies[1].title))
        assertThat(retrievedMovies[1].releaseDateString, `is`(cachedMovies[1].releaseDateString))
        assertThat(retrievedMovies[1].posterUrl, `is`(cachedMovies[1].posterUrl))
        assertThat(retrievedMovies[1].overview, `is`(cachedMovies[1].overview))
        assertThat(retrievedMovies[1].id, `is`(cachedMovies[1].id))
        assertThat(retrievedMovies[1].voteAverage, `is`(cachedMovies[1].voteAverage))
        assertThat(retrievedMovies[1].voteCount, `is`(cachedMovies[1].voteCount))
    }

    @Test
    fun insertPopularMovies_nothing_emptyList() = runBlocking {
        val emptyCachedMovies = movieDao.getAllMovies().getOrAwaitValue()
        assertThat(emptyCachedMovies, `is`(emptyList()))
    }
}
