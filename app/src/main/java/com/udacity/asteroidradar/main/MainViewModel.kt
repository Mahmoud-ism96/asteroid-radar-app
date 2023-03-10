package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)

    private val asteroidRepository = AsteroidRepository(database)

    val asteroidRows = asteroidRepository.getRows

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateFilter(filter: Int) {
        _currentFilter.value = filter
        viewModelScope.launch { getAsteroids() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAsteroids(): List<Asteroid> {
        val list: List<Asteroid> = viewModelScope.async {
            return@async when (_currentFilter.value) {
                Constants.FILTER_DAY -> asteroidRepository.filterDay()
                Constants.FILTER_SAVED -> asteroidRepository.filterSaved()
                else -> asteroidRepository.filterWeek()
            }
        }.await()
        return list
    }

//    private val _asteroids = MutableLiveData<List<Asteroid>>()
//    val asteroids: LiveData<List<Asteroid>>
//        get() = _asteroids

    private val _currentFilter = MutableLiveData<Int>()
    val currentFilter : LiveData<Int> get() = _currentFilter

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay> get() = _pictureOfDay

    private val _navigateToDetailFragment = MutableLiveData<Asteroid>()
    val navigateToDetailFragment: LiveData<Asteroid>
        get() = _navigateToDetailFragment

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailFragment.value = asteroid
    }

    fun doneNavigating() {
        _navigateToDetailFragment.value = null
    }

    init {
        viewModelScope.launch {
            try {
                _currentFilter.value = 0
                asteroidRepository.refreshAsteroids()
                refreshPictureOfDay()
            } catch (err: Exception) {
                println("Error refreshing data: $err")
            }
        }
    }

    private suspend fun refreshPictureOfDay() {
        try {
            val result = AsteroidApi.retrofitService.getPictureOfDay(Constants.API_KEY)
            _pictureOfDay.value = result
        } catch (err: Exception) {
            println("Error refreshing picture: $err")
        }

    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}