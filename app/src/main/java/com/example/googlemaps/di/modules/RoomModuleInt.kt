package com.example.googlemaps.di.modules


import com.example.googlemaps.data.entities.NotesMarkerEntity
import com.example.googlemaps.data.entities.NotesMarkerSavedEntity

interface RoomModuleInt {
    fun insertNotesMarker(notesMaker: NotesMarkerEntity)
    fun getNotesMarker(): List<NotesMarkerEntity>
    fun insertSaveDescribeMarker(notesMaker: NotesMarkerSavedEntity)
}