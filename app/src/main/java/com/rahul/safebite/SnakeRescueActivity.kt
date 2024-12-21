package com.rahul.safebite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahul.safebite.databinding.ActivitySnakeRescueBinding

class SnakeRescueActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySnakeRescueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySnakeRescueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val snakeCatchers = listOf(
            SnakeCatcher("John Doe", "123-456-7890", "Nanded"),
            SnakeCatcher("Rahul Sharma", "987-654-3210", "Aurangabad"),
            SnakeCatcher("Anjali Patel", "555-555-5555", "Pune")
        )


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = SnakeCatcherAdapter(snakeCatchers)
    }
}
