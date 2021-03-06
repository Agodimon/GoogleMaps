package com.example.googlemaps.data.entities

import androidx.room.*
import com.example.googlemaps.data.RoomConstants


@Entity(tableName = RoomConstants.NOTES_SAVED_MARKERS_TABLE)
data class NotesMarkerEntity(
    @PrimaryKey
    @ColumnInfo(name = "name marker")
    val nameMarker: String,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

)


