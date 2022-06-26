package com.example.googlemaps.di.modules


import com.example.googlemaps.data.entities.NotesMarkerEntity

interface RoomModuleInt {
    fun insertNotesMarker(notesMaker: NotesMarkerEntity)
    fun getNotesMarker(): NotesMarkerEntity
}