package com.mj.remote.model

import com.mj.data.model.ImageLinksEntity
import com.mj.remote.RemoteMapper

data class ImageLinkResponse(
    val smallThumbnail: String?,
    val thumbnail: String?,
): RemoteMapper<ImageLinksEntity> {

    override fun toData(): ImageLinksEntity =
        ImageLinksEntity(smallThumbnail.orEmpty(), thumbnail.orEmpty())
}
