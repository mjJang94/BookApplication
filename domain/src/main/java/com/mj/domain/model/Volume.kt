package com.mj.domain.model

data class Volume(
    val title: String,
    val authors: List<String>,
    val publishedDate: String,
    val publisher: String,
    val printType: String,
    val infoLink: String,
    val imageLinks: ImageLinks,
)
