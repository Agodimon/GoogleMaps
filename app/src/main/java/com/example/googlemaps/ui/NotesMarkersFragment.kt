package com.example.googlemaps.ui

import android.os.Bundle
import android.util.Log
import android.util.Log.DEBUG
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.googlemaps.R
import com.example.googlemaps.databinding.FragmentNotesMarkerBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.googlemaps.data.entities.NotesMarkerEntity
import com.example.googlemaps.data.entities.NotesMarkerSavedEntity
import com.example.googlemaps.di.modules.RoomModuleInt
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.core.component.getScopeId

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

    fun saveDescribeToDB(notesMarkerSavedEntity:NotesMarkerSavedEntity){

        val savedEntity = NotesMarkerSavedEntity(
            nameMarker =  notesMarkerSavedEntity.nameMarker,
            description = notesMarkerSavedEntity.description,
            latitude = notesMarkerSavedEntity.latitude,
            longitude = notesMarkerSavedEntity.longitude
        )
        scopeIo.launch { repo.insertSaveDescribeMarker(savedEntity) }
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

