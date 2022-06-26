package com.example.googlemaps.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemaps.R
import com.example.googlemaps.databinding.LayoutNotesMarkerListItemBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.googlemaps.data.entities.NotesMarkerEntity
import com.example.googlemaps.di.modules.RoomModuleInt
import kotlinx.coroutines.*
import org.koin.java.KoinJavaComponent.inject


class CategoryListAdapter(private val onNotesClickListener: OnNotesClickListener) :
    ListAdapter<NotesMarkerEntity, CategoryListAdapter.CategoryViewHolder>(NotesMarkerDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_notes_marker_list_item, parent, false)
        )



    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener { onNotesClickListener.onClick(item) }
        holder.bind(item)

    }


    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding: LayoutNotesMarkerListItemBinding by viewBinding()
        fun bind(notesMakerEntity: NotesMarkerEntity) =
            with(viewBinding) {
                nameMarker.text = notesMakerEntity.nameMarker
                longitude.text = notesMakerEntity.longitude.toString()
                latitude.text = notesMakerEntity.latitude.toString()
            }
    }


}


