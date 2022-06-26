package com.example.googlemaps.di.modules

import com.example.googlemaps.aplication.App
import com.example.googlemaps.data.entities.NotesMakerEntity

class RoomModule :RoomModuleInt{
    override fun insertNotesMarker(notesMaker:NotesMakerEntity){
        App.instance.databaseService.getNotesMarkers().insertNotesMarker(notesMaker)
    }
}