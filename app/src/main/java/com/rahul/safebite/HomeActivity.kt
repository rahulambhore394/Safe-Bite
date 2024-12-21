package com.rahul.safebite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rahul.safebite.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.fragmentContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userName = intent.getStringExtra("userName") ?: "Unknown User"
        val userMobile = intent.getStringExtra("userMobile") ?: "No mobile"
        val bundle = Bundle().apply {
            putString("userName", userName)
            putString("userMobile", userMobile)
        }
        val homeFragment = HomeFragment().apply {
            arguments = bundle
        }
        loadFragment(homeFragment)
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    loadFragment(homeFragment) // Reuse the home fragment with user data
                    true
                }
                R.id.navigation_identify -> loadFragment(IdentifyFragment())
                R.id.navigation_map -> loadFragment(MapFragment())
                else -> false
            }
        }
    }
    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        return true
    }
}
