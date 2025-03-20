package com.rahul.safebite.ExtraClasses

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rahul.safebite.R
import com.rahul.safebite.ViewHospitalActivity
import kotlin.math.*

class HospitalAdapter(
    private val hospitals: List<Hospital>,
    private val userLatitude: Double,
    private val userLongitude: Double
) : RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hospital, parent, false)
        return HospitalViewHolder(view)
    }

    override fun onBindViewHolder(holder: HospitalViewHolder, position: Int) {
        val hospital = hospitals[position]
        holder.tvHospitalName.text = hospital.name
        holder.tvHospitalAddress.text = hospital.address
        holder.tvHospitalDistance.text = "Distance: ${calculateDistance(hospital.latitude, hospital.longitude)} km"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ViewHospitalActivity::class.java).apply {
                putExtra("hospitalName", hospital.name)
                putExtra("hospitalAddress", hospital.address)
                putExtra("hospitalLatitude", hospital.latitude)
                putExtra("hospitalLongitude", hospital.longitude)
                putExtra("userLatitude", userLatitude)  // Pass user's latitude
                putExtra("userLongitude", userLongitude)  // Pass user's longitude
            }
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = hospitals.size

    class HospitalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHospitalName: TextView = itemView.findViewById(R.id.tvHospitalName)
        val tvHospitalAddress: TextView = itemView.findViewById(R.id.tvHospitalAddress)
        val tvHospitalDistance: TextView = itemView.findViewById(R.id.tvHospitalDistance)
    }

    // Calculate distance between two points (Haversine formula)
    private fun calculateDistance(hospitalLat: Double, hospitalLon: Double): String {
        val earthRadius = 6371.0 // Radius of Earth in kilometers
        val dLat = Math.toRadians(hospitalLat - userLatitude)
        val dLon = Math.toRadians(hospitalLon - userLongitude)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(userLatitude)) * cos(Math.toRadians(hospitalLat)) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return String.format("%.2f", earthRadius * c) // Returns distance in km
    }
}
