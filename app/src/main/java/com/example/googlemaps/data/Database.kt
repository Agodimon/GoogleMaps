package com.example.googlemaps.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.googlemaps.data.dao.NotesMarkersDao
import com.example.googlemaps.data.entities.NotesMakerEntity


@Database(
    entities = [
        NotesMakerEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class Database : RoomDatabase(){
    abstract fun getNotesMarkers(): NotesMarkersDao


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
