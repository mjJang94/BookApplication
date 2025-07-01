package com.mj.data.model

import com.mj.data.DataMapper
import com.mj.domain.model.ImageLinks

data class ImageLinksEntity(
    val smallThumbnail: String?,
    val thumbnail: String?,
) : DataMapper<ImageLinks?> {
    override fun toDomain(): ImageLinks = ImageLinks(smallThumbnail ?: "", thumbnail ?: "")
}
