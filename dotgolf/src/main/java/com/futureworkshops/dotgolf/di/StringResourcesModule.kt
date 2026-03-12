package com.futureworkshops.dotgolf.di

import android.content.Context
import com.futureworkshops.dotgolf.theme.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StringResourcesModule {
    @Provides
    @Singleton
    @Named("greeting_loading")
    fun provideGreetingLoading(
        @ApplicationContext context: Context
    ): String {
        return context.getString(R.string.greeting_loading)
    }

    @Provides
    @Singleton
    @Named("greeting_fallback")
    fun provideGreetingFallback(
        @ApplicationContext context: Context
    ): String {
        return context.getString(R.string.greeting_fallback)
    }
}
