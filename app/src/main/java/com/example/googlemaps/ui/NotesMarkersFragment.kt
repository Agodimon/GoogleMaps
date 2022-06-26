package com.example.googlemaps.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.googlemaps.R
import com.example.googlemaps.databinding.FragmentNotesMarkerBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.googlemaps.data.entities.NotesMarkerEntity
import com.example.googlemaps.di.modules.RoomModuleInt
import org.koin.android.ext.android.inject

class NotesMarkersFragment : Fragment(R.layout.fragment_notes_marker) {
    private val viewBinding: FragmentNotesMarkerBinding by viewBinding()
    private lateinit var adapter: CategoryAdapter
    val repo: RoomModuleInt by inject()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() = with(viewBinding) {
        adapter = CategoryAdapter(object : OnNotesClickListener {
            override fun onClick(notesMarker: NotesMarkerEntity) {
                navigateToUp()
            }
        })
        categoryListRecyclerView.adapter = adapter

    }

    private fun navigateToUp() {
        findNavController().navigateUp()
    }
}

