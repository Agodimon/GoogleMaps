package com.example.googlemaps.data.entities

import androidx.room.*
import com.example.googlemaps.data.RoomConstants


@Entity(tableName = RoomConstants.FRAGMENT_NOTES_MARKER_TABLE)
data class NotesMarkerSavedEntity(
    @PrimaryKey
    @ColumnInfo(name = "name marker")
    val nameMarker: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

)


