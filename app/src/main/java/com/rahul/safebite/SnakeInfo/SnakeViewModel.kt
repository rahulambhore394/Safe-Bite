package com.rahul.safebite.SnakeInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rahul.safebite.RetrofitClient
import kotlinx.coroutines.launch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SnakeViewModel : ViewModel() {
    val snakes = MutableLiveData<List<Snake>>()

    fun fetchSnakes(query: String) {
        viewModelScope.launch {
            val response = RetrofitClient.apiService.getSnakes(query)
            if (response.isSuccessful) {
                val results = response.body()?.results ?: emptyList()
                val snakeList = results.map {
                    Snake(
                        id = it.id,
                        scientificName = it.name,
                        commonName = it.preferred_common_name ?: "Unknown",
                        imageUrl = it.default_photo?.medium_url ?: ""
                    )
                }
                snakes.postValue(snakeList)
            }
        }
    }
}
