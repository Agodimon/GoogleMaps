package com.example.googlemaps.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.googlemaps.R
import com.example.googlemaps.databinding.FragmentNotesMarkerBinding
import by.kirich1409.viewbindingdelegate.viewBinding

class NotesMarkersFragment : Fragment(R.layout.fragment_notes_marker) {
    private val viewBinding: FragmentNotesMarkerBinding by viewBinding()
    private lateinit var adapter: CategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() = with(viewBinding) {
        adapter = CategoryAdapter(object : OnCategoryClickListener {
            override fun onClick(notesMarker: NotesMarker) {
                navigateToUp()
            }
        })
        categoryListRecyclerView.adapter = adapter
    }

    private fun navigateToUp() {
        findNavController().navigateUp()
    }
}

