package com.droidrbi.flopp.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.droidrbi.flopp.database.model.Movie

@Database(
    entities = [Movie::class],
    version = 2
)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDatabaseDao(): MovieDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(application: Application): MovieDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    application.applicationContext,
                    MovieDatabase::class.java,
                    DatabaseConstants.DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }


}
