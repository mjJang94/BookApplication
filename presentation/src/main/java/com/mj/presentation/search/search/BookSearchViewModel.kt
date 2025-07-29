package com.mj.presentation.search.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mj.domain.usecase.GetBookUseCase
import com.mj.presentation.search.base.BaseViewModel
import com.mj.presentation.search.base.ViewEvent
import com.mj.presentation.search.model.BookModel
import com.mj.presentation.search.model.BookPagingSource
import com.mj.presentation.search.search.BookSearchViewModel.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(
    private val getBookUseCase: GetBookUseCase
) : BaseViewModel<Event>() {


    private val _books = MutableStateFlow<List<BookModel>>(emptyList())
    val books = _books.asStateFlow()

    val isSearchStarted = MutableStateFlow(false)
    private val _query = MutableStateFlow("")
    fun search(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isSearchStarted.value = true
            _query.emit(query)

        }
    }

    val bookPagingDataFlow: Flow<PagingData<BookModel>> = _query.flatMapLatest { q ->
        if (q.isBlank()) {
            emptyFlow()
        } else {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false,
                    initialLoadSize = 20,
                ),
                pagingSourceFactory = {
                    BookPagingSource { page ->
                        runCatching {
                            getBookUseCase(q, page)
                        }.onSuccess {
                            isSearchStarted.value = false
                        }.onFailure {
                            isSearchStarted.value = false
                        }.getOrDefault(emptyList())
                    }
                }
            ).flow.cachedIn(viewModelScope)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("BookSearchViewModel", "onCleared()")
    }


    sealed class Event : ViewEvent {
    }
}