package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroidTable ORDER BY closeApproachDate ASC")
    suspend fun getAllAsteroids(): List<Asteroid>

    @Query("SELECT * FROM asteroidTable WHERE closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate ASC")
    suspend fun getAsteroidsWeek(startDate: String, endDate: String): List<Asteroid>

    @Query("SELECT * FROM asteroidTable WHERE closeApproachDate = :startDate ORDER BY closeApproachDate ASC")
    suspend fun getAsteroidsToday(startDate: String): List<Asteroid>

    @Query("DELETE FROM asteroidTable WHERE closeApproachDate < :startDate")
    suspend fun deletePreviousDaysAsteroids(startDate: String): Int

    @Query("SELECT COUNT(id) FROM asteroidTable")
    fun getRows(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: Asteroid)

}

@Database(entities = [Asteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext, AsteroidDatabase::class.java, "asteroids"
            ).build()
        }
    }
    return INSTANCE
}