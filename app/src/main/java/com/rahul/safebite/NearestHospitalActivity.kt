package com.rahul.safebite

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rahul.safebite.ExtraClasses.Hospital
import com.rahul.safebite.ExtraClasses.HospitalAdapter
import com.rahul.safebite.SkeletonView.LoadingAdapterNearestHospital
import com.rahul.safebite.databinding.ActivityNearestHospitalBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class NearestHospitalActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityNearestHospitalBinding
    private val hospitals = mutableListOf<Hospital>()
    private var userLatitude = 0.0
    private var userLongitude = 0.0

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearestHospitalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.recyclerViewHospitals.layoutManager = LinearLayoutManager(this)

        // 1) Show the loading adapter immediately
        binding.recyclerViewHospitals.adapter = LoadingAdapterNearestHospital()

        // 2) Then request location
        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED
        ) {
            fetchCurrentLocation()
        } else {
            Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                userLatitude = location.latitude
                userLongitude = location.longitude
                fetchHospitals(location)
            } else {
                Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
                // Optionally keep showing LoadingAdapter or show an error adapter
            }
        }
    }

    private fun fetchHospitals(location: Location) {
        val client = OkHttpClient()
        val apiKey = "DLf2nyLXSEK5OptkVFc6AGYFsNbJRxRF"
        val url = "https://api.tomtom.com/search/2/poiSearch/hospital.json" +
                "?lat=${location.latitude}&lon=${location.longitude}&radius=60000&key=$apiKey"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@NearestHospitalActivity,
                        "Failed to load hospitals", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@NearestHospitalActivity,
                            "Error: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                    return
                }

                response.body?.string()?.let { body ->
                    val json = JSONObject(body)
                    val results = json.getJSONArray("results")

                    hospitals.clear()
                    for (i in 0 until results.length()) {
                        val item = results.getJSONObject(i)
                        val poi = item.getJSONObject("poi")
                        val pos = item.getJSONObject("position")
                        val addr = item.getJSONObject("address")

                        val name = poi.optString("name", "Unnamed Hospital")
                        val address = addr.optString("freeformAddress", "")
                        val lat = pos.optDouble("lat", 0.0)
                        val lon = pos.optDouble("lon", 0.0)

                        val distArray = FloatArray(1)
                        android.location.Location.distanceBetween(
                            userLatitude, userLongitude, lat, lon, distArray
                        )

                        hospitals.add(Hospital(name, address, lat, lon, distArray[0]))
                    }
                    hospitals.sortBy { it.distance }

                    runOnUiThread {
                        // 3) Swap in your real data adapter
                        binding.recyclerViewHospitals.adapter =
                            HospitalAdapter(hospitals, userLatitude, userLongitude)
                    }
                }
            }
        })
    }
}
