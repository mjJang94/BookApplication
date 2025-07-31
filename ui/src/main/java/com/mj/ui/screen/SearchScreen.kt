package com.mj.ui.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.fallback
import com.mj.presentation.search.model.BookModel
import com.mj.presentation.search.model.ImageLinksModel
import com.mj.presentation.search.model.VolumeModel
import com.mj.presentation.search.search.BookSearchViewModel
import com.mj.ui.R
import com.mj.ui.theme.PreviewTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun SearchScreen(
    viewModel: BookSearchViewModel
) {
    val pagingBookData = viewModel.bookPagingDataFlow.collectAsLazyPagingItems()
    val keywords by viewModel.searchKeywords.collectAsState()
    val searchStart by viewModel.isSearchStarted.collectAsStateWithLifecycle()

    SearchScreenContent(
        bookItems = pagingBookData,
        keywords = keywords,
        searchStart = searchStart,
        search = { q -> viewModel.search(q) },
        deleteKeyword = { q -> viewModel.deleteKeyword(q) },
        deleteAllKeywords = { viewModel.deleteAllKeywords() },
    )
}

@Composable
private fun SearchScreenContent(
    bookItems: LazyPagingItems<BookModel>,
    keywords: List<String>,
    searchStart: Boolean,
    search: (String) -> Unit,
    deleteKeyword: (String) -> Unit,
    deleteAllKeywords: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var searchDetailVisible by remember { mutableStateOf(false) }

    var defaultKeyword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        QueryTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            focusManager = focusManager,
            defaultKeyword = defaultKeyword,
            onBack = {
                focusManager.clearFocus()
                searchDetailVisible = false
            },
            onSearch = { q ->
                search(q)
                searchDetailVisible = false
            },
            onShowSearchHistories = { isVisible ->
                searchDetailVisible = isVisible
            }
        )

        if (searchDetailVisible) {
            SearchDetail(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                keywords = keywords,
                onDeleteKeyword = { deleteKeyword(it) },
                onDeleteAllKeywords = { deleteAllKeywords() },
                onSearchKeyword = {
                    focusManager.clearFocus()
                    defaultKeyword = it
                    search(it)
                    searchDetailVisible = false
                })
        } else {
            BookList(searchStart, bookItems)
        }
    }
}

@Composable
private fun ColumnScope.BookList(searchStart: Boolean, bookItems: LazyPagingItems<BookModel>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f)
    ) {
        when {
            searchStart -> item { LoadingItem() }
            bookItems.itemSnapshotList.isEmpty() -> item { WaitingItem() }
            else -> {
                items(count = bookItems.itemCount) { index ->
                    val book = bookItems[index]
                    if (book != null) {
                        BookItem(book.volumeInfo)
                    } else {
                        EmptyItem()
                    }
                }
                bookItems.apply {
                    when {
                        loadState.refresh is LoadState.Error -> {
                            val e = loadState.refresh as LoadState.Error
                            item {
                                LoadFailItem(e)
                            }
                        }

                        loadState.append is LoadState.Loading -> {
                            item {
                                AppendLoadingItem()
                            }
                        }

                        loadState.append is LoadState.Error -> {
                            val e = loadState.append as LoadState.Error
                            item {
                                NextLoadFailItem(e)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchDetail(
    modifier: Modifier = Modifier,
    keywords: List<String>,
    onDeleteAllKeywords: () -> Unit,
    onDeleteKeyword: (String) -> Unit,
    onSearchKeyword: (String) -> Unit,
) {

    var selectRemovable by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("선택 삭제", "전체 삭제")

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        if (keywords.isEmpty()) {
            Text(
                text = "검색 내역이 없습니다",
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.5f),
                fontSize = 14.sp,
            )
        } else {

            //androidx.compose.material3.tokens.IconButtonTokens.StateLayerSize = 40.dp
            Row(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "최근 검색어",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )

                Box {
                    if (selectRemovable) {
                        Text(
                            modifier = Modifier.clickable { selectRemovable = false },
                            text = "삭제 완료",
                            fontSize = 12.sp,
                            color = Color.Blue,
                            fontWeight = FontWeight.Bold,
                        )
                    } else {
                        IconButton(
                            onClick = { expanded = !expanded }
                        ) {
                            Icon(
                                modifier = Modifier.rotate(90f),
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = ""
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options.forEachIndexed { index, label ->
                                DropdownMenuItem(
                                    text = { Text(text = label) },
                                    onClick = {
                                        val result = index % 2 == 0
                                        if (result) {
                                            selectRemovable = true
                                        }else {
                                            onDeleteAllKeywords()
                                        }
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items(keywords) { k ->
                    KeywordHistoryItem(
                        keyword = k,
                        removable = selectRemovable,
                        onDelete = { onDeleteKeyword(k) },
                        onSearch = { onSearchKeyword(k) },
                    )
                }
            }
        }
    }
}

@Composable
private fun KeywordHistoryItem(
    keyword: String,
    removable: Boolean,
    onDelete: () -> Unit,
    onSearch: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable { if (removable) onDelete() else onSearch() }
            .background(color = Color.White, shape = RoundedCornerShape(20.dp))
            .border(border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.5f)), shape = RoundedCornerShape(20.dp))
            .padding(vertical = 5.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.widthIn(max = 100.dp),
            text = keyword,
            maxLines = 1,
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
        )

        if (removable) {
            Image(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Filled.Clear,
                contentDescription = ""
            )
        }
    }
}

@Composable
private fun LoadingItem() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
private fun AppendLoadingItem() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
private fun EmptyItem() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "존재하지 않는 도서입니다",
            textAlign = TextAlign.Center,
        )

    }
}

@Composable
private fun WaitingItem() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "검색어를 입력해 주세요",
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun LazyPagingItems<BookModel>.LoadFailItem(e: LoadState.Error) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.Companion
                .clickable { retry() },
            text = "데이터 로드 실패: ${e.error.localizedMessage}",
        )
    }
}

@Composable
private fun LazyPagingItems<BookModel>.NextLoadFailItem(e: LoadState.Error) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { retry() },
        text = "다음 페이지 로드 실패: ${e.error.localizedMessage}"
    )
}

@Composable
private fun BookItem(data: VolumeModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        AsyncImage(
            modifier = Modifier.size(100.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(data.imageLinks.smallThumbnail)
                .fallback(R.drawable.baseline_image_not_supported_24)
                .crossfade(true)
                .build(),
            contentDescription = "Book Thumbnail",
            onError = { error ->
                Log.e("AsyncImage", "이미지 로딩 실패: ${error.result.throwable}")
            }
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(text = data.title, fontSize = 16.sp, maxLines = 2, overflow = TextOverflow.Ellipsis, color = Color.Black, fontWeight = FontWeight.Bold)
            Text(text = data.authors.firstOrNull() ?: "정보 없음", fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color.Black)
            Text(text = data.publishedDate, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color.Black)
        }
    }
}

@Composable
private fun QueryTextField(
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    defaultKeyword: String,
    onBack: () -> Unit,
    onSearch: (String) -> Unit,
    onShowSearchHistories: (Boolean) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var lastSearchedQuery by remember { mutableStateOf<String?>(null) }

    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(defaultKeyword, selection = TextRange(defaultKeyword.length)))
    }

    LaunchedEffect(defaultKeyword) {
        textFieldValue = TextFieldValue(defaultKeyword, selection = TextRange(defaultKeyword.length))
    }

    LaunchedEffect(isFocused) {
        onShowSearchHistories(isFocused)
    }

    OutlinedTextField(
        modifier = modifier,
        value = textFieldValue,
        onValueChange = { textFieldValue = it },
        label = { Text("검색어") },
        singleLine = true,
        interactionSource = interactionSource,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (lastSearchedQuery == textFieldValue.text) return@KeyboardActions
                onSearch(textFieldValue.text)
                lastSearchedQuery = textFieldValue.text
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        leadingIcon = if (isFocused) {
            {
                IconButton(
                    onClick = {
                        onBack()
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_arrow_back_ios_24),
                        contentDescription = "",
                    )
                }
            }
        } else {
            null
        },
        trailingIcon = {
            val image = if (textFieldValue.text.isBlank()) {
                Icons.Outlined.Search
            } else {
                Icons.Filled.Search
            }

            IconButton(
                onClick = {
                    if (textFieldValue.text.isNotBlank()) {
                        if (lastSearchedQuery == textFieldValue.text) return@IconButton
                        onSearch(textFieldValue.text)
                        lastSearchedQuery = textFieldValue.text
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                }) {
                Icon(
                    imageVector = image,
                    contentDescription = "",
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun BookItemPreview() {
    PreviewTheme {

        val info = VolumeModel(
            title = "미리보기 책 제목",
            authors = listOf("작가 1", "작가 2"),
            publishedDate = "2023-01-01",
            printType = "book",
            infoLink = "",
            imageLinks = ImageLinksModel(
                smallThumbnail = "",
                thumbnail = "",
            )
        )
        BookItem(info)
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    PreviewTheme {

        val sampleBooks = List(10) { index ->
            BookModel(
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
            keywords = listOf("롱", "테스트1", "테스트1", "테스트1", "테스트1"),
            searchStart = false,
            search = {},
            deleteAllKeywords = {},
            deleteKeyword = {},
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