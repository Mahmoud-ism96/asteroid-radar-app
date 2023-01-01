package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataWorker(context: Context, pram: WorkerParameters) : CoroutineWorker(context, pram) {

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val videosRepository = AsteroidRepository(database)

        return try {
            videosRepository.refreshAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

}