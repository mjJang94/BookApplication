package com.mj.local.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.mj.data.local.BookLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): BookLocalDataSource {

    companion object {
        val SEARCH_KEYWORD = stringSetPreferencesKey("search_keyword")
    }

    override fun searchKeywordsFlow(): Flow<List<String>> =
        dataStore.data.map { preferences ->
            preferences[SEARCH_KEYWORD]?.toList() ?: emptyList()
        }

    override suspend fun addSearchKeyword(keyword: String) {
        dataStore.edit { preferences ->
            val current = preferences[SEARCH_KEYWORD]?.toMutableSet() ?: mutableSetOf()
            current.add(keyword)
            preferences[SEARCH_KEYWORD] = current
        }
    }

    override suspend fun deleteKeyword(keyword: String) {
        dataStore.edit { preferences ->
            val current = preferences[SEARCH_KEYWORD]?.toMutableSet() ?: return@edit
            current.remove(keyword)
            preferences[SEARCH_KEYWORD] = current
        }
    }

    override suspend fun clearSearchKeywords() {
        dataStore.edit {
            it.remove(SEARCH_KEYWORD)
        }
    }
}