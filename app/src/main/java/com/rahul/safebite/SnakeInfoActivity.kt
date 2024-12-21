package com.rahul.safebite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahul.safebite.SnakeInfo.SnakeAdapter
import com.rahul.safebite.SnakeInfo.SnakeViewModel
import com.rahul.safebite.databinding.ActivitySnakeInfoBinding

class SnakeInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySnakeInfoBinding
    private val viewModel: SnakeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnakeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.snakes.observe(this) { snakes ->
            binding.recyclerView.adapter = SnakeAdapter(snakes)
        }

        viewModel.fetchSnakes("snake india")
    }
}
