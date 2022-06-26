package com.example.googlemaps.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.googlemaps.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.*
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.googlemaps.R
import com.example.googlemaps.data.entities.NotesMarkerEntity
import com.example.googlemaps.di.modules.RoomModuleInt
import org.koin.android.ext.android.inject
import java.io.IOException


class MapsFragment : Fragment(R.layout.fragment_maps) {
    private val viewBinding: FragmentMapsBinding by viewBinding()
    private val LATITUDE = 44.952117
    private val LONGITUDE = 34.102417
    private val MIN_TIME_MS = 5000L
    private val MIN_DISTANCE_M = 10f
    private lateinit var map: GoogleMap
    private var menu: Menu? = null
    val repo: RoomModuleInt by inject()
    private val markers: ArrayList<Marker> = ArrayList()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> }
    private val scopeIo =
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler + SupervisorJob())
    private val scope =
        CoroutineScope(Dispatchers.Main + coroutineExceptionHandler + SupervisorJob())
    private var job: Job? = null
    private val permissionResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result) {
            getLocation()
        } else {
            Toast.makeText(
                context, getString(R.string.need_permissions_to_find_location),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkPermission() {
        activity?.let {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    getLocation()
                }
                else -> {
                    permissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    // получаем текущее местоположение
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val locationManager = // локация определяется через locationManager
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
// Проверка включено ли местоположенире
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
            provider?.let {
                // Будем получать геоположение через каждые 60 секунд или каждые 100 метров
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_MS,
                    MIN_DISTANCE_M,
                    onLocationListener
                )
            }

        } else {
            val location =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) // запрос последнего известного местоположения в системе
            if (location == null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.looks_like_location_disabled),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                getAddressAsync(location)
            }
        }
    }

    private val onLocationListener = LocationListener { location -> getAddressAsync(location) }

    //Тема геокодер
    private fun getAddressAsync(location: Location) = with(viewBinding) {
        val geoCoder = Geocoder(context)
        job = scopeIo.launch(Dispatchers.IO) {
            try {
                val addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
                withContext(Dispatchers.Main) {
                    showAddressDialog(addresses[0].getAddressLine(0), location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_take_a_note)) { _, _ ->
                    navigateToNotesMarkersFragment()
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        }

        val initialPlace = LatLng(LATITUDE, LONGITUDE)
        val marker = googleMap.addMarker(
            MarkerOptions().position(initialPlace).title(getString(R.string.marker_start))
        )
        marker?.let { markers.add(it) }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initialPlace))
        googleMap.setOnMapLongClickListener { latLng ->
            setMarker(latLng, "From long click")
            drawLine()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearchByAddress()
        viewBinding.includedMapsFragment.mainFragmentFABLocation.setOnClickListener { checkPermission() }
        viewBinding.includedMapsFragment.fabSavedNotesGeolocation.setOnClickListener { navigateToNotesMarkersFragment() }
    }

    override fun onDestroyView() {
        menu?.findItem(R.id.menu_google_maps)?.isVisible = true
        super.onDestroyView()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.map_menu, menu)
        this.menu = menu
//        menu.findItem(R.id.menu_google_maps).isVisible = false
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_map_mode_normal -> {
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
                return true
            }
            R.id.menu_map_mode_satellite -> {
                map.mapType = GoogleMap.MAP_TYPE_SATELLITE
                return true
            }
            R.id.menu_map_mode_terrain -> {
                map.mapType = GoogleMap.MAP_TYPE_TERRAIN
                return true
            }
            R.id.menu_map_traffic -> {
                map.isTrafficEnabled = !map.isTrafficEnabled
                return true
            }
        }

        return false
    }

    private fun initSearchByAddress() = with(viewBinding) {
        buttonSearch.setOnClickListener {
            val geoCoder = Geocoder(it.context)
            val searchText = searchAddress.text.toString()
            job = scopeIo.launch(Dispatchers.IO) {
                try {
                    val addresses = geoCoder.getFromLocationName(searchText, 1)
                    if (addresses.isNotEmpty()) {
                        goToAddress(addresses, it, searchText)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun goToAddress(addresses: MutableList<Address>, view: View, searchText: String) {
        val location = LatLng(addresses[0].latitude, addresses[0].longitude)
        job = scope.launch {
            setMarker(location, searchText)
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    location,
                    15f
                )
            )
        }
    }

    private fun navigateToNotesMarkersFragment() {
        findNavController().navigate(R.id.action_mapsFragment_to_notesMarkersFragment)
    }


    private fun setMarker(location: LatLng, searchText: String) {
        map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
        )?.let { markers.add(it) }
        val notesMarker = NotesMarkerEntity(

            nameMarker = searchText,
            latitude = location.latitude,
            longitude = location.longitude

        )
        scopeIo.launch { repo.insertNotesMarker(notesMarker) }

    }


    private fun drawLine() {
        val last: Int = markers.size - 1
        if (last >= 1) {
            val previous: LatLng = markers[last - 1].position
            val current: LatLng = markers[last].position
            map.addPolyline(
                PolylineOptions()
                    .add(previous, current)
                    .color(Color.RED)
                    .width(5f)
            )
        }
    }

    override fun onDestroy() {
        scopeIo.cancel()
        super.onDestroy()
    }

}


