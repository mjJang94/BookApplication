package com.mj.domain.usecase

import com.mj.domain.repository.BookRepository
import javax.inject.Inject

class GetKeywordUseCase @Inject constructor(private val bookRepository: BookRepository) {
    operator fun invoke() = bookRepository.keywordFlow()
}