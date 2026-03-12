package com.futureworkshops.dotgolf.di

import com.futureworkshops.dotgolf.data.repository.DefaultGreetingRepository
import com.futureworkshops.dotgolf.data.repository.GreetingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GreetingModule {
    @Binds
    @Singleton
    abstract fun bindGreetingRepository(
        repository: DefaultGreetingRepository
    ): GreetingRepository
}
