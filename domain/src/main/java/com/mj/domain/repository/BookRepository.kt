package com.mj.domain.repository

import com.mj.data_resource.DataResource
import com.mj.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    fun getBooksFlow(query: String, page: Int): Flow<DataResource<List<Book>>>
    suspend fun getBooks(query: String, page: Int): List<Book>

    fun keywordFlow(): Flow<List<String>>
    suspend fun addSearchKeyword(keyword: String)
    suspend fun deleteKeyword(keyword: String)
    suspend fun clearSearchKeywords()
}