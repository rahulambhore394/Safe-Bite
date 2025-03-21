package com.rahul.safebite.SkeletonView

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.rahul.safebite.R

class LoadingAdapterNearestHospital : RecyclerView.Adapter<LoadingAdapterNearestHospital.ViewHolder>() {

    private val shimmerItemCount = 12 // Number of shimmer items shown

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hospital_loading, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.animateShimmer(holder.shimmerCircle)
        holder.animateShimmer(holder.shimmerLine1)
        holder.animateShimmer(holder.shimmerLine2)
    }

    override fun getItemCount(): Int = shimmerItemCount

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shimmerCircle: View = itemView.findViewById(R.id.shimmer_circle)
        val shimmerLine1: View = itemView.findViewById(R.id.shimmer_line1)
        val shimmerLine2: View = itemView.findViewById(R.id.shimmer_line2)

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
