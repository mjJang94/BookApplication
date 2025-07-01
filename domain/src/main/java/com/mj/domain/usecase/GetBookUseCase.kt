package com.mj.domain.usecase

import com.mj.domain.repository.BookRepository
import javax.inject.Inject

class GetBookUseCase @Inject constructor(private val bookRepository: BookRepository) {

    suspend operator fun invoke(query: String, page: Int) =
        bookRepository.getBooks(query, page)

    fun flow(query: String, page: Int) =
        bookRepository.getBooksFlow(query, page)
}