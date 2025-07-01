package com.mj.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mj.presentation.search.model.BookModel
import com.mj.ui.theme.PreviewTheme

@Composable
internal fun SearchScreen(
    bookData: List<BookModel>,
    onSearch: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        QueryTextField(
            onSearch = { q -> onSearch(q) }
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(bookData.size) { index ->
                Text(text = bookData[index].volumeInfo.title)
            }
        }
    }
}

@Composable
private fun QueryTextField(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = modifier,
        value = query,
        onValueChange = { query = it },
        label = { Text("검색어") },
        singleLine = true,
        trailingIcon = {
            val image = if (query.isBlank()) {
                Icons.Outlined.Search
            } else {
                Icons.Filled.Search
            }

            val description = if (query.isBlank()) "검색어 입력하기" else "검색"
            IconButton(onClick = { if (query.isNotBlank()) onSearch(query) }) {
                Icon(image, description)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun QueryTextFieldPreview() {
    PreviewTheme {
        Column {
            QueryTextField(
                onSearch = {}
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    PreviewTheme {
        SearchScreen(
            bookData = emptyList(),
            onSearch = {},
        )
    }
}