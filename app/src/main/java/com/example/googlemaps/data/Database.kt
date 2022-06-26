package com.example.googlemaps.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.googlemaps.data.dao.NotesMarkerSavedDao
import com.example.googlemaps.data.dao.NotesMarkersDao

import com.example.googlemaps.data.entities.NotesMarkerEntity
import com.example.googlemaps.data.entities.NotesMarkerSavedEntity


@Database(
    entities = [
        NotesMarkerSavedEntity::class,
        NotesMarkerEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun getNotesMarkers(): NotesMarkersDao
    abstract fun getNotesMarkersSaved(): NotesMarkerSavedDao


    companion object {
        @Volatile
        private var INSTANCE: com.example.googlemaps.data.Database? = null

        fun createDatabase(context: Context): com.example.googlemaps.data.Database =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                com.example.googlemaps.data.Database::class.java,
                RoomConstants.DATABASE_NAME
            ).build()
    }
}
