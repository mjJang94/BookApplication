package com.mj.data.local

import kotlinx.coroutines.flow.Flow

interface BookLocalDataSource {

    fun searchKeywordsFlow(): Flow<List<String>>
    suspend fun addSearchKeyword(keyword: String)
    suspend fun deleteKeyword(keyword: String)
    suspend fun clearSearchKeywords()
}