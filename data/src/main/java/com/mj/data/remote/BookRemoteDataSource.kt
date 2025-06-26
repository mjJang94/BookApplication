package com.mj.data.remote

import com.mj.data.model.BookEntity

interface BookRemoteDataSource {

    suspend fun getSummaryBooks(query: String, startIndex: Int): List<BookEntity>
}