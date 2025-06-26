package com.mj.data.di

import com.mj.data.impl.BookRepositoryImpl
import com.mj.domain.repository.BookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBookRepository(repo: BookRepositoryImpl): BookRepository
}