package com.mj.domain.usecase

import com.mj.domain.repository.BookRepository
import javax.inject.Inject

class GetBookUseCase @Inject constructor(private val bookRepository: BookRepository) {

    operator fun invoke(query: String, startIndex: Int) =
        bookRepository.getBooks(query, startIndex)
}