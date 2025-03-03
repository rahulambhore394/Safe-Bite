package com.rahul.safebite

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class FirstAidActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_aid)

        val toggleButton = findViewById<ToggleButton>(R.id.toggleButton)
        val container = findViewById<FrameLayout>(R.id.container)


        val inflater = LayoutInflater.from(this)
        var currentView = inflater.inflate(R.layout.first_aid_layout, container, false)
        container.addView(currentView)

        toggleButton.setOnCheckedChangeListener { _, isChecked ->

            container.removeView(currentView)
            currentView = if (isChecked) {
                inflater.inflate(R.layout.symtomps_layout, container, false)
            } else {
                inflater.inflate(R.layout.first_aid_layout, container, false)
            }

            container.addView(currentView)
        }
    }
}
