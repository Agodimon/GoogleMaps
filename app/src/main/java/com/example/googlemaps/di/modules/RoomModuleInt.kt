package com.example.googlemaps.di.modules

import com.example.googlemaps.data.entities.NotesMakerEntity

interface RoomModuleInt {
    fun insertNotesMarker(notesMaker: NotesMakerEntity)
}