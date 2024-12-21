package com.rahul.safebite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rahul.safebite.databinding.ActivitySnakeDetailsBinding

class SnakeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySnakeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnakeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val snakeName = intent.getStringExtra("NAME") ?: "Unknown"
        val scientificName = intent.getStringExtra("SCIENTIFIC_NAME") ?: "Unknown"
        val imageUrl = intent.getStringExtra("IMAGE_URL") ?: ""
        val isVenomous = intent.getBooleanExtra("VENOMOUS", false)
        val foundIn = intent.getStringExtra("FOUND_IN") ?: "Unknown"

        binding.tvSnakeName.text = snakeName
        binding.tvScientificName.text = scientificName
        binding.tvVenomous.text = if (isVenomous) "Venomous" else "Non-venomous"
        binding.tvFoundIn.text = foundIn
        Glide.with(this).load(imageUrl).into(binding.ivSnakeImage)
    }
}
