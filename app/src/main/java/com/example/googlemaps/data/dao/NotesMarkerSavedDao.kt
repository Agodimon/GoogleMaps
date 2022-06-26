package com.example.googlemaps.data.dao

import androidx.room.*
import com.example.googlemaps.data.RoomConstants
import com.example.googlemaps.data.entities.NotesMarkerEntity
import com.example.googlemaps.data.entities.NotesMarkerSavedEntity

@Dao
interface NotesMarkerSavedDao {


        @Query("SELECT * FROM ${RoomConstants.FRAGMENT_NOTES_MARKER_TABLE}")
        fun getSaveDescribeMarker(): NotesMarkerSavedEntity

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertSaveDescribeMarker(notesMaker: NotesMarkerSavedEntity)

        @Delete
        fun deleteNotesMarker(notesMaker: List<NotesMarkerSavedEntity>)

        @Query("DELETE FROM ${RoomConstants.FRAGMENT_NOTES_MARKER_TABLE}")
        fun deleteAllNotesMarker()


}