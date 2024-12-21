package com.rahul.safebite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rahul.safebite.databinding.ActivityViewHospitalBinding
import kotlin.math.*

class ViewHospitalActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityViewHospitalBinding
    private lateinit var mapView: MapView
    private var hospitalLatitude = 0.0
    private var hospitalLongitude = 0.0
    private var userLatitude = 0.0
    private var userLongitude = 0.0
    private var hospitalName = "Unknown Hospital"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewHospitalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hospitalName = intent.getStringExtra("hospitalName") ?: "Unknown Hospital"
        val hospitalAddress = intent.getStringExtra("hospitalAddress") ?: "Unknown Address"
        hospitalLatitude = intent.getDoubleExtra("hospitalLatitude", 0.0)
        hospitalLongitude = intent.getDoubleExtra("hospitalLongitude", 0.0)
        userLatitude = intent.getDoubleExtra("userLatitude", 0.0)
        userLongitude = intent.getDoubleExtra("userLongitude", 0.0)

        binding.tvHospitalName.text = hospitalName
        binding.tvHospitalAddress.text = hospitalAddress


        Log.d("LocationDebug", "User Lat: $userLatitude, User Lon: $userLongitude")
        Log.d("LocationDebug", "Hospital Lat: $hospitalLatitude, Hospital Lon: $hospitalLongitude")


        val distance = calculateDistance(userLatitude, userLongitude, hospitalLatitude, hospitalLongitude)
        Log.d("LocationDebug", "Calculated Distance: ${String.format("%.2f", distance)} km")
        binding.tvHospitalDistance.text = "Distance from your location: ${String.format("%.2f", distance)} km"

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)


        binding.btnViewOnMap.setOnClickListener {
            val uri = Uri.parse("google.navigation:q=$hospitalLatitude,$hospitalLongitude")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val hospitalLocation = LatLng(hospitalLatitude, hospitalLongitude)
        googleMap.addMarker(MarkerOptions().position(hospitalLocation).title(hospitalName))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospitalLocation, 15f))
    }

    private fun calculateDistance(
        userLat: Double,
        userLon: Double,
        hospitalLat: Double,
        hospitalLon: Double
    ): Double {
        val earthRadius = 6371.0 // radius in kilometers
        val dLat = Math.toRadians(hospitalLat - userLat)
        val dLon = Math.toRadians(hospitalLon - userLon)

        val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(userLat)) * cos(Math.toRadians(hospitalLat)) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
