package com.mj.domain.model

data class Book(
    val id: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: Volume,
)
