package com.mj.presentation.search.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mj.domain.model.Book

class BookPagingSource(
    private val dispatch: suspend (page: Int) -> List<Book>,
) : PagingSource<Int, BookModel>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookModel> {
        val currentPage = params.key ?: 1
        val response = dispatch(currentPage).map { it.toPresentation() }

        val nextKey = if (response.size < params.loadSize) {
            null
        }else {
            currentPage + 1
        }

        return LoadResult.Page(
            data = response,
            prevKey = if (currentPage == 1) null else currentPage - 1,
            nextKey = nextKey
        )
    }

    override fun getRefreshKey(state: PagingState<Int, BookModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}