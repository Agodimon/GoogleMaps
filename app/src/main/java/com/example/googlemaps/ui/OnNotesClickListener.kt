package com.example.googlemaps.ui

import com.example.googlemaps.data.entities.NotesMarkerEntity


interface OnNotesClickListener {
    fun onClick(category: NotesMarkerEntity)
}