package com.futureworkshops.dotgolf.data.repository

import com.futureworkshops.dotgolf.data.network.GreetingApi
import javax.inject.Inject
import javax.inject.Named

class DefaultGreetingRepository @Inject constructor(
    private val greetingApi: GreetingApi,
    @param:Named("greeting_fallback") private val fallbackGreeting: String
) : GreetingRepository {
    override suspend fun fetchGreeting(): String {
        return runCatching { greetingApi.getGreeting().message }
            .getOrElse { fallbackGreeting }
    }
}
