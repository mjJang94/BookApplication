package com.mj.remote.impl

import com.mj.data.model.BookEntity
import com.mj.data.remote.BookRemoteDataSource
import com.mj.remote.api.ApiService
import javax.inject.Inject

class BookRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
): BookRemoteDataSource {

    override suspend fun getSummaryBooks(query: String, startIndex: Int): List<BookEntity> =
        apiService.getBookSearch(query = query, startIndex = startIndex).items.map { it.toData() }
}