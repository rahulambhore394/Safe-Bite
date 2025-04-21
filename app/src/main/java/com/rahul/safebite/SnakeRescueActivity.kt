package com.rahul.safebite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahul.safebite.SnakeRescue.SnakeCatcherAdapter
import com.rahul.safebite.SnakeRescue.SnakeCatcherViewModel
import com.rahul.safebite.databinding.ActivitySnakeRescueBinding

class SnakeRescueActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySnakeRescueBinding
    private val viewModel: SnakeCatcherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnakeRescueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.snakeCatchers.observe(this) { catchers ->
            binding.recyclerView.adapter = SnakeCatcherAdapter(catchers)
        }

        viewModel.fetchCatchers()
    }
}
