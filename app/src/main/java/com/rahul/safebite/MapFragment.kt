package com.rahul.safebite

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rahul.safebite.databinding.FragmentMapBinding

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val locationViewModel: LocationViewModel by activityViewModels()
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationViewModel.currentLocation.observe(viewLifecycleOwner) { location ->
            location?.let { updateMapLocation(it) }
        }

        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

    private fun updateMapLocation(location: Location) {
        val currentLatLng = LatLng(location.latitude, location.longitude)
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(currentLatLng).title("You are here"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
