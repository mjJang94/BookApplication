package com.mj.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.mj.presentation.search.model.BookModel
import com.mj.presentation.search.model.VolumeModel
import com.mj.ui.theme.PreviewTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun SearchScreen(
    bookData: LazyPagingItems<BookModel>,
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

        if (bookData.itemCount < 1) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("검색어를 입력해주세요.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(count = bookData.itemCount) { index ->
                    val book = bookData[index]
                    if (book != null) {
                        Text(book.volumeInfo.title)
                    } else {
                        Text("검색 결과 없음")
                    }
                }
                bookData.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item {
                                Column(
                                    modifier = Modifier.fillParentMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        loadState.append is LoadState.Loading -> {
                            item {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                                }
                            }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val e = bookData.loadState.refresh as LoadState.Error
                            item {
                                Text(
                                    modifier = Modifier
                                        .fillParentMaxSize()
                                        .clickable { retry() },
                                    text = "데이터 로드 실패: ${e.error.localizedMessage}",
                                )
                            }
                        }
                    }
                }
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
    var lastSearchedQuery by remember { mutableStateOf<String?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current

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
            IconButton(onClick = {
                if (query.isNotBlank()) {
                    if (lastSearchedQuery != query) {
                        onSearch(query)
                        lastSearchedQuery = query
                        keyboardController?.hide()
                    }
                }
            }) {
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

        val sampleBooks = List(10) { index ->
            BookModel(
                // id = "id_$index", // BookModel에 id 필드가 있다면 추가
                id = index.toString(),
                etag = "",
                selfLink = "",
                volumeInfo = VolumeModel(
                    title = "미리보기 책 제목 ${index + 1}",
                    authors = listOf("작가 ${index + 1}"),
                    publishedDate = "2023-01-01",
                    printType = "book#$index",
                    infoLink = "",
                )
            )
        }

        SearchScreen(
            bookData = rememberPreviewLazyPagingItems(data = sampleBooks),
            onSearch = {},
        )
    }
}

@Composable
fun rememberPreviewLazyPagingItems(
    data: List<BookModel> = emptyList()
): LazyPagingItems<BookModel> {
    val flow: Flow<PagingData<BookModel>> = flowOf(PagingData.from(data))
    return flow.collectAsLazyPagingItems()
}