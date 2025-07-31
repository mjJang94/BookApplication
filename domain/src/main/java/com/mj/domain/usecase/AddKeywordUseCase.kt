package com.mj.domain.usecase

import com.mj.domain.repository.BookRepository
import javax.inject.Inject

class AddKeywordUseCase @Inject constructor(private val bookRepository: BookRepository){
    suspend operator fun invoke(keyword: String) = bookRepository.addSearchKeyword(keyword)
}