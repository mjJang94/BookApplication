package com.mj.remote.model

import com.mj.data.model.VolumeEntity
import com.mj.remote.RemoteMapper

data class VolumeResponse(
    val title: String?,
    val authors: List<String>?,
    val publishedDate: String?,
    val publisher: String?,
    val pageCount: Int?,
    val printType: String?,
    val language: String?,
    val infoLink: String?,
    val imageLinks: ImageLinkResponse?,
): RemoteMapper<VolumeEntity> {

    override fun toData(): VolumeEntity  {
        return VolumeEntity(title.orEmpty(), authors.orEmpty(), publishedDate.orEmpty(), publisher.orEmpty(), printType.orEmpty(), infoLink.orEmpty(), (imageLinks ?: ImageLinkResponse("", "")).toData())
    }

}