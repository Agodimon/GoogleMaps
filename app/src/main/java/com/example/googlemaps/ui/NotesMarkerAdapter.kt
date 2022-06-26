package com.example.googlemaps.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemaps.NotesMarker
import com.example.googlemaps.R
import com.example.googlemaps.databinding.LayoutNotesMarkerListItemBinding
import by.kirich1409.viewbindingdelegate.viewBinding


class CategoryAdapter(private val onCategoryClickListener: OnCategoryClickListener) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    var notesMarkerData = listOf<NotesMarker>()
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
            onCategoryClickListener.onClick(item)
        }
        holder.bind(onCategoryClickListener, item)

    }

    override fun getItemCount() = notesMarkerData.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding: LayoutNotesMarkerListItemBinding by viewBinding()

        fun bind(onCategoryClickListener: OnCategoryClickListener, item: NotesMarker) =
            with(viewBinding) {
                nameMarker.text = item.nameMarker
                address.text = item.address
                longitude.text = item.longitude
                latitude.text = item.latitude

            }

    }
}


