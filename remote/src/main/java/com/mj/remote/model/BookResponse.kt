package com.mj.remote.model

import com.mj.data.model.BookEntity
import com.mj.remote.RemoteMapper

data class BookResponse(
    val kind: String,
    val id: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: VolumeResponse,
) : RemoteMapper<BookEntity> {

    override fun toData(): BookEntity =
        BookEntity(kind, id, etag, selfLink, volumeInfo.toData())
}