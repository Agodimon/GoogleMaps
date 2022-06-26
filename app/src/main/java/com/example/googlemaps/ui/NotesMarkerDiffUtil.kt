package com.example.googlemaps.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.googlemaps.data.entities.NotesMarkerEntity

class NotesMarkerDiffUtil(
) : DiffUtil.ItemCallback<NotesMarkerEntity>() {
    override fun areItemsTheSame(
        oldItem: NotesMarkerEntity,
        newItem: NotesMarkerEntity
    ) = oldItem === newItem

    override fun areContentsTheSame(
        oldItem: NotesMarkerEntity,
        newItem: NotesMarkerEntity
    ) = oldItem == newItem
}