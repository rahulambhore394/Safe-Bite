package com.rahul.safebite

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rahul.safebite.ExtraClasses.Hospital
import com.rahul.safebite.ExtraClasses.HospitalAdapter
import com.rahul.safebite.databinding.ActivityNearestHospitalBinding
import okhttp3.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class NearestHospitalActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityNearestHospitalBinding
    private val hospitals = mutableListOf<Hospital>()
    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearestHospitalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.recyclerViewHospitals.layoutManager = LinearLayoutManager(this)

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

    private fun fetchCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                userLatitude = location.latitude
                userLongitude = location.longitude
                fetchHospitals(location)
            } else {
                Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchHospitals(location: Location) {
        val overpassQuery = """
        [out:json];
        node["amenity"="hospital"](around:5000, ${location.latitude}, ${location.longitude});
        out;
    """.trimIndent()

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://overpass-api.de/api/interpreter?data=${Uri.encode(overpassQuery)}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("HTTP_ERROR", "Failed to fetch hospitals", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("HTTP_ERROR", "Unexpected code $response")
                    return
                }

                response.body?.let { responseBody ->
                    val json = JSONObject(responseBody.string())
                    val elements = json.getJSONArray("elements")

                    hospitals.clear()
                    for (i in 0 until elements.length()) {
                        val hospital = elements.getJSONObject(i)
                        val name = hospital.optJSONObject("tags")?.optString("name", "Unnamed Hospital") ?: "Unnamed Hospital"
                        val address = hospital.optJSONObject("tags")?.optString("addr:full", "Address not available") ?: "Address not available"
                        val latitude = hospital.optDouble("lat", 0.0)
                        val longitude = hospital.optDouble("lon", 0.0)


                        val distance = FloatArray(1)
                        Location.distanceBetween(
                            userLatitude, userLongitude,
                            latitude, longitude,
                            distance
                        )

                        hospitals.add(Hospital(name, address, latitude, longitude, distance[0]))
                    }

                    hospitals.sortBy { it.distance }

                    runOnUiThread {
                        binding.recyclerViewHospitals.adapter = HospitalAdapter(hospitals, userLatitude, userLongitude)
                    }
                }
            }
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                fetchCurrentLocation()
            } else {
                Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
