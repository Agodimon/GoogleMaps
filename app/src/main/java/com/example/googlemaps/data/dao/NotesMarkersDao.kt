package com.example.googlemaps.data.dao

import androidx.room.*
import com.example.googlemaps.data.RoomConstants.NOTES_MARKERS_TABLE
import com.example.googlemaps.data.entities.NotesMakerEntity


@Dao
interface NotesMarkersDao {
    @Query("SELECT * FROM $NOTES_MARKERS_TABLE")
    fun getNotesMarker(): NotesMakerEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotesMarker(notesMaker: NotesMakerEntity)

    @Delete
    suspend fun deleteNotesMarker(notesMaker: List<NotesMakerEntity>)

    @Query("DELETE FROM $NOTES_MARKERS_TABLE")
    suspend fun deleteAllNotesMarker()

}