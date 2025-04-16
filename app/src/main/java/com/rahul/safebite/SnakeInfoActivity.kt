package com.rahul.safebite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahul.safebite.SkeletonView.SnakeInfoLoadingAdapter
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

        // Setup RecyclerView with a loading skeleton view initially
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val skeletonAdapter = SnakeInfoLoadingAdapter()
        binding.recyclerView.adapter = skeletonAdapter

        // Observe the snake data from ViewModel
        viewModel.snakes.observe(this) { snakes ->
            if (snakes != null && snakes.isNotEmpty()) {
                // Set the real data adapter once the data is fetched
                binding.recyclerView.adapter = SnakeAdapter(snakes)
            }
        }

        // Trigger data fetch
        viewModel.fetchSnakes("snake india")
    }
}
