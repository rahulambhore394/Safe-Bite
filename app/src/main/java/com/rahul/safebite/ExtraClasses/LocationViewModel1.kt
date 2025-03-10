package com.rahul.safebite

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LocationViewModel1(application: Application) : AndroidViewModel(application) {

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> get() = _location

    fun updateLocation(newLocation: Location) {
        _location.value = newLocation
    }
}
