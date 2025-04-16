package com.rahul.safebite.SnakeInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rahul.safebite.databinding.ItemSnakeDeatailsBinding

class SnakeAdapter(
    private val snakeList: List<Snake>
) : RecyclerView.Adapter<SnakeAdapter.SnakeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnakeViewHolder {
        val binding = ItemSnakeDeatailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SnakeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SnakeViewHolder, position: Int) {
        val snake = snakeList[position]
        holder.bind(snake)
    }

    override fun getItemCount(): Int = snakeList.size

    class SnakeViewHolder(private val binding: ItemSnakeDeatailsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(snake: Snake) {
            binding.tvSnakeName.text = "Name: ${snake.commonName ?: snake.scientificName}"
            binding.tvScientificName.text = "Scientific Name: ${snake.scientificName}"
            binding.tvVenomous.text = "Venomous: ${if (snake.scientificName.contains("venom", true)) "Yes" else "No"}"
            binding.tvFoundIn.text = "Found in: India"  // Example static info
            Glide.with(binding.root.context).load(snake.imageUrl).into(binding.ivSnakeImage)
        }
    }
}