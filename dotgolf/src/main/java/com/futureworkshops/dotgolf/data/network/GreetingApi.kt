package com.futureworkshops.dotgolf.data.network

import com.futureworkshops.dotgolf.data.model.GreetingDto
import retrofit2.http.GET

interface GreetingApi {
    @GET("greeting")
    suspend fun getGreeting(): GreetingDto
}
