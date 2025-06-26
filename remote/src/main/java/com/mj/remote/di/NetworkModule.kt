package com.mj.remote.di

import com.mj.remote.api.ApiService
import com.mj.remote.api.BASE_URL
import com.mj.remote.api.createApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService = createApiService(BASE_URL)
}