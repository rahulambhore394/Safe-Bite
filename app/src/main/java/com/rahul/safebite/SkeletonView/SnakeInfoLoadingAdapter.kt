package com.rahul.safebite.SkeletonView

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.rahul.safebite.R

class SnakeInfoLoadingAdapter : RecyclerView.Adapter<SnakeInfoLoadingAdapter.ViewHolder>() {

    private val shimmerItemCount = 12

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_snake_loading, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.animateShimmer(holder.snakeImage)
        holder.animateShimmer(holder.snakeName)
        holder.animateShimmer(holder.scientificName)
        holder.animateShimmer(holder.venomous)
        holder.animateShimmer(holder.foundIn)
    }

    override fun getItemCount(): Int = shimmerItemCount

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val snakeImage: View = itemView.findViewById(R.id.shimmer_snake_image)
        val snakeName: View = itemView.findViewById(R.id.shimmer_snake_name)
        val scientificName: View = itemView.findViewById(R.id.shimmer_scientific_name)
        val venomous: View = itemView.findViewById(R.id.shimmer_venomous)
        val foundIn: View = itemView.findViewById(R.id.shimmer_found_in)

        fun animateShimmer(view: View) {
            val fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.3f).apply {
                duration = 400
            }

            val fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0.3f, 1f).apply {
                duration = 400
            }

            val shimmerAnimator = AnimatorSet().apply {
                playSequentially(fadeOut, fadeIn)
                interpolator = LinearInterpolator()
            }

            // Loop forever manually
            shimmerAnimator.addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    shimmerAnimator.start()
                }
            })

            shimmerAnimator.start()
        }
    }
}
