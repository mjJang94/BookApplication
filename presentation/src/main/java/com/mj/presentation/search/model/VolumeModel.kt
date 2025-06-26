package com.mj.presentation.search.model

import com.mj.domain.model.Volume

data class VolumeModel(
    val title: String,
    val authors: List<String>,
    val publishedDate: String,
    val printType: String,
    val infoLink: String
)

fun Volume.toPresentation(): VolumeModel = VolumeModel(
    title,
    authors,
    publishedDate,
    printType,
    infoLink
)
