package com.example.googlemaps.di.modules

import com.example.googlemaps.aplication.App
import com.example.googlemaps.data.dao.NotesMarkersDao
import com.example.googlemaps.data.entities.NotesMarkerEntity


class RoomModule :RoomModuleInt{
    override fun insertNotesMarker(notesMaker: NotesMarkerEntity){
        App.instance.databaseService.getNotesMarkers().insertNotesMarker(notesMaker)
    }
    override fun getNotesMarker(): NotesMarkerEntity {
        return App.instance.databaseService.getNotesMarkers().getNotesMarker()
    }
}