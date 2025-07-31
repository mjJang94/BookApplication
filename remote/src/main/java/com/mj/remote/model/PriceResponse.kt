package com.mj.remote.model

import com.mj.data.model.PriceEntity
import com.mj.remote.RemoteMapper

data class PriceResponse(
    val amount: Int?,
    val currencyCode: String?,
) : RemoteMapper<PriceEntity> {
    override fun toData(): PriceEntity =
        PriceEntity(amount= amount ?: 0, currencyCode = currencyCode.orEmpty())
}
