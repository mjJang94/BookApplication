package com.mj.data.model

import com.mj.data.DataMapper
import com.mj.domain.model.Volume

data class VolumeEntity(
    val title: String,
    val authors: List<String>,
    val publishedDate: String,
    val publisher: String,
    val printType: String,
    val infoLink: String,
    val imageLinks: ImageLinksEntity
) : DataMapper<Volume> {
    override fun toDomain(): Volume =
        Volume(title, authors, publishedDate, publisher, printType, infoLink, imageLinks.toDomain())

}