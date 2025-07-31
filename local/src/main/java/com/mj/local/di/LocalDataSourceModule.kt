package com.mj.local.di

import com.mj.data.local.BookLocalDataSource
import com.mj.local.impl.BookLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindBookLocalDataSource(source: BookLocalDataSourceImpl): BookLocalDataSource
}