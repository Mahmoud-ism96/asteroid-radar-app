package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        try {
            withContext(Dispatchers.IO) {
                val asteroidList = AsteroidApi.retrofitService.getProperties(Constants.API_KEY)
                val result = parseAsteroidsJsonResult(JSONObject(asteroidList))
                if (result.isNotEmpty())
                    database.asteroidDao.insertAll(*result.asDatabaseModel())
                else
                    println("Error result is Empty")
            }
        } catch (err: Exception) {
            println("Error refreshing asteroids: $err")
        }
    }
}
