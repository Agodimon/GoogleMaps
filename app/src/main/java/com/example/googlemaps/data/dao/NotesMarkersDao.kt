package com.example.googlemaps.data.dao

import androidx.room.*
import com.example.googlemaps.data.RoomConstants.NOTES_SAVED_MARKERS_TABLE


import com.example.googlemaps.data.entities.NotesMarkerEntity


@Dao
interface NotesMarkersDao {
    @Query("SELECT * FROM $NOTES_SAVED_MARKERS_TABLE")
    fun getNotesMarker(): List<NotesMarkerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotesMarker(notesMaker: NotesMarkerEntity)

    @Delete
    fun deleteNotesMarker(notesMaker: List<NotesMarkerEntity>)

    @Query("DELETE FROM $NOTES_SAVED_MARKERS_TABLE")
    fun deleteAllNotesMarker()

}