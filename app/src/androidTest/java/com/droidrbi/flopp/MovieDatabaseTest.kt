package com.droidrbi.flopp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.droidrbi.flopp.database.MovieDatabase
import com.droidrbi.flopp.database.MovieDatabaseDao
import com.droidrbi.flopp.database.model.Movie
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MovieDatabaseTest {

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
    fun insertAndGetMovies() = runBlocking {

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

        val movieList = listOf(movie1, movie2)

        movieDao.insertPopularMovies(movieList)

        val insertedMovieList = movieDao.getAllMovies()

        assertEquals(
            "Inserted movie list does not match the one retrieved",
            movieList[0].id,
            insertedMovieList[0].id
        )


    }

    @Test
    fun emptyDatabaseTest() = runBlocking {
        val emptyMovieList = movieDao.getAllMovies()
        assertEquals("List is not empty", 0, emptyMovieList.size)
    }

    @Test
    fun compareListTest() {
        // compare without using dbId
    }

}
