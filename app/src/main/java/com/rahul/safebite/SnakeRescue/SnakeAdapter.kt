package com.rahul.safebite.SnakeRescue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rahul.safebite.R

class SnakeCatcherAdapter(private val catchers: List<SnakeCatcher>) :
    RecyclerView.Adapter<SnakeCatcherAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.nameText)
        val phoneText: TextView = view.findViewById(R.id.phoneText)
        val locationText: TextView = view.findViewById(R.id.locationText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_snake_catcher, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = catchers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val catcher = catchers[position]
        holder.nameText.text = catcher.name
        holder.phoneText.text = catcher.phone
        holder.locationText.text = catcher.location
    }
}
