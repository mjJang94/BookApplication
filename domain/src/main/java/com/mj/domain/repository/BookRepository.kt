package com.mj.domain.repository

import com.mj.data_resource.DataResource
import com.mj.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    fun getBooks(query: String, startIndex: Int): Flow<DataResource<List<Book>>>
}