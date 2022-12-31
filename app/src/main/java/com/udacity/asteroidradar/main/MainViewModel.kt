package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.network.AsteroidApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _response = MutableLiveData<String>()

    val response: LiveData<String>
        get() = _response

    init {
        getAsteroidProperties()
    }

    private fun getAsteroidProperties() {
        AsteroidApi.retrofitService.getProperties().enqueue( object: Callback<List<Asteroid>> {
            override fun onFailure(call: Call<List<Asteroid>>, t: Throwable) {
                _response.value = "Failure: " + t.message
            }

            override fun onResponse(call: Call<List<Asteroid>>, response: Response<List<Asteroid>> ) {
                _response.value = "Success: ${response.body()?.size} Asteroids Retrieved"
            }
        })
    }
}