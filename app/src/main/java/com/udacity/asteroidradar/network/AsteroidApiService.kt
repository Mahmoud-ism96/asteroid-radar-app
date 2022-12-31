package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit =
    Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(
        MoshiConverterFactory.create(moshi)
    ).baseUrl(
        Constants.BASE_URL
    ).build()

interface AsteroidApiService {
    @GET("neo/rest/v1/feed?&api_key=krxvhB8G2ZK2NE2Tixf0RDXn4gJfoHpXiC7Woh0V")
    fun getProperties(): Call<List<Asteroid>>
}

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}