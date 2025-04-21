package com.rahul.safebite

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.rahul.safebite.chatBot.ChatBotActivity
import com.rahul.safebite.databinding.ActivityChatBotBinding
import com.rahul.safebite.databinding.FragmentHomeBinding
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var emergencyMobileNo1: String? = null
    private var emergencyMobileNo2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        displayUserInfo()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.cardFirstaid.setOnClickListener {
            startActivity(Intent(requireContext(), FirstAidActivity::class.java))
        }
        binding.cardNearestHospital.setOnClickListener {
            startActivity(Intent(requireContext(), NearestHospitalActivity::class.java))
        }
        binding.cardRescue.setOnClickListener {
            startActivity(Intent(requireContext(), SnakeRescueActivity::class.java))
        }
        binding.cardInformation.setOnClickListener {
            startActivity(Intent(requireContext(), SnakeInfoActivity::class.java))
        }

        binding.fbChatBot.setOnClickListener {
            startActivity(Intent(requireContext(), ChatBotActivity::class.java))
        }

        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.firstOrNull()?.let { showLocation(it) }
            }
        }

        binding.sosButtonLayout.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                sendEmergencyAlerts()
            } else {
                requestSMSPermissionLauncher.launch(Manifest.permission.SEND_SMS)
            }
        }

        requestLocationPermission()



        return binding.root
    }

    // Fetch user info from SharedPreferences
    private fun displayUserInfo() {
        val sharedPreferences = requireContext().getSharedPreferences("SafeBitePrefs", Context.MODE_PRIVATE)

        val userName = sharedPreferences.getString("userFullName", "Unknown User") ?: "Unknown User"
        val userMobile = sharedPreferences.getString("userMobile", "No mobile") ?: "No mobile"
        emergencyMobileNo1 = sharedPreferences.getString("Emergency No1", "") ?: ""
        emergencyMobileNo2 = sharedPreferences.getString("Emergency No2", "") ?: ""

        binding.tvUserFullnameHome.text = userName
        binding.tvMobileNoHome.text = userMobile
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationUpdates()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startLocationUpdates()
            } else {
                Toast.makeText(requireContext(), "Location permission is required", Toast.LENGTH_SHORT).show()
            }
        }

    private val requestSMSPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Toast.makeText(requireContext(), "SMS permission is required", Toast.LENGTH_SHORT).show()
            }
        }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                requireActivity().mainLooper
            )
        }
    }

    private fun sendEmergencyAlerts() {
        val userName = binding.tvUserFullnameHome.text.toString()

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                val addressText = if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    "${address.thoroughfare ?: "Unknown Street"}, ${address.locality ?: "Unknown City"}, ${address.adminArea ?: "Unknown State"}, ${address.countryName ?: "Unknown Country"}"
                } else {
                    "Location not available"
                }

                val message = """
                    🚨 EMERGENCY ALERT 🚨
                    
                    Name: $userName
                    Emergency Type: Snake Bite
                    Location: $addressText
                    Google Maps: https://maps.google.com/?q=${location.latitude},${location.longitude}
                    
                    Please send help immediately!
                """.trimIndent()

                sendWhatsAppMessages(message)
                sendSMSMessages(message)
            } else {
                Toast.makeText(requireContext(), "Could not fetch location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendWhatsAppMessages(message: String) {
        val emergencyContacts = mutableListOf<String>()
        if (!emergencyMobileNo1.isNullOrBlank()) emergencyContacts.add(emergencyMobileNo1!!)
        if (!emergencyMobileNo2.isNullOrBlank()) emergencyContacts.add(emergencyMobileNo2!!)

        if (emergencyContacts.isEmpty()) {
            Toast.makeText(requireContext(), "No emergency contacts found", Toast.LENGTH_SHORT).show()
            return
        }

        for (contact in emergencyContacts) {
            try {
                val uri = Uri.parse("https://api.whatsapp.com/send?phone=$contact&text=${Uri.encode(message)}")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.whatsapp")
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("WhatsAppError", "Error sending WhatsApp message", e)
                Toast.makeText(requireContext(), "WhatsApp not installed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendSMSMessages(message: String) {
        val emergencyContacts = mutableListOf<String>()
        if (!emergencyMobileNo1.isNullOrBlank()) emergencyContacts.add(emergencyMobileNo1!!)
        if (!emergencyMobileNo2.isNullOrBlank()) emergencyContacts.add(emergencyMobileNo2!!)

        if (emergencyContacts.isEmpty()) {
            Toast.makeText(requireContext(), "No emergency contacts found", Toast.LENGTH_SHORT).show()
            return
        }

        val smsManager = SmsManager.getDefault()

        for (contact in emergencyContacts) {
            try {
                smsManager.sendTextMessage(contact, null, message, null, null)
                Toast.makeText(requireContext(), "SOS SMS Sent to $contact", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("SMSError", "Error sending SMS", e)
                Toast.makeText(requireContext(), "Failed to send SMS to $contact", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLocation(location: Location) {
        // Optional: Show location details in UI or toast.
    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


