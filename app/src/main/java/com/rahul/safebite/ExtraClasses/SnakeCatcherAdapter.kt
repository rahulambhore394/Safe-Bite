package com.rahul.safebite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SnakeCatcherAdapter(private val snakeCatchers: List<SnakeCatcher>) :
    RecyclerView.Adapter<SnakeCatcherAdapter.SnakeCatcherViewHolder>() {

    class SnakeCatcherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val phoneTextView: TextView = itemView.findViewById(R.id.phoneTextView)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnakeCatcherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.snake_catcher_item, parent, false)
        return SnakeCatcherViewHolder(view)
    }

    override fun onBindViewHolder(holder: SnakeCatcherViewHolder, position: Int) {
        val snakeCatcher = snakeCatchers[position]
        holder.nameTextView.text = snakeCatcher.name
        holder.phoneTextView.text = snakeCatcher.phoneNumber
        holder.locationTextView.text = snakeCatcher.location
    }

    override fun getItemCount(): Int = snakeCatchers.size
}
