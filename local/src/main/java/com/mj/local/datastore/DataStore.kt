package com.mj.local.datastore

import kotlinx.coroutines.flow.Flow

interface DataStore {
    fun searchKeywordsFlow(): Flow<List<String>>
    suspend fun addSearchKeyword(keyword: String)
    suspend fun deleteKeyword(keyword: String)
    suspend fun clearSearchKeywords()
}