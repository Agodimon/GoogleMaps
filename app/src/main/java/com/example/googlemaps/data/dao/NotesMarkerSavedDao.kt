package com.example.googlemaps.data.dao

import androidx.room.*
import com.example.googlemaps.data.RoomConstants
import com.example.googlemaps.data.entities.NotesMarkerEntity

@Dao
interface NotesMarkerSavedDao {


        @Query("SELECT * FROM ${RoomConstants.FRAGMENT_NOTES_MARKER_TABLE}")
        fun getNotesMarker(): NotesMarkerEntity

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertNotesMarker(notesMaker: NotesMarkerEntity)

        @Delete
        fun deleteNotesMarker(notesMaker: List<NotesMarkerEntity>)

        @Query("DELETE FROM ${RoomConstants.FRAGMENT_NOTES_MARKER_TABLE}")
        fun deleteAllNotesMarker()


}