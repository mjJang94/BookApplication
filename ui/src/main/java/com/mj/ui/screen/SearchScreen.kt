package com.mj.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mj.presentation.search.search.BookSearchViewModel

@Composable
fun SearchScreen(
    searchViewModel: BookSearchViewModel = hiltViewModel()
) {

    val bookData by searchViewModel.books.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            modifier = Modifier.clickable { searchViewModel.search("짱구", 0) },
            text = "load"
        )

        if (bookData.isNotEmpty()) {
            LazyColumn {
                items(bookData.size) { index ->
                    Text(text = bookData[index].volumeInfo.title)
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    SearchScreen()
}