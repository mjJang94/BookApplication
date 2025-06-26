package com.mj.presentation.search.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mj.data_resource.mapListDataResource
import com.mj.domain.usecase.GetBookUseCase
import com.mj.presentation.search.base.BaseViewModel
import com.mj.presentation.search.base.ViewEvent
import com.mj.presentation.search.model.BookModel
import com.mj.presentation.search.model.toPresentation
import com.mj.presentation.search.search.BookSearchViewModel.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(
    private val getBookUseCase: GetBookUseCase
): BaseViewModel<Event>() {


    private val _books = MutableStateFlow<List<BookModel>>(emptyList())
    val books = _books.asStateFlow()

    fun search(query: String, startIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _books.value = getBookUseCase(query, startIndex).mapListDataResource{ it.toPresentation()}
                .await() ?: return@launch
            
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("BookSearchViewModel", "onCleared()")
    }



    sealed class Event: ViewEvent {

    }
}