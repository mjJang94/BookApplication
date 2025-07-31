package com.mj.data.impl

import com.mj.data.bound.flowDataResource
import com.mj.data.local.BookLocalDataSource
import com.mj.data.remote.BookRemoteDataSource
import com.mj.data_resource.DataResource
import com.mj.domain.model.Book
import com.mj.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class BookRepositoryImpl @Inject constructor(
    private val bookRemoteDataSource: BookRemoteDataSource,
    private val bookLocalDataSource: BookLocalDataSource,
): BookRepository {

    override fun getBooksFlow(query: String, page: Int): Flow<DataResource<List<Book>>> =
        flowDataResource { bookRemoteDataSource.getSummaryBooks(query, page) }

    override suspend fun getBooks(query: String, page: Int): List<Book> =
        bookRemoteDataSource.getSummaryBooks(query, page).map { it.toDomain() }

    override fun keywordFlow(): Flow<List<String>> =
        bookLocalDataSource.searchKeywordsFlow()

    override suspend fun addSearchKeyword(keyword: String) =
        bookLocalDataSource.addSearchKeyword(keyword)

    override suspend fun deleteKeyword(keyword: String) =
        bookLocalDataSource.deleteKeyword(keyword)

    override suspend fun clearSearchKeywords() =
        bookLocalDataSource.clearSearchKeywords()
}