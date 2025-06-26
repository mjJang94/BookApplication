package com.mj.remote.model

import com.mj.data.model.VolumeEntity
import com.mj.remote.RemoteMapper

data class VolumeResponse(
    val title: String,
    val authors: List<String>,
    val publishedDate: String,
    val pageCount: Int,
    val printType: String,
    val language: String,
    val infoLink: String,
): RemoteMapper<VolumeEntity> {

    override fun toData(): VolumeEntity =
        VolumeEntity(title, authors, publishedDate, printType, infoLink)
}