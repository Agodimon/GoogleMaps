import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.googlemaps.R
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
import java.io.IOException


class MapsFragment : Fragment(R.layout.fragment_maps) {
    private val viewBinding: FragmentMapsBinding by viewBinding()
    private val LATITUDE = 44.952117
    private val LONGITUDE = 34.102417

    private lateinit var map: GoogleMap
    private var menu: Menu? = null

    private val markers: ArrayList<Marker> = ArrayList()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> }
    private val scopeIo = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler + SupervisorJob())
    private val scope = CoroutineScope(Dispatchers.Main + coroutineExceptionHandler + SupervisorJob())
    private var job: Job? = null

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


    private fun setMarker(location: LatLng, searchText: String) {
        map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
        )?.let { markers.add(it) }
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
