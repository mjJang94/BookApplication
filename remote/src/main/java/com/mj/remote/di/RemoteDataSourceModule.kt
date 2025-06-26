package com.mj.remote.di

import com.mj.data.remote.BookRemoteDataSource
import com.mj.remote.impl.BookRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RemoteDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindBookRemoteDataSource(source: BookRemoteDataSourceImpl): BookRemoteDataSource
}