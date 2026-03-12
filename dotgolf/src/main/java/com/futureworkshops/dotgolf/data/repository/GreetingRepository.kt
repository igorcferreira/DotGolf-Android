package com.futureworkshops.dotgolf.data.repository

interface GreetingRepository {
    suspend fun fetchGreeting(): String
}
