package com.udacity.asteroidradar.repository

import android.util.Log
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.AsteroidApi
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

//    val asteroids: List<Asteroid> =
//        Transformations.map(database.asteroidDao.getAsteroids()) {
//            it
//        }

    suspend fun filterSaved() = database.asteroidDao.getAsteroids()

    val getRows = database.asteroidDao.getRows()

    suspend fun refreshAsteroids() {
        try {
            val asteroidList = AsteroidApi.retrofitService.getProperties(Constants.API_KEY)
            val result = parseAsteroidsJsonResult(JSONObject(asteroidList))
            Log.i("Repo: Inserted", result.toString())
            if (result.isNotEmpty()) {
                database.asteroidDao.insertAll(*result.toTypedArray())
                Log.i("Repo: Inserted", "Success")
            } else
                Log.i("Repo: Inserted", "Failed")

        } catch (err: Exception) {
            println("Error refreshing asteroids: $err")
        }
    }


}
