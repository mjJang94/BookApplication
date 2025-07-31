package com.mj.data.model

import com.mj.data.DataMapper
import com.mj.domain.model.Book

data class BookEntity(
    val kind: String,
    val id: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: VolumeEntity,
    val saleInfo: SaleEntity,
) : DataMapper<Book> {
    override fun toDomain(): Book {
        return Book(kind, id, etag, selfLink, volumeInfo.toDomain(), saleInfo.toDomain())
    }

}
