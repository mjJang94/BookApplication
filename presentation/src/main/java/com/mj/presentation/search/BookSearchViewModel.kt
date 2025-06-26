package com.mj.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mj.domain.usecase.GetBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(
    private val getBookUseCase: GetBookUseCase
): ViewModel() {


    fun search(query: String, startIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val s = getBookUseCase(query, startIndex)
            
        }
    }
}