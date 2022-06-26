package com.example.googlemaps.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemaps.R
import com.example.googlemaps.databinding.LayoutNotesMarkerListItemBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.googlemaps.data.entities.NotesMarkerEntity
import com.example.googlemaps.di.modules.RoomModuleInt
import kotlinx.coroutines.*
import org.koin.java.KoinJavaComponent.inject


class CategoryAdapter(private val onNotesClickListener: OnNotesClickListener) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var notesMarkerData = listOf<NotesMarkerEntity>()
        set(value) {
            field = value
            notifyItemChanged(itemCount)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_notes_marker_list_item, parent, false)
        )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = notesMarkerData[position]
        holder.itemView.setOnClickListener {
            onNotesClickListener.onClick(item)
        }
        holder.bind(onNotesClickListener, item)

    }

    override fun getItemCount() = notesMarkerData.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> }
        private val scopeIo =
            CoroutineScope(Dispatchers.IO + coroutineExceptionHandler + SupervisorJob())
        private val scopeMain =
            CoroutineScope(Dispatchers.Main + coroutineExceptionHandler + SupervisorJob())
        private val viewBinding: LayoutNotesMarkerListItemBinding by viewBinding()
        private val repo: RoomModuleInt by inject(repo::class.java)
        fun bind(onNotesClickListener: OnNotesClickListener, item: NotesMarkerEntity) =
            with(viewBinding) {
                scopeMain.launch {
                    scopeIo.launch {
                        nameMarker.text = repo.getNotesMarker().nameMarker
                        longitude.text = repo.getNotesMarker().longitude.toString()
                        latitude.text = repo.getNotesMarker().latitude.toString()
                    }
                }
            }
    }

}


