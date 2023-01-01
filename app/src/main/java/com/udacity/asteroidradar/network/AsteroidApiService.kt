package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()


interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getProperties(
        @Query("api_key") api_key: String
    ): String
}

object AsteroidApi {
    private val retrofit =
        Retrofit.Builder().baseUrl(
            Constants.BASE_URL
        ).addConverterFactory(ScalarsConverterFactory.create()).build()

    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}