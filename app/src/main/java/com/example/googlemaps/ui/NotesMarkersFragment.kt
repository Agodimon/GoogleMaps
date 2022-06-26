package com.example.googlemaps.ui

import android.os.Bundle
import android.util.Log
import android.util.Log.DEBUG
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.googlemaps.R
import com.example.googlemaps.databinding.FragmentNotesMarkerBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.googlemaps.data.entities.NotesMarkerEntity
import com.example.googlemaps.di.modules.RoomModuleInt
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class NotesMarkersFragment : Fragment(R.layout.fragment_notes_marker) {
    private val viewBinding: FragmentNotesMarkerBinding by viewBinding()
    private lateinit var adapter: CategoryListAdapter
    val repo: RoomModuleInt by inject()
    private var listNotesBD = emptyList<NotesMarkerEntity>()
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable -> Log.d(tag, "throwable:$throwable") }
    private val scopeIo =
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler + SupervisorJob())
    private val scopeMain =
        CoroutineScope(Dispatchers.Main + coroutineExceptionHandler + SupervisorJob())
    private var job: Job? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        job = scopeMain.launch() {

            listNotesBD = withContext(Dispatchers.IO) {
                return@withContext repo.getNotesMarker()
            }

            adapter.submitList(listNotesBD)
        }
    }

    private fun initRecyclerView() = with(viewBinding) {
        adapter = CategoryListAdapter(object : OnNotesClickListener {
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

