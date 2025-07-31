package com.mj.presentation.search.model

import com.mj.domain.model.Book

data class BookModel(
    val id: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: VolumeModel,
    val saleInfo: SaleModel,
)

fun Book.toPresentation(): BookModel = BookModel(
    id,
    etag,
    selfLink,
    volumeInfo.toPresentation(),
    saleInfo.toPresentation(),
)
