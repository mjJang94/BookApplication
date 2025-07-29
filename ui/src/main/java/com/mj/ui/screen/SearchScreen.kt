package com.mj.ui.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mj.presentation.search.model.BookModel
import com.mj.presentation.search.model.ImageLinksModel
import com.mj.presentation.search.model.VolumeModel
import com.mj.presentation.search.search.BookSearchViewModel
import com.mj.ui.theme.PreviewTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun SearchScreen(
    viewModel: BookSearchViewModel
) {
    val pagingBookData = viewModel.bookPagingDataFlow.collectAsLazyPagingItems()
    val searchStart by viewModel.isSearchStarted.collectAsStateWithLifecycle()

    SearchScreenContent(
        bookItems = pagingBookData,
        searchStart = searchStart,
        search = { q -> viewModel.search(q) }
    )
}

@Composable
private fun SearchScreenContent(
    bookItems: LazyPagingItems<BookModel>,
    searchStart: Boolean,
    search: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        QueryTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            onSearch = { q -> search(q) },
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            if (searchStart) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            } else if (bookItems.itemCount < 1) {
                item {
                    Text(
                        text = "검색어를 입력해 주세요.",
                    )
                }
            } else {
                items(count = bookItems.itemCount) { index ->
                    val book = bookItems[index]
                    if (book != null) {
                        BookItem(book.volumeInfo)
                    } else {
                        Text("존재하지 않는 도서입니다.")
                    }
                }
                bookItems.apply {
                    when {
                        loadState.refresh is LoadState.Error -> {
                            val e = loadState.refresh as LoadState.Error
                            item {
                                Text(
                                    modifier = Modifier
                                        .fillParentMaxSize()
                                        .clickable { retry() },
                                    text = "데이터 로드 실패: ${e.error.localizedMessage}",
                                )
                            }
                        }

                        loadState.append is LoadState.Loading -> {
                            Log.d("SearchScreen", "loadState.append is LoadState.Loading")
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }
                        }

                        loadState.append is LoadState.Error -> {
                            val e = loadState.append as LoadState.Error
                            item {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { retry() },
                                    text = "다음 페이지 로드 실패: ${e.error.localizedMessage}"
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
private fun BookItem(data: VolumeModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {


        AsyncImage(
            modifier = Modifier.size(100.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(data.imageLinks.smallThumbnail)
                .crossfade(true)
                .build(),
            contentDescription = "Book Thumbnail",
            onError = { error ->
                Log.e("AsyncImage", "이미지 로딩 실패: ${error.result.throwable}")
            }
        )
        Text(data.title)
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
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (lastSearchedQuery == query) return@KeyboardActions
                onSearch(query)
                lastSearchedQuery = query
                keyboardController?.hide()
            }
        ),
        trailingIcon = {
            val image = if (query.isBlank()) {
                Icons.Outlined.Search
            } else {
                Icons.Filled.Search
            }

            val description = if (query.isBlank()) "검색어 입력하기" else "검색"
            IconButton(onClick = {
                if (query.isNotBlank()) {
                    if (lastSearchedQuery == query) return@IconButton
                    onSearch(query)
                    lastSearchedQuery = query
                    keyboardController?.hide()
                }
            }) {
                Icon(image, description)
            }
        }
    )
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
                    imageLinks = ImageLinksModel(
                        smallThumbnail = "",
                        thumbnail = "",
                    )
                )
            )
        }

        SearchScreenContent(
            bookItems = rememberPreviewLazyPagingItems(data = sampleBooks),
            searchStart = false,
            search = {},
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