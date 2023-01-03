package com.udacity.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AsteroidRepository(private val database: AsteroidDatabase) {

    suspend fun filterSaved() = database.asteroidDao.getAllAsteroids()

    @RequiresApi(Build.VERSION_CODES.O)
    val startDate: String = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)

    @RequiresApi(Build.VERSION_CODES.O)
    val endDate: String = LocalDateTime.now().plusDays(7).format(DateTimeFormatter.ISO_DATE)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun filterWeek() = database.asteroidDao.getAsteroidsWeek(
        startDate, endDate
    )

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun filterDay() = database.asteroidDao.getAsteroidsToday(
        startDate
    )

    val getRows = database.asteroidDao.getRows()

    suspend fun refreshAsteroids() {
        try {
            val asteroidList = AsteroidApi.retrofitService.getProperties(Constants.API_KEY)
            val result = parseAsteroidsJsonResult(JSONObject(asteroidList))
            if (result.isNotEmpty()) {
                database.asteroidDao.insertAll(*result.toTypedArray())
            } else println("Error Inserting Data")

        } catch (err: Exception) {
            println("Error refreshing asteroids: $err")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun deletePreviousDayAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deletePreviousDaysAsteroids(startDate)
        }
    }

}
