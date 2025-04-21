package com.rahul.safebite.SnakeRescue

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class SnakeCatcherViewModel : ViewModel() {

    private val _snakeCatchers = MutableLiveData<List<SnakeCatcher>>()
    val snakeCatchers: LiveData<List<SnakeCatcher>> = _snakeCatchers

    fun fetchCatchers() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getSnakeCatchers(
                    lat = 19.1125,
                    lon = 77.2943
                )
                val list = response.results.mapNotNull {
                    val name = it.poi?.name ?: return@mapNotNull null
                    val phone = it.poi.phone?.firstOrNull() ?: "N/A"
                    val location = it.address?.municipality ?: "Unknown"
                    SnakeCatcher(name, phone, location)
                }
                _snakeCatchers.postValue(list)
            } catch (e: Exception) {
                e.printStackTrace()
                _snakeCatchers.postValue(emptyList())
            }
        }
    }
}
