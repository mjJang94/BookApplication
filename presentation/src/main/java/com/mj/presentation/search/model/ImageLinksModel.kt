package com.mj.presentation.search.model

import com.mj.domain.model.ImageLinks

data class ImageLinksModel(
    val smallThumbnail: String?,
    val thumbnail: String?,
)

fun ImageLinks.toPresentation(): ImageLinksModel = ImageLinksModel(
    smallThumbnail,
    thumbnail,
)