package com.mj.data.model

data class VolumeEntity(
    val title: String,
    val authors: List<String>,
    val publishedDate: String,
    val printType: String,
    val infoLink: String
)