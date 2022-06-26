package com.example.googlemaps.di.modules

import com.example.googlemaps.application.App
import com.example.googlemaps.data.dao.NotesMarkersDao
import com.example.googlemaps.data.entities.NotesMarkerEntity
import com.example.googlemaps.data.entities.NotesMarkerSavedEntity


class RoomModule :RoomModuleInt{
    override fun insertNotesMarker(notesMaker: NotesMarkerEntity){
        App.instance.databaseService.getNotesMarkers().insertNotesMarker(notesMaker)
    }
    override fun getNotesMarker(): List<NotesMarkerEntity> {
        return App.instance.databaseService.getNotesMarkers().getNotesMarker()
    }
    override fun insertSaveDescribeMarker(notesMakerSavedEntity: NotesMarkerSavedEntity){
        App.instance.databaseService.getNotesMarkersSaved().insertSaveDescribeMarker(notesMakerSavedEntity)
    }
}