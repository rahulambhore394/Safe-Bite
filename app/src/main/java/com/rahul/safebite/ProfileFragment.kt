package com.rahul.safebite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rahul.safebite.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        loadUserProfile()

        binding.btnLogout.setOnClickListener{
            logoutUser()
        }

        return binding.root
    }

    private fun loadUserProfile() {
        val sharedPreferences = requireContext().getSharedPreferences("SafeBitePrefs", Context.MODE_PRIVATE)

        val fullName = sharedPreferences.getString("userFullName", "Not available")
        val username = sharedPreferences.getString("userName", "Not available")
        val mobile = sharedPreferences.getString("userMobile", "Not available")
        val emergencyNo1 = sharedPreferences.getString("Emergency No1", "Not available")
        val emergencyNo2 = sharedPreferences.getString("Emergency No2", "Not available")

        binding.name.text = fullName
        binding.etUsername.text = username
        binding.etMobileNo.text = mobile
        binding.etEmergencyMobileNo1.text = emergencyNo1
        binding.etEmergencyMobileNo2.text = emergencyNo2

       // Toast.makeText(requireContext(),emergencyNo1.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun logoutUser() {

        val sharedPreferences = requireContext().getSharedPreferences("SafeBitePrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
