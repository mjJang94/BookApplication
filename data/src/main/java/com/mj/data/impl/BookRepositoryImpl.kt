package com.mj.data.impl

import com.mj.data.bound.flowDataResource
import com.mj.data.remote.BookRemoteDataSource
import com.mj.data_resource.DataResource
import com.mj.domain.model.Book
import com.mj.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class BookRepositoryImpl @Inject constructor(
    private val bookRemoteDataSource: BookRemoteDataSource
): BookRepository {

    override fun getBooks(query: String, startIndex: Int): Flow<DataResource<List<Book>>> =
        flowDataResource { bookRemoteDataSource.getSummaryBooks(query, startIndex) }

}