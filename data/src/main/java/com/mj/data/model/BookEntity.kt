package com.mj.data.model

data class BookEntity(
    val id: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: VolumeEntity,
)
