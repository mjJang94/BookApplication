package com.mj.domain.usecase

import com.mj.domain.repository.BookRepository
import javax.inject.Inject

class DeleteKeywordUseCase @Inject constructor(private val bookRepository: BookRepository){
    suspend operator fun invoke(keyword: String) = bookRepository.deleteKeyword(keyword)

    suspend fun deleteAll() = bookRepository.clearSearchKeywords()
}