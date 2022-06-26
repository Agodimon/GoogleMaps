package com.example.googlemaps.data.entities

import androidx.room.*
import com.example.googlemaps.data.RoomConstants


@Entity(tableName = RoomConstants.NOTES_MARKERS_TABLE)
data class NotesMakerEntity(
    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "id marker")
    var id: Int,

    @ColumnInfo(name = "name marker")
    val nameMarker: String,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "title food")
    val latitude: String,

    @ColumnInfo(name = "sourceName food")
    val longitude: String,

)


