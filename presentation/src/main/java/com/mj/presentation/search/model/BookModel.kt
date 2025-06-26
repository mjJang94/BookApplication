package com.mj.presentation.search.model

import com.mj.domain.model.Book

data class BookModel(
    val id: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: VolumeModel,
)

fun Book.toPresentation(): BookModel = BookModel(
    id,
    etag,
    selfLink,
    volumeInfo.toPresentation()
)
