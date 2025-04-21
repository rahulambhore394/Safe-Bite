package com.rahul.safebite

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.rahul.safebite.databinding.ActivityViewHospitalBinding
import kotlin.math.*

class ViewHospitalActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityViewHospitalBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private var hospitalLatitude = 0.0
    private var hospitalLongitude = 0.0
    private var userLatitude = 0.0
    private var userLongitude = 0.0
    private var hospitalName = "Unknown Hospital"
    private var hospitalAddress = "Unknown Address"

    private var userMarker: Marker? = null
    private val pathPoints = ArrayList<LatLng>()
    private var routePolyline: Polyline? = null
    private var firstCameraMove = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewHospitalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hospitalName = intent.getStringExtra("hospitalName") ?: "Unknown Hospital"
        hospitalAddress = intent.getStringExtra("hospitalAddress") ?: "Unknown Address"
        hospitalLatitude = intent.getDoubleExtra("hospitalLatitude", 0.0)
        hospitalLongitude = intent.getDoubleExtra("hospitalLongitude", 0.0)
        userLatitude = intent.getDoubleExtra("userLatitude", 0.0)
        userLongitude = intent.getDoubleExtra("userLongitude", 0.0)

        binding.tvHospitalName.text = hospitalName
        binding.tvHospitalAddress.text = hospitalAddress

        val distance = calculateDistance(userLatitude, userLongitude, hospitalLatitude, hospitalLongitude)
        binding.tvHospitalDistance.text = "Distance: ${String.format("%.2f", distance)} km"

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { updateUserLocation(it) }
            }
        }

        binding.btnViewOnMap.setOnClickListener {
            val uri = Uri.parse("geo:$hospitalLatitude,$hospitalLongitude?q=${Uri.encode(hospitalName)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val hospitalLatLng = LatLng(hospitalLatitude, hospitalLongitude)
        val userLatLng = LatLng(userLatitude, userLongitude)

        googleMap.addMarker(
            MarkerOptions()
                .position(hospitalLatLng)
                .title(hospitalName)
                .snippet(hospitalAddress)
        )

        userMarker = googleMap.addMarker(
            MarkerOptions()
                .position(userLatLng)
                .title("Your Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        )

        val boundsBuilder = LatLngBounds.builder()
        boundsBuilder.include(userLatLng)
        boundsBuilder.include(hospitalLatLng)
        val bounds = boundsBuilder.build()

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150))

        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000L)
            .setMinUpdateIntervalMillis(2000L)
            .build()
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun updateUserLocation(location: Location) {
        val newLatLng = LatLng(location.latitude, location.longitude)

        if (userMarker == null) {
            userMarker = googleMap.addMarker(
                MarkerOptions()
                    .position(newLatLng)
                    .title("Your Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
        } else {
            userMarker?.position = newLatLng
        }

        // Only move camera once to avoid zoom every time
        if (firstCameraMove) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 16f))
            firstCameraMove = false
        }

        pathPoints.add(newLatLng)

        routePolyline?.remove()
        routePolyline = googleMap.addPolyline(
            PolylineOptions()
                .addAll(pathPoints)
                .color(0xFF2196F3.toInt()) // Material Blue
                .width(8f)
                .geodesic(true)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun calculateDistance(userLat: Double, userLon: Double, hospitalLat: Double, hospitalLon: Double): Double {
        val earthRadius = 6371.0
        val dLat = Math.toRadians(hospitalLat - userLat)
        val dLon = Math.toRadians(hospitalLon - userLon)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(userLat)) * cos(Math.toRadians(hospitalLat)) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }
}
